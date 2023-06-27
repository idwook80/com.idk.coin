package com.idk.coin.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.account.OrderRest_V3;
import com.idk.coin.bybit.alram.CalculatePositionV3;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.OrderExecution;
import com.idk.coin.bybit.model.PriceListener;

abstract public class AlarmManager implements Runnable ,PriceListener{
	public static Logger LOG 			= LoggerFactory.getLogger(AlarmManager.class.getName());

	public static double max_price 		= 999999.0;
	public static double min_price 		= 0.001;
	public static double current_price	= -1;
	
	public static boolean OVER  		= true;
	public static boolean UNDER 		= false;
	public static int REVERSE			= 99;
	public static int REPEAT			= 99;
	public static int RR				= 99; //reverse and repeat
	public static int ONCE				= 1;
	public static int TWICE				= 3;
	public static int THIRD				= 5;
	
	public BigDecimal DEFAULT_QTY		= new BigDecimal("0.001");
	public double  QTY	  				= DEFAULT_QTY.doubleValue();
	public double  QTY2					= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public double  QTY3					= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public double  QTY4					= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
	public double  QTY5					= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
	public double  QTY6					= DEFAULT_QTY.multiply(new BigDecimal("6")).doubleValue();
	public double  QTY7					= DEFAULT_QTY.multiply(new BigDecimal("7")).doubleValue();
	public double  QTY8					= DEFAULT_QTY.multiply(new BigDecimal("8")).doubleValue();
	public double  QTY9					= DEFAULT_QTY.multiply(new BigDecimal("9")).doubleValue();
	public double  QTY10				= DEFAULT_QTY.multiply(new BigDecimal("10")).doubleValue();
	
	public double  LOSS_TRIGGER_QTY		= 0.001;
	public double  MIN_PROFIT			= 50;
	public boolean is_run 				= false;
	public Thread thread;
	
	
	public String symbol				= "";
	public String web_id				= "";
	public BybitUser user;
	public boolean db_enable					= false;
	
	public static int IDLE_TIME 		= 10;      		
	public int idle_check_time 			= 0; 			//min
	public static int RESET_TIME 		= 60 * 1; 		//reset 4시간마다
	public int reset_check_time 		= RESET_TIME; 	//min
	public CalculatePositionV3 startCalculateModel = null;
	public CalculatePositionV3 calculator 			= null;
	public double startCalculateEquity = 0.0;
	public double take_1		= 2.5;
	public double take_2 		= 5;
	public int max_open_size	= 20;
	public int over_open_size	= 5;
	public int min_open_size	= 5;
	public boolean clear_profit = false;
	
	public ArrayList<AlarmPrice> list;
	public ArrayList<AlarmPrice> idles;
	public AlarmManager(String symbol, BybitUser user) throws Exception{
		list 		= new ArrayList();
		idles		= new ArrayList();
		this.symbol = symbol;
		this.user 	= user;
		if(user != null) this.web_id  = user.getId();
		userSet();
		//alarmSet();
		
	}
	public AlarmManager(String symbol, String web_id,String web_pw) throws Exception{
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
	abstract public void userSet() throws Exception;
	abstract public void alarmSet() throws Exception;
	
	public String getSymbol() {
		return symbol;
	}
	public void startAlarmManager() {
		LOG.info(user.getId() +" , "+ symbol + " Alarm System Start!");
		is_run = true;
		thread = new Thread(this);
		thread.start();
		
	}
	public void stopAlarmManager() {
		is_run = false;
		if(thread != null) {
			thread.interrupt();
			thread = null;
		}
		LOG.info(user.getId() +" , "+ symbol + " Alarm System Stop!");
		
	}
	
	
	public void setDefault_qty(double qty) {
	    QTY				= new BigDecimal(String.valueOf(qty)).doubleValue();
	    BigDecimal temp = new BigDecimal(String.valueOf(qty));
		QTY2  			= temp.multiply(new BigDecimal("2")).doubleValue();
		QTY3			= temp.multiply(new BigDecimal("3")).doubleValue();
		QTY4			= temp.multiply(new BigDecimal("4")).doubleValue();
		QTY5			= temp.multiply(new BigDecimal("5")).doubleValue();
	}
	
	public void addAlarm(AlarmPrice alarm) {
		synchronized(list) {
			list.add(alarm);
			LOG.info("알람추가  " + alarm.toString());
		}
	}
	public void checkAlarmExecution(OrderExecution execution) {
		if(!symbol.equals(execution.getSymbol()))  return;
		double price 		= execution.getPrice();
		String side 		= execution.getSide();
		String side_name 	= side;
		double qty			= execution.getOrder_qty();
		double exec_qty  	= execution.getExec_qty();
		double leave_qty    = execution.getLeaves_qty();
		

		if(side.equals("Sell")){
			side_name = " [Open Short] 또는 [Close Long]";
		}else {
			side_name = " [Close Short] 또는 [Open Long]";
		}
		LOG.info("#############["+getUser().getId()+"]##############[주문체결]######################["+getSymbol()+"]#################");
		LOG.info("주문체결 : ["+price+"],("+exec_qty+"/"+qty+")주(" +leave_qty+") ["+ side + "]("+side_name+") 주문이 체결되었습니다.(" + execution.getOrder_id()+ ")\t##");
		LOG.info("#############["+getUser().getId()+"]##############[주문체결]######################["+getSymbol()+"]#################");
		if(list.isEmpty() || !is_run) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() != null) {
					if(execution.getOrder_id().equals(alarm.getParent_order_id())){
						LOG.info("");
						LOG.info("##############################[알람처리 시작]##################################");
						LOG.info("알람확인 : 확인된 [체결알람]이있습니다. " + alarm.toAlarm() + "\t"+alarm.getParent_order_id());
						//LOG.info("알람 : "+ alarm + " 사용자 : " + user);
						//current_price 	= execution.getPrice();
						//String side 	= execution.getSide();
						//double qty  	= execution.getExec_qty();
						
						list.remove(alarm);
						try {
							alarm.action(user, symbol);
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}
		if(symbol.equals(execution.getSymbol())) { 
			checkAlarmPrice(price);
			//checkAlarmIdles();
		}
		
	}
	public void checkAlarmPrice(double price) {
		setCurrentPrice(price);
		if(list.isEmpty() || !is_run) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() == null) {
					if(alarm.compare(price)) {
						LOG.info("");
						LOG.info("##############################[알람처리 시작]##################################");
						LOG.info("알람확인: "+price +" 확인된 [가격알람]이있습니다. " + alarm.toAlarm());
						//LOG.info("알람 확인 : "+ alarm + " 사용자 : " + user);
						list.remove(alarm);
						try {
							alarm.action(user, symbol);
						}catch(Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}
			
		}
	}
	public synchronized void setCurrentPrice(double price) {
		this.current_price = price;
	}
	public double getCurrentPrice() {
		return this.current_price;
	}
	public void cancelAllOrder() {
		LOG.info("Cancel All Orders ");
		try {
			OrderRest_V3.cancelAllOrder(user.getApi_key(),user.getApi_secret(),symbol);
			//OrderRest.cancelAllOrder(user.getApi_key(),user.getApi_secret(),symbol);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void clearAllAlarms() {
		synchronized (list) {
			list.removeAll(list);
		}
		LOG.info("Clear Actives Alarms : " + list.size());
		synchronized (idles) {
			idles.remove(idles);
		}
		LOG.info("Clear Idles Alarms : " + idles.size());
	}
	public int getSize() {
		return list.size() + idles.size();
	}
	
	
	
	public String printListString() {
		StringBuffer message = new StringBuffer();
		
		
		message.append("★★★["+user.getUser_id()+"]★[Actives]★\t" + list.size() + "\t★★★["+getSymbol()+"]★★★★★\n");
		int i=1; 
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				message.append("["+String.format("%03d",i++)+"]" +  alarm.toString()+"\n");
				//LOG.info("["+alarm.getAlarm_id()+"]" +  alarm.toString());
			}
		}
		message.append("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★"+"\n");
		message.append("★★★["+user.getUser_id()+"]★[Idles]★\t" + idles.size() + "\t★★★["+getSymbol()+"]★★★★★"+"\n");
		 i=1; 
		synchronized(list) {
			AlarmPrice[] obj = idles.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				message.append("["+String.format("%03d",i++)+"]" +  alarm.toString()+"\n");
				//LOG.info("["+alarm.getAlarm_id()+"]" +  alarm.toString());
			}
		}
		message.append("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★"+"\n");
		
		return message.toString();
	}
	
	
	
	
	
	/* ###################    OPEN LONG   ##################### */
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty) throws Exception {
		return addOpenLong(trigger, is_over, price, qty, ONCE);
	}
	public AlarmPrice createOpenLong(double trigger, boolean is_over, double price, double qty, double close_price, int is_reverse) throws Exception {
		return makeOpenLong(trigger, is_over, price, qty, close_price, is_reverse);
	}
	
	abstract public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty, int is_reverse)throws Exception;
	abstract public AlarmPrice makeOpenLong(double trigger, boolean is_over, double open_price, double qty, double close_price, int is_reverse) throws Exception;
 
	
	
	/* ###################    CLOSE LONG   ##################### */
	public AlarmPrice addCloseLong(double price, boolean is_over, double open_price, double qty) throws Exception{
		return addCloseLong(price, is_over, open_price, qty, ONCE);
	}
	abstract public AlarmPrice addCloseLong(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception; 
	public AlarmPrice createCloseLong(double trigger, boolean is_over, double price, double qty, double open_price, int is_reverse) throws Exception{
		return makeCloseLong(trigger, is_over, price, qty, open_price, is_reverse);
	}
	abstract public AlarmPrice makeCloseLong(double trigger, boolean is_over, double close_price, double qty, double open_price, int is_reverse) throws Exception;
	 
	
	
	/* ###################    OPEN SHORT   ##################### */
	public AlarmPrice addOpenShort(double price,boolean is_over,double open_price, double qty) throws Exception {
		return addOpenShort(price, is_over, open_price, qty, ONCE);
	}
	abstract public AlarmPrice addOpenShort(double trigger,boolean is_over,double price,double qty,int is_reverse)throws Exception;
	public AlarmPrice createOpenShort(double trigger, boolean is_over, double price, double qty, double close_price, int is_reverse) throws Exception {
		return makeOpenShort(trigger, is_over, price, qty, close_price, is_reverse);
	}
	abstract public AlarmPrice makeOpenShort(double trigger, boolean is_over, double open_price, double qty, double close_price,int is_reverse) throws Exception;
	 
 
	
	
	/* ###################    CLOSE SHORT   ##################### */
	public AlarmPrice addCloseShort(double price, boolean is_over, double open_price, double qty) throws Exception {
		 return addCloseShort(price, is_over, open_price, qty, ONCE);
	}
	abstract public AlarmPrice addCloseShort(double trigger,boolean is_over,double price,double qty,int is_reverse) throws Exception;
	
	/** 1. CloseShort  -> Open Short  **/
	public AlarmPrice createCloseShort(double trigger, boolean is_over, double price, double qty, double open_price, int is_reverse) throws Exception {
		return makeCloseShort(trigger, is_over, price, qty, open_price, is_reverse);
	}
	abstract public AlarmPrice makeCloseShort(double trigger, boolean is_over, double close_price, double qty, double open_price, int is_reverse) throws Exception;
	
	
	/** Only Long Close **/
	public void longStopLoss(double trigger, boolean is_over, double close_price, double qty) throws Exception{
		addCloseLong(trigger, is_over, close_price, qty);
		
	}
	/** Long Close And (Long & Short) Open Package  **/
	public void longStopLoss(double trigger, double qty) throws Exception {
		addOpenLong(trigger + (MIN_PROFIT*2), UNDER, trigger, LOSS_TRIGGER_QTY);
		addCloseLong(trigger, UNDER, trigger+5, qty);
		
		addOpenLong(trigger, UNDER, trigger-MIN_PROFIT, QTY);
		addOpenShort(trigger,UNDER, trigger+MIN_PROFIT, QTY);
		
	}
	/** Only Short Close **/
	public void shortStopLoss(double trigger, boolean is_over, double close_price, double qty)  throws Exception {
		addCloseShort(trigger, OVER, close_price, qty);
	}
	/** Short Close And (Long & Short) Open Package **/
	public void shortStopLoss(double trigger, double qty)  throws Exception {
		addOpenShort(trigger - (MIN_PROFIT*2), OVER, trigger, LOSS_TRIGGER_QTY);
		addCloseShort(trigger, OVER, trigger+5, qty);
		
		addOpenShort(trigger, OVER, trigger+MIN_PROFIT, QTY);
		addOpenLong(trigger, OVER, trigger-MIN_PROFIT, QTY);
	}

	public void enableDatabase(boolean enable) {
		db_enable = enable;
	}
	public abstract void checkAlarmIdles(int percent);
	public abstract void loadAlarmDatabase() ;
	public abstract void registerAlarmDatabase();
	 
	public abstract void clearAlarmDatabase() ;
	public abstract void deleteDatabase(AlarmPrice alarm);  
	public abstract int insertDatabase(AlarmPrice alarm);
	public abstract void updateDatabase(AlarmPrice alarm);	 
	 
	public String toString() {
		return user.getId();
	}
}