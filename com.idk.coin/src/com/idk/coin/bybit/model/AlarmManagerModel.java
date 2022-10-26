package com.idk.coin.bybit.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.BybitMain;

abstract public class AlarmManagerModel {
	public static Logger LOG 	= LoggerFactory.getLogger(AlarmManagerModel.class.getName());
	
	public static double over_price 	= 999999.0;
	public static double under_price 	= 0.5;
	public static double current_price	= 20000.0;
	
	public static boolean OVER  		= true;
	public static boolean UNDER 		= false;
	public static boolean REVERSE		= true;
	public static boolean REPEAT		= REVERSE;
	public static boolean RR			= REVERSE; //reverse and repeat
	public static boolean ONCE			= false;
	
	public static BigDecimal DEFAULT_QTY= new BigDecimal("0.15");
	public static double  QTY	  		= DEFAULT_QTY.doubleValue();
	public static double  QTY1			= new BigDecimal("0.2").doubleValue();
	public static double  QTY2			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public static double  QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public static double  QTY4			= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
	public static double  QTY5			= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
	public static double  LOSS_TRIGGER_QTY	= 0.001;
	public static double  MIN_PROFIT		= 50;
	
	public BybitMain main;
	
	String symbol				= "BTCUSDT";
	String user_id				= "idwook80";
	String api_key				= "";
	String api_secret			= "";
	
	ArrayList<AlarmPrice> list 	= new ArrayList();
	
	
	public AlarmManagerModel(BybitMain main) {
		this.main = main;
	}
	
	
	
	public void addAlarm(AlarmPrice alarm) {
		synchronized(list) {
			list.add(alarm);
			//alarm.setManager(this);
			LOG.info("ADD Alarm :  " + alarm.toString());
		}
	}
	public void checkAlarmExecution(OrderExecution execution) {
		current_price = execution.getPrice();
		LOG.info("★★★★★★★★★★\t Execution Checked " + list.size() + "\t★★★★★★★★★★★★★");
		
		if(list.isEmpty()) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() != null) {
					if(execution.getOrder_id().equals(alarm.getParent_order_id())){
						list.remove(alarm);
						alarm.action(api_key, api_secret);
					}
				}
			}
		}
		checkAlarmPrice(execution.getPrice());
	}
	public void checkAlarmPrice(double price) {
		current_price = price;
		if(list.isEmpty()) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() == null) {
					if(alarm.compare(price)) {
						list.remove(alarm);
						alarm.action(api_key, api_secret);
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
	
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty, boolean is_reverse)throws Exception {
		AlarmPrice a = new AlarmPrice(trigger, is_over, is_reverse);
		a.setOpenLongAction(price, qty);
		if(is_reverse && is_over == OVER) {
			if(trigger < price) throw new Exception("[Warning] trigger < open_price [Long] for reverse"); 
			//addOpenLong(18910, OVER, 18930, QTY, RR); //18920 무한
		}
		addAlarm(a);
		return a;
	}
	public AlarmPrice addOpenShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse)throws Exception {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setOpenShortAction(price, qty);
		if(is_reverse && is_over == UNDER) {
			if(trigger > price) throw new Exception("[Warning] trigger > open_price [Short] for reverse");
			//addOpenShort(18930, UNDER, 18910, QTY, RR); //18920 무한 triger
		}
		addAlarm(a);
		return a;
	}
	public AlarmPrice addCloseLong(double trigger,boolean is_over,double price,double qty,boolean is_reverse)throws Exception {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setCloseLongAction(price, qty);
		if(is_reverse && is_over == UNDER) {
			if(trigger > price) throw new Exception("[Warning] trigger > close_price [Long] for reverse"); 
			//ex addCloseLong(18930, UNDER, 18910, QTY, RR); 18920 무한
		}
		addAlarm(a);
		return a;
	}
	public AlarmPrice addCloseShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse) throws Exception {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setCloseShortAction(price, qty);
		if(is_reverse && is_over == OVER) {
			if(trigger < price) throw new Exception("[Warning] trigger < close_price [Short] for reverse");
			 //ex) addCloseShort(18910, OVER, 18930, QTY, RR); // 18920 무한 
		}
		addAlarm(a);
		return a;
	}
	
	
	public AlarmPrice createOpenLong(double trigger, boolean is_over, double price, double qty, boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setOpenLongAction(price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice createOpenShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setOpenShortAction(price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice createCloseLong(double trigger,boolean is_over,double price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setCloseLongAction(price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice createCloseShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setCloseShortAction(price, qty);
		addAlarm(a);
		return a;
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
	public void createOpenLong(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) throws Exception {
		addOpenLong(trigger, is_over, price, qty);
		addCloseLong(price, UNDER, close_price, qty, is_reverse);
	}
	
	public AlarmPrice addOpenLong(double price, boolean is_over, double open_price, double qty) throws Exception {
		return addOpenLong(price, is_over, open_price, qty, ONCE);
	}
	
	/*public AlarmPrice addOpenLong(double trigger, boolean is_over, double open_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == OVER) {
			if(trigger < open_price) throw new Exception("[Warning] trigger < open_price [Long] for reverse"); 
			//addOpenLong(18910, OVER, 18930, QTY, RR); //18920 무한
		}
		return createOpenLong(trigger, is_over, open_price, qty, is_reverse);
		//return main.getAlarmPriceManager().createOpenLong(trigger, is_over, open_price, qty,is_reverse);
	}*/
	
	/* ###################    CLOSE LONG   ##################### */
	public void createCloseLong(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) throws Exception{
			addCloseLong(trigger, is_over, price, qty, ONCE);
			addOpenLong(price, OVER, open_price, qty, is_reverse);
	}
	
	public AlarmPrice addCloseLong(double price, boolean is_over, double open_price, double qty) throws Exception{
		return addCloseLong(price, is_over, open_price, qty, ONCE);
	}
	
/*	public AlarmPrice addCloseLong(double trigger, boolean is_over, double close_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == UNDER) {
			if(trigger > close_price) throw new Exception("[Warning] trigger > close_price [Long] for reverse"); 
			//ex addCloseLong(18930, UNDER, 18910, QTY, RR); 18920 무한
		}
		return createCloseLong(trigger, is_over, close_price, qty,is_reverse);
		//return main.getAlarmPriceManager().createCloseLong(trigger, is_over, close_price, qty,is_reverse);
	}*/
	/* ###################    OPEN SHORT   ##################### */
	public void createOpenShort(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) throws Exception {
			addOpenShort(trigger, is_over, price, qty, ONCE);
			addCloseShort(price, OVER, close_price, qty, is_reverse);
	}
	public AlarmPrice addOpenShort(double price,boolean is_over,double open_price, double qty) throws Exception {
		return addOpenShort(price, is_over, open_price, qty, ONCE);
	}
	
	
	/*public AlarmPrice addOpenShort(double trigger, boolean is_over, double open_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == UNDER) {
			if(trigger > open_price) throw new Exception("[Warning] trigger > open_price [Short] for reverse");
			//addOpenShort(18930, UNDER, 18910, QTY, RR); //18920 무한 triger
		}
		return createOpenShort(trigger, is_over, open_price, qty, is_reverse);
		//return main.getAlarmPriceManager().createOpenShort(trigger, is_over, open_price, qty, is_reverse);
	 
	}*/
	
	
	/* ###################    CLOSE SHORT   ##################### */
	
	public void createCloseShort(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) throws Exception {
		addCloseShort(trigger, is_over, price, qty, ONCE);
		addOpenShort(price, UNDER, open_price, qty, is_reverse);
	}
	
	public AlarmPrice addCloseShort(double price, boolean is_over, double open_price, double qty) throws Exception {
	 return addCloseShort(price, is_over, open_price, qty, ONCE);
	}
	
	/*public AlarmPrice addCloseShort(double trigger, boolean is_over, double close_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == OVER) {
			if(trigger < close_price) throw new Exception("[Warning] trigger < close_price [Short] for reverse");
			 //ex) addCloseShort(18910, OVER, 18930, QTY, RR); // 18920 무한 
		}
		return createCloseShort(trigger, is_over, close_price, qty,is_reverse);
		//return main.getAlarmPriceManager().createCloseShort(trigger, is_over, close_price, qty,is_reverse);
		
	}*/
	public void makeShortOpen(double trigger, boolean is_over, double open_price, double qty, double close_price,boolean is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenShort(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new AlarmPrice(open_price, OVER, is_repeat);
		closeAlarm.setCloseShortAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
		if(is_repeat && open_price < close_price) throw new Exception(" -------[Short] open > close ---------\n " + openAlarm.toString());
	}
	public void makeShortClose(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseShort(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new AlarmPrice(close_price, UNDER, is_repeat);
		openAlarm.setOpenShortAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
		if(is_repeat && open_price < close_price) throw new Exception(" -------[Short] open > close ---------\n " + closeAlarm.toString());
	}
	public void makeLongOpen(double trigger, boolean is_over, double open_price, double qty, double close_price, boolean is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenLong(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new AlarmPrice(open_price, UNDER, is_repeat);
		closeAlarm.setCloseLongAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
		if(is_repeat && open_price > close_price) throw new Exception(" ------- [LONG] open < close ---------\n " + openAlarm.toString());
	}
	public void makeLongClose(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) throws Exception {
			AlarmPrice closeAlarm = addCloseLong(trigger, is_over, close_price, qty, ONCE);
			AlarmPrice openAlarm = new AlarmPrice(close_price, OVER, is_repeat); //trigger 
			openAlarm.setOpenLongAction(open_price, qty);
			closeAlarm.setNextAlarm(openAlarm);
				if(is_repeat && open_price > close_price) throw new Exception(" -------[Long] open < close ---------\n " + closeAlarm.toString());
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
