package com.idk.coin.bybit.model;

import java.util.ArrayList;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.BybitAlarmPrice;
import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.alram.CalculateModel;
import com.idk.coin.bybit.db.BybitAlarmDao;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.model.AlarmManager;

abstract public class BybitAlarmsModel extends AlarmManager{
	
	public final static boolean DEBUG_ON  = true;
	public final static boolean DEBUG_OFF = false;
	
	public final static boolean ENABLE 	 = true;
	public final static boolean DISABLE 	 = false;
	

	
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
	
	
	public void checkBalance() throws Exception{
		LOG.info("["+user.getId()+"] Actives:"+list.size() + ",Idles:"+idles.size()
		+",설정가 : [" + calculator.getPrice()+"]" +"현재가 : " + getCurrentPrice()
		+" 알람 리셋 : " + reset_check_time + "/"+RESET_TIME+"분, 알랑 활성체크: " 
		+ idle_check_time + "/"+IDLE_TIME+"분");
	
		
		Balance balance 	   	=  WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
		double oldEquity		= 0.0;
		if(startCalculateModel != null) {
			oldEquity	= startCalculateModel.getBalance().getEquity();
		}
	
		double newEquity 	= balance.getEquity();
		double profit	 	= oldEquity == 0 ? 0 : newEquity - oldEquity;
		double per		 	= profit == 0 ? 0 : profit / (oldEquity/100);
		LOG.info("Balance : " + newEquity + " - " + oldEquity + "  =  " + profit + "  ( "+ per+" %)" );
	}

	public void currentStatus(boolean debug) throws Exception{
		double price = getCurrentPrice();
		int count = 5;
		while(price < 0	) {
			price = getCurrentPrice();
			thread.sleep(1000 * 1);
			System.out.println("wait");
			if(count-- < 0) break;
		}
		if(price > 0) {
			ArrayList<Position> ps	=  PositionRest.getActiveMyPosition(user.getApi_key(),user.getApi_secret(), symbol);
			Balance balance 	   	=  WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
			Position buy			=  Position.getPosition(ps, symbol, "Buy");
			Position sell 			=  Position.getPosition(ps, symbol, "Sell");
			
			if(checkClearProfit(price, balance, buy, sell)) return;
			
			calculator = createCalculateModel(this,price, buy, sell, balance, QTY, debug);
			//new CalculatePositionV3(this,price, buy, sell, balance, QTY, debug);
			//calculator.setSizeValue(max_open_size, over_open_size, min_open_size);
			calculator.calculateStatus();
			
			LOG.debug(calculator.toAlarmString());
			insertAlarmMessageToDatabase(calculator);
			
			if(startCalculateModel == null)  	{
				startCalculateModel = calculator;
				if(startCalculateEquity > 0 ) startCalculateModel.getBalance().setEquity(startCalculateEquity);
			}
			
			reset_check_time = RESET_TIME;
			
		}else {
			LOG.info("아직 price 를 가져오지 못했슨니다." +getCurrentPrice() );
		}
	}
	
	public int message_count = 1;
	public void insertAlarmMessageToDatabase(CalculateModel cal) throws Exception{
		if(!db_enable) return;
		if(startCalculateModel != null) LOG.info("startCalculateModel "+startCalculateModel.getBalance().getEquity() + "")	;
		LOG.info("startCalculateEquity  " +startCalculateEquity );
		double oldEquity = startCalculateModel != null ? startCalculateModel.getBalance().getEquity() : startCalculateEquity;
		double newEquity = cal.getBalance().getEquity();
		double profit	 = oldEquity == 0 ? 0 : newEquity - oldEquity;
		double per		 = profit == 0 ? 0 : profit / (oldEquity/100);
	
		BybitAlarmDao.getInstace().insertMessage(user.getId(), user.getUser_id(), getSymbol(),
				oldEquity,newEquity,profit,per,message_count++,cal.toAlarmString());
	}
	
	public boolean checkClearProfit(double price, Balance balance,Position buy, Position sell)throws Exception {
		if(!clear_profit) return false;
		
		double oldEquity = startCalculateModel != null ? startCalculateModel.getBalance().getEquity() : 0.0;
		double newEquity = balance.getEquity();
		double profit	 = oldEquity == 0 ? 0 : newEquity - oldEquity;
		double per		 = profit == 0 ? 0 : profit / (oldEquity/100);
	
		if(per > take_2) {								//all take profit 매시간 검사
			if(clearProfit(price, balance, buy, sell, oldEquity,newEquity,profit, per)) return true;
		}else if(per > take_1) { 						//half take profit message_count/4 2시간 검사
			//if(message_count % 2 == 0)  {
				if(clearProfit(price, balance, buy, sell,oldEquity,newEquity, profit, per)) return true;
			//}
		}else if(per < loss_1) {
			if(clearProfit(price, balance, buy, sell,oldEquity,newEquity, profit, per)) return true;
		}
		return false;
	}
	public boolean clearProfit(double price,Balance balance,Position buy, Position sell ,
			double oldEquity, double newEquity, double profit, double per)throws Exception {
			int min_open 			= calculator.MIN_OPEN_SIZE;
			int long_size 			= (int) (buy.getSize()/QTY);
			int short_size 			= (int) (sell.getSize()/QTY);
			
			if(min_open > long_size && min_open > short_size) return false;
			
			int short_close_size	= 0;
			int long_close_size		= 0;
			if(per > take_2) {
				short_close_size =  (short_size - min_open);
				long_close_size  =  (long_size - min_open);
			}else {
				short_close_size =  Math.round((short_size/100) * 30);
				long_close_size  =  Math.round((long_size/100) * 30);
			}
			
			
			double default_price 	= getDefaultPrice(price);
			
			double close_long_size		= long_close_size * QTY;
			double close_short_size  	= short_close_size * QTY;
			
			StringBuffer msg = new StringBuffer();
			msg.append(per > 0 ? "Take Profit : " : "Stop Loss : ");
			
			msg.append(" "+profit+"( "+ per+"% ) , Count : " + message_count+"\n");
			msg.append("LONG :  " + buy.getEntry_price() + " , " + buy.getSize() + "\n");
			msg.append("SHORT :  " + sell.getEntry_price() + " , " + sell.getSize() + "\n");
			
			double long_trigger		= default_price-50;
			double short_trigger	= default_price+50;
			
			if(close_long_size > 0) closeLong(long_trigger, OVER, long_trigger, close_long_size,ONCE);
			if(close_short_size > 0) closeShort(short_trigger, UNDER, short_trigger, close_short_size, ONCE);
			
			msg.append("closeLong("+long_trigger+", OVER, "+long_trigger+", "+close_long_size+",ONCE)\n");
			msg.append("closeShort("+short_trigger+", UNDER, "+short_trigger+", "+close_short_size+",ONCE)\n");
			
			startCalculateModel = calculator;
			reset_check_time = 1;
			message_count = 0;
			BybitAlarmDao.getInstace().insertMessage(user.getId(), user.getUser_id(), getSymbol(),
				oldEquity,newEquity,profit,per,message_count++,msg.toString());
			
			startCalculateModel.getBalance().setEquity(newEquity);
			return true;
			}
	public double getDefaultPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		double d = Math.round((c_price -m -h)/ 10) * 10;
		return m + h + d;
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
		AlarmPrice openAlarm = new BybitAlarmPrice(this, close_price, OVER, is_repeat); //trigger wait order
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
		else LOG.info("데이터베이스 사용  합니다.");
		//clearAlarmDatabase();
		//registerAlarmDatabase();
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