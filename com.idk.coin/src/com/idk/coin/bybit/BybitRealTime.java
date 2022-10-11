package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.model.OrderExecution;

public class BybitRealTime {
	public static Logger LOG =   LoggerFactory.getLogger(BybitRealTime.class.getName());
	BybitMain main;
	BybitExecution execution;
	
	public BybitRealTime(BybitMain main) {
		this.main = main;
		init();
	}
	public void init() {
		execution = new BybitExecution(this);
	}
	
	public void eventExecution(OrderExecution e) {
		if(e.getLeaves_qty() == 0) {
			main.getAlarmPriceManager().checkAlarmExecution(e);
			main.getPositionManager().changedPositions();
		}
	}
}
