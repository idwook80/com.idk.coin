package com.idk.coin.bybit;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmPriceManager {
	public static Logger LOG 	= LoggerFactory.getLogger(AlarmPriceManager.class.getName());
	
	public static double over_price 	= 999999.0;
	public static double under_price 	= 0.5;
	public static double current_price	= 20000.0;
	
	ArrayList<AlarmPrice> idles = new ArrayList();
	ArrayList<AlarmPrice> list 	= new ArrayList();
	
	
	public AlarmPriceManager() {}
	public void addAlarm(AlarmPrice alarm) {
		synchronized(list) {
			list.add(alarm);
			alarm.setManager(this);
			LOG.info("ADD Alarm :  " + alarm.toString());
		}
	}
	public void checkAlarm(double price) {
		current_price = price;
		checkIdle();
		
		if(list.isEmpty()) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				if(alarm.compare(price)) {
					list.remove(alarm);
					alarm.action();
					LOG.info("★★★★★★★★★★\t" + list.size() + "\t★★★★★★★★★★★★★");
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
	
	public void setIdleOverPrice(double price) {
		this.over_price = price;
	}
	public void setIdleUnderPrice(double price) {
		this.under_price = price;
	}
	public void addIldeAlarm(AlarmPrice alarm) {
		synchronized (idles) {
			idles.add(alarm);
			LOG.info("ADD Idle Alarm : " + alarm.toString());
		}
	}
	public void checkIdle() {
		if(idles.isEmpty()) return;
		if( over_price > current_price && current_price > under_price) return;
		synchronized (idles) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				addAlarm(alarm);
				idles.remove(alarm);
			}
		}
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
	
}
