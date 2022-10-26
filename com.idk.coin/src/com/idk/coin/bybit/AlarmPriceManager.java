package com.idk.coin.bybit;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.model.OrderExecution;

public class AlarmPriceManager {
	public static Logger LOG 	= LoggerFactory.getLogger(AlarmPriceManager.class.getName());
	
	public static double over_price 	= 999999.0;
	public static double under_price 	= 0.5;
	public static double current_price	= 20000.0;
	
	ArrayList<AlarmPrice> list 	= new ArrayList();
	
	
	public AlarmPriceManager() {}
	public void addAlarm(AlarmPrice alarm) {
		synchronized(list) {
			list.add(alarm);
			alarm.setManager(this);
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
						alarm.action();
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
						alarm.action();
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
	
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty, boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setOpenLongAction(price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice addOpenShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setOpenShortAction(price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice addCloseLong(double trigger,boolean is_over,double price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setCloseLongAction(price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice addCloseShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(trigger, is_over,is_reverse);
		a.setCloseShortAction(price, qty);
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
	
}
