package com.idk.coin.binance.alarm;

import java.util.ArrayList;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.db.BybitAlarmDao;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.model.AlarmManager;

abstract public class BinanceAlarmsModel extends AlarmManager{
	
	public BinanceAlarmsModel(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
		
	}
	public BinanceAlarmsModel(String symbol, String web_id,String web_pw) throws Exception{
		this(symbol , null);
		this.web_id = web_id;
		setDB(web_id, web_pw);
		
	}
	public BybitUser getUser() {
		return user;
	}
	public void setDB(String web_id, String web_pw) throws Exception{
		  try {
		    	user =  BybitDao.getInstace().selectUser(web_id, web_pw);
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
	}
 
	
	
	/* ###################    OPEN LONG   ##################### */
 
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty, int is_reverse)throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over, is_reverse);
		a.setOpenLongAction(price, qty);
		if(is_reverse>1 && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < open_price [Long] for reverse"); 
			//addOpenLong(18910, OVER, 18930, QTY, RR); //18920 무한
		}
		addAlarm(a);
		return a;
	}
 
	public AlarmPrice makeOpenLong(double trigger, boolean is_over, double open_price, double qty, double close_price, int is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenLong(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new BinanceAlarmPrice(this, open_price, UNDER, is_repeat);
		closeAlarm.setCloseLongAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
		return openAlarm;
	}
	
	
	
	/* ###################    CLOSE LONG   ##################### */
 
	public AlarmPrice addCloseLong(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseLongAction(price, qty);
		if(is_reverse>1 && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > close_price [Long] for reverse"); 
		}
		addAlarm(a);
		return a;
	}
	 
	public AlarmPrice makeCloseLong(double trigger, boolean is_over, double close_price, double qty, double open_price, int is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseLong(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new BinanceAlarmPrice(this, close_price, OVER, is_repeat); //trigger 
		openAlarm.setOpenLongAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
		return closeAlarm;
	}
	
	
	/* ###################    OPEN SHORT   ##################### */
	 
	public AlarmPrice addOpenShort(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over,is_reverse);
		a.setOpenShortAction(price, qty);
		if(is_reverse>ONCE && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > open_price [Short] for reverse");
		}
		addAlarm(a);
		return a;
	}
	 
	public AlarmPrice makeOpenShort(double trigger, boolean is_over, double open_price, double qty, double close_price,int is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenShort(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new BinanceAlarmPrice(this, open_price, OVER, is_repeat);
		closeAlarm.setCloseShortAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
		return openAlarm;
	}
 
	
	
	/* ###################    CLOSE SHORT   ##################### */
	 
	public AlarmPrice addCloseShort(double trigger,boolean is_over,double price,double qty,int is_reverse) throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseShortAction(price, qty);
		if(is_reverse>ONCE && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < close_price [Short] for reverse");
			 //ex) addCloseShort(18910, OVER, 18930, QTY, RR); // 18920 무한 
		}
		addAlarm(a);
		return a;
	}
	public AlarmPrice makeCloseShort(double trigger, boolean is_over, double close_price, double qty, double open_price, int is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseShort(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new BinanceAlarmPrice(this, close_price, UNDER, is_repeat);
		openAlarm.setOpenShortAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
		
		return closeAlarm;
	}
	
	public void enableDatabase(boolean enable) {
		super.enableDatabase(enable);
		clearAlarmDatabase();
		registerAlarmDatabase();
	}
	public void checkAlarmIdles(int percent){
		
	}
	public void loadAlarmDatabase() {
		if(!db_enable) return;
	}
	public void registerAlarmDatabase() {
		LOG.info("★★★["+user.getUser_id()+"]★★★★★\t[알람데이터베이스 등록]\t★★★["+getSymbol()+"]★★★★★");
		if(!db_enable) return;
		try {
			ArrayList<BybitUser>  users = BybitDao.getInstace().selectUserList();
			int i=1; 
			synchronized(list) {
				AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
				for(AlarmPrice alarm : obj) {
					int lastIdx = BybitAlarmDao.getInstace().insert(alarm);
					alarm.setAlarm_id(String.valueOf(lastIdx));
					if(alarm.getNext() != null) {
						AlarmPrice next = alarm.getNext();
						next.setParent_alarm_id(alarm.getAlarm_id());
						lastIdx = BybitAlarmDao.getInstace().insert(next);
						next.setAlarm_id(String.valueOf(lastIdx));
						BybitAlarmDao.getInstace().update(alarm);
					}
				}
			}
			LOG.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void clearAlarmDatabase() {
		LOG.info("★★★["+user.getUser_id()+"]★★★★★\t[알람데이터베이스 전체 삭제]\t★★★["+getSymbol()+"]★★★★★");
		if(!db_enable) return;
		try {
			BybitAlarmDao.getInstace().deleteAll(getUser().getUser_id(), getSymbol());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void deleteDatabase(AlarmPrice alarm) {
		if(!db_enable) return;
		try {
			BybitAlarmDao.getInstace().delete(alarm);
		}catch(Exception e) {
			
		}
	}
	public int insertDatabase(AlarmPrice alarm) {
		if(!db_enable) return 0;
		try {
			return BybitAlarmDao.getInstace().insert(alarm);
		}catch(Exception e) {
			
		}
		return 0;
	}
	public void updateDatabase(AlarmPrice alarm	) {
		if(!db_enable) return;
		try {
			BybitAlarmDao.getInstace().update(alarm);
		}catch(Exception e) {
			
		}
	}
	
}