package com.idk.coin.bybit;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmPriceManager {
	public static Logger LOG =   LoggerFactory.getLogger(AlarmPriceManager.class.getName());
	ArrayList<AlarmPrice> list = new ArrayList();
	
	
	public AlarmPriceManager() {}
	public void addAlarm(AlarmPrice alarm) {
		synchronized(list) {
			list.add(alarm);
			alarm.setManager(this);
			LOG.info("ADD Alarm :  " + alarm.toString());
		}
	}
	public void checkAlarm(double price) {
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
	public int getSize() {
		return list.size();
	}
	
	
	public AlarmPrice createOpenLong(double price,boolean is_over,double open_price,double qty, boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(price, is_over,is_reverse);
		a.setOpenLongAction(open_price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice createOpenShort(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(price, is_over,is_reverse);
		a.setOpenShortAction(open_price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice createCloseLong(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(price, is_over,is_reverse);
		a.setCloseLongAction(open_price, qty);
		addAlarm(a);
		return a;
	}
	public AlarmPrice createCloseShort(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		AlarmPrice a = new AlarmPrice(price, is_over,is_reverse);
		a.setCloseShortAction(open_price, qty);
		addAlarm(a);
		return a;
	}
	
}
