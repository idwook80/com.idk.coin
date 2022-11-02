package com.idk.coin.bybit.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;

abstract public class AlarmManager implements Runnable ,PriceListener{
	public static Logger LOG 	= LoggerFactory.getLogger(AlarmManager.class.getName());

	public static double over_price 	= 999999.0;
	public static double under_price 	= 0.001;
	public static double current_price	= -1;
	
	public static boolean OVER  		= true;
	public static boolean UNDER 		= false;
	public static boolean REVERSE		= true;
	public static boolean REPEAT		= REVERSE;
	public static boolean RR			= REVERSE; //reverse and repeat
	public static boolean ONCE			= false;
	
	public BigDecimal DEFAULT_QTY= new BigDecimal("0.001");
	public double  QTY	  		= DEFAULT_QTY.doubleValue();
	public double  QTY2			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public double  QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public double  QTY4			= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
	public double  QTY5			= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
	public double  LOSS_TRIGGER_QTY	= 0.001;
	public double  MIN_PROFIT		= 50;
	public boolean is_run = false;
	public Thread thread;
	
	
	public String symbol				= "";
	public String web_id				= "";
	public BybitUser user;
	
	ArrayList<AlarmPrice> list 	= new ArrayList();
	public AlarmManager(String symbol, BybitUser user) throws Exception{
		list 	= new ArrayList();
		this.symbol = symbol;
		this.user = user;
		if(user != null) this.web_id  = user.getId();
		userSet();
		alarmSet();
		
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
			LOG.info("ADD Alarm :  " + alarm.toString());
		}
	}
	public void checkAlarmExecution(OrderExecution execution) {
		current_price = execution.getPrice();
		LOG.info("★★★★★★★★★★\t Execution Checked " + list.size() + "\t★★★★★★★★★★★★★");
		
		if(list.isEmpty() || !is_run) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() != null) {
					if(execution.getOrder_id().equals(alarm.getParent_order_id())){
						list.remove(alarm);
						try {
							alarm.action(user);
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		checkAlarmPrice(execution.getPrice());
	}
	public void checkAlarmPrice(double price) {
		current_price = price;
		if(list.isEmpty() || !is_run) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() == null) {
					if(alarm.compare(price)) {
						LOG.info("found : "+ alarm + " user : " + user);
						list.remove(alarm);
						try {
							alarm.action(user);
						}catch(Exception e) {
							e.printStackTrace();
						}
						LOG.info("★★★★★★★★★★\t Price Checked " + list.size() + "\t★★★★★★★★★★★★★");
					}
				}
			}
		}
	}
	public void clearAllAlarms() {
		synchronized (list) {
			list.removeAll(list);
		}
		LOG.info("Clear All Alarms : " + list.size());
	}
	public int getSize() {
		return list.size();
	}
	
	
	
	public void printListString() {
		LOG.info("★★★★★★★★★★\t" + list.size() + "\t★★★★★★★★★★★★★");
		int i=0; 
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				LOG.info("["+String.format("%03d",i++)+"]" +  alarm.toString());
			}
		}
		LOG.info("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
	}
	
	
	
	
	
	/* ###################    OPEN LONG   ##################### */
	public AlarmPrice addOpenLong(double price, boolean is_over, double open_price, double qty) throws Exception {
		return addOpenLong(price, is_over, open_price, qty, ONCE);
	}
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty, boolean is_reverse)throws Exception {
		AlarmPrice a = new AlarmPrice(this, trigger, is_over, is_reverse);
		a.setOpenLongAction(price, qty);
		if(is_reverse && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < open_price [Long] for reverse"); 
			//addOpenLong(18910, OVER, 18930, QTY, RR); //18920 무한
		}
		addAlarm(a);
		return a;
	}
	public void createOpenLong(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) throws Exception {
		addOpenLong(trigger, is_over, price, qty);
		addCloseLong(price, UNDER, close_price, qty, is_reverse);
		//makeLongOpen(trigger, is_over, price, qty, close_price, is_reverse);
	}
	
	public void makeOpenLong(double trigger, boolean is_over, double open_price, double qty, double close_price, boolean is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenLong(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new AlarmPrice(this, open_price, UNDER, is_repeat);
		closeAlarm.setCloseLongAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
	}
	
	
	
	/* ###################    CLOSE LONG   ##################### */
	public AlarmPrice addCloseLong(double price, boolean is_over, double open_price, double qty) throws Exception{
		return addCloseLong(price, is_over, open_price, qty, ONCE);
	}
	public AlarmPrice addCloseLong(double trigger,boolean is_over,double price,double qty,boolean is_reverse)throws Exception {
		AlarmPrice a = new AlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseLongAction(price, qty);
		if(is_reverse && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > close_price [Long] for reverse"); 
			//ex addCloseLong(18930, UNDER, 18910, QTY, RR); 18920 무한
		}
		addAlarm(a);
		return a;
	}
	public void createCloseLong(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) throws Exception{
		addCloseLong(trigger, is_over, price, qty, ONCE);
		addOpenLong(price, OVER, open_price, qty, is_reverse);
		//makeLongClose(trigger, is_over, price, qty, open_price, is_reverse);
}
	public void makeCloseLong(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseLong(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new AlarmPrice(this, close_price, OVER, is_repeat); //trigger 
		openAlarm.setOpenLongAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
	}
	
	
	/* ###################    OPEN SHORT   ##################### */
	public AlarmPrice addOpenShort(double price,boolean is_over,double open_price, double qty) throws Exception {
		return addOpenShort(price, is_over, open_price, qty, ONCE);
	}
	public AlarmPrice addOpenShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse)throws Exception {
		AlarmPrice a = new AlarmPrice(this, trigger, is_over,is_reverse);
		a.setOpenShortAction(price, qty);
		if(is_reverse && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > open_price [Short] for reverse");
			//addOpenShort(18930, UNDER, 18910, QTY, RR); //18920 무한 triger
		}
		addAlarm(a);
		return a;
	}
	public void createOpenShort(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) throws Exception {
		addOpenShort(trigger, is_over, price, qty, ONCE);
		addCloseShort(price, OVER, close_price, qty, is_reverse);
	}
	public void makeOpenShort(double trigger, boolean is_over, double open_price, double qty, double close_price,boolean is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenShort(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new AlarmPrice(this, open_price, OVER, is_repeat);
		closeAlarm.setCloseShortAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
	}
 
	
	
	/* ###################    CLOSE SHORT   ##################### */
	public AlarmPrice addCloseShort(double price, boolean is_over, double open_price, double qty) throws Exception {
		 return addCloseShort(price, is_over, open_price, qty, ONCE);
	}
	public AlarmPrice addCloseShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse) throws Exception {
		AlarmPrice a = new AlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseShortAction(price, qty);
		if(is_reverse && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < close_price [Short] for reverse");
			 //ex) addCloseShort(18910, OVER, 18930, QTY, RR); // 18920 무한 
		}
		addAlarm(a);
		return a;
	}
	public void createCloseShort(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) throws Exception {
		addCloseShort(trigger, is_over, price, qty, ONCE);
		addOpenShort(price, UNDER, open_price, qty, is_reverse);
	}
	public void makeCloseShort(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseShort(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new AlarmPrice(this, close_price, UNDER, is_repeat);
		openAlarm.setOpenShortAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
	}
	
	
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
	
	
	
}