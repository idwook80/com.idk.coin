package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BybitRealTime {
	public static Logger LOG =   LoggerFactory.getLogger(BybitRealTime.class.getName());
	AlarmPriceManager alarmPriceManager;
	BybitExecution execution;
	
	public BybitRealTime(AlarmPriceManager alarmPriceManager) {
		this.alarmPriceManager = alarmPriceManager;
		init();
	}
	public void init() {
		execution = new BybitExecution(alarmPriceManager);
	}
}
