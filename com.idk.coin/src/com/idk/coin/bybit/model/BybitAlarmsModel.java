package com.idk.coin.bybit.model;

import java.util.ArrayList;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.BybitAlarmPrice;
import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.db.BybitAlarmDao;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.model.AlarmManager;

abstract public class BybitAlarmsModel extends AlarmManager{
	
	public boolean DEBUGGING = true;
	public boolean ENABLE 	 = true;
	public boolean DISABLE 	 = false;
	
	public BybitAlarmsModel(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
		
	}
	public BybitAlarmsModel(String symbol, String web_id,String web_pw) throws Exception{
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
		    	user = new BybitUser();
		    	if(System.getProperty("BYBIT_API_KEY_" + web_id.toUpperCase()) == null) throw e;
		    	user.setApi_key(System.getProperty("BYBIT_API_KEY_" + web_id.toUpperCase()));
		    	user.setApi_secret(System.getProperty("BYBIT_API_SECRET" + web_id.toUpperCase()));
		    }
	}
	//abstract public void userSet() throws Exception;
	//abstract public void alarmSet() throws Exception;
	
	/* ###################    OPEN LONG   ##################### */
	public AlarmPrice openLong(double trigger, boolean is_over, double price, double qty, int is_reverse)throws Exception {
		return addOpenLong(trigger, is_over, price, qty, is_reverse);
	}
	public AlarmPrice openLong(double close, boolean is_over, double price, double qty, int is_reverse,double trigger)throws Exception {
		if(is_over == OVER) {
			if(trigger < price) throw new Exception("[Warning] trigger < price [Long]");
		}
		return makeOpenLong(trigger, is_over, price, qty, close, is_reverse);
	}
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty, int is_reverse)throws Exception {
		AlarmPrice a = new BybitAlarmPrice(this, trigger, is_over, is_reverse);
		a.setOpenLongAction(price, qty);
		if(is_reverse > ONCE && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < open_price [Long] for reverse"); 
			//addOpenLong(18910, OVER, 18930, QTY, RR); //18920 무한
		}
		addAlarm(a);
		return a;
	}
 
	public AlarmPrice makeOpenLong(double trigger, boolean is_over, double open_price, double qty, double close_price, int is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenLong(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new BybitAlarmPrice(this, open_price, UNDER, is_repeat);
		closeAlarm.setCloseLongAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
		return openAlarm;
	}
	
	
	
	/* ###################    CLOSE LONG   ##################### */
	public AlarmPrice closeLong(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception {
		return addCloseLong(trigger, is_over, price, qty, is_reverse);
	}
	public AlarmPrice addCloseLong(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception {
		AlarmPrice a = new BybitAlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseLongAction(price, qty);
		if(is_reverse > 1 && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > close_price [Long] for reverse"); 
		}
		addAlarm(a);
		return a;
	}
	 
	public AlarmPrice makeCloseLong(double trigger, boolean is_over, double close_price, double qty, double open_price, int is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseLong(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new BybitAlarmPrice(this, close_price, OVER, is_repeat); //trigger 
		openAlarm.setOpenLongAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
		return closeAlarm;
	}
	
	
	/* ###################    OPEN SHORT   ##################### */
	public AlarmPrice openShort(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception {
		return addOpenShort(trigger, is_over, price, qty, is_reverse);
	}
	public AlarmPrice openShort(double close,boolean is_over,double price,double qty,int is_reverse,double trigger)throws Exception {
		if(is_over == UNDER) {
			if(trigger > price) throw new Exception("[Warning] trigger > price [Short]");
		}
		return makeOpenShort(trigger, is_over, price, qty, close, is_reverse);
	}
	public AlarmPrice addOpenShort(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception {
		AlarmPrice a = new BybitAlarmPrice(this, trigger, is_over,is_reverse);
		a.setOpenShortAction(price, qty);
		if(is_reverse > ONCE && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > open_price [Short] for reverse");
		}
		addAlarm(a);
		return a;
	}
	public AlarmPrice makeOpenShort(double trigger, boolean is_over, double open_price, double qty, double close_price,int is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenShort(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new BybitAlarmPrice(this, open_price, OVER, is_repeat);
		closeAlarm.setCloseShortAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
		return openAlarm;
	}
 
	
	
	/* ###################    CLOSE SHORT   ##################### */
	public AlarmPrice closeShort(double trigger,boolean is_over,double price,double qty,int is_reverse) throws Exception {
		return addCloseShort(trigger, is_over, price, qty, is_reverse);
	}
	public AlarmPrice addCloseShort(double trigger,boolean is_over,double price,double qty,int is_reverse) throws Exception {
		AlarmPrice a = new BybitAlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseShortAction(price, qty);
		if(is_reverse> 1 && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < close_price [Short] for reverse");
			 //ex) addCloseShort(18910, OVER, 18930, QTY, RR); // 18920 무한 
		}
		addAlarm(a);
		return a;
	}
	public AlarmPrice makeCloseShort(double trigger, boolean is_over, double close_price, double qty, double open_price, int is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseShort(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new BybitAlarmPrice(this, close_price, UNDER, is_repeat);
		openAlarm.setOpenShortAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
		return closeAlarm;
	}
	public void status() {
		try {
			LOG.info(current_price + " , "  + this.getSize() + "  : " + this.getClass().getName());
			ArrayList<Position> ps 		= PositionRest.getActiveMyPosition(user.getApi_key(),user.getApi_secret(), symbol);
			Balance balance 		 =   WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
			Position buy =  Position.getPosition(ps, symbol, "Buy");
			Position sell = Position.getPosition(ps, symbol, "Sell");
			LOG.info(buy.getSize() + " , [" +  String.format("%.2f",(buy.getSize()/QTY)/10) + "]:" 
					 + "[" +  String.format("%.2f", (sell.getSize()/QTY)/10)+"] , "+ sell.getSize()
			 		+" , ["+String.format("%.2f",balance.getEquity()) + "]");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	int percent = 5;
	public void checkAlarmIdles(int percent) {
		this.percent = percent;
		double price = getCurrentPrice();
		double per	=  (price / 100) * percent;
		double over_price =  price + per;
		double under_price = price - per;
		int oldActives = list.size();
		int oldIdles   = idles.size();
		
		checkActives(getCurrentPrice(), over_price, under_price);
		checkIdles(getCurrentPrice() , over_price, under_price);
		LOG.info("+"+percent+"% ["+over_price + "] ~  ["+price+"] ~  [" + under_price+"] -"+percent+"%");
	
		LOG.info("Active :  " +oldActives+" -> " +list.size() + " , Idles : " +oldIdles +" -> "+idles.size());
	}
	public void checkActives(double price, double over, double under) {
		
		LOG.info(over + " **** actives -- > idles ****" + under);
		AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
		
		for(AlarmPrice alarm : obj) {
			double trigger = alarm.trigger;
			boolean is_over = alarm.isIs_over();
			boolean is_move	= false;
			if(is_over) {
				if(over < trigger) is_move = true;
			}else {
				if(under > trigger) is_move = true;
			}
			
			if(is_move) {
				deleteDatabase(alarm);
				AlarmPrice next = alarm.getNext();
				if(next != null) {
					next.setIs_active(false);
					deleteDatabase(next);
				}
				alarm.setIs_active(false);
				list.remove(alarm);
				idles.add(alarm);
				LOG.info("[알람 비활성] 되었습니다. : " + alarm);
			}
			
		}
	}
	public void checkIdles(double price, double over, double under) {
		LOG.info("**************** check idles - > actives ***********");
		AlarmPrice[] obj = idles.toArray(new AlarmPrice[0]);
		
		for(AlarmPrice alarm : obj) {
			double trigger = alarm.trigger;
			boolean is_over = alarm.isIs_over();
			boolean is_move	= false;
			if(is_over) {
				if(over > trigger) is_move = true;
			}else {
				if(under < trigger) is_move = true;
			}
			
			if(is_move) {
				idles.remove(alarm);
				list.add(alarm);
				AlarmPrice next = alarm.getNext();
				alarm.setIs_active(true);
				insertDatabase(alarm);
				if(next != null) {
					next.setIs_active(true);
					insertDatabase(next);
				}
				
				LOG.info("[알람 활성화] 되었습니다. : " + alarm);
			}
			
		}
	}
	
	public void enableDatabase(boolean enable) {
		super.enableDatabase(enable);
		if(!enable) LOG.info("데이터베이스 사용하지 않습니다.");
		clearAlarmDatabase();
		registerAlarmDatabase();
	}
	public void loadAlarmDatabase() {
		if(!db_enable) return;
	}
	public void registerAlarmDatabase() {
		if(!db_enable) return;
		LOG.info("★★★["+user.getUser_id()+"]★★★★★\t[알람데이터베이스 등록]\t★★★["+getSymbol()+"]★★★★★");
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
		if(!db_enable) return;
		LOG.info("★★★["+user.getUser_id()+"]★★★★★\t[알람데이터베이스 전체 삭제]\t★★★["+getSymbol()+"]★★★★★");
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