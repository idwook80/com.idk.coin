package com.idk.coin.bybit.alram;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class AlarmManager03 extends BybitAlarmsModel {
	
	public AlarmManager03(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager03(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty 		= user.getDefault_qty();
		if(default_qty == null) default_qty = "0.01";
		setDefault_qty(Double.valueOf(default_qty));
		LOSS_TRIGGER_QTY			= 0.01;
	    MIN_PROFIT					= 50;
	    
	    IDLE_TIME 		= 9; // 10분마다
	    RESET_TIME 		= 60;// 1시간마다
	    startCalculateEquity = 400.0;
	    message_count		 = 15;
	    
	    //max_open_size	= 50;
		//over_open_size	= 20;
		//min_open_size	= 20;
		
		take_1			= 1;
		take_2 			= 3;
		loss_1			= -5;
		///clear_profit    = ENABLE;
	}
	

	public void alarmSet() throws Exception{
		LOG.info("Alarm Set");
		boolean is_debug  = DEBUG_OFF;
		clearAllAlarms();
		if(is_debug) {
			enableDatabase(DISABLE);
			currentStatus(DEBUG_ON);
			clear_profit    = DISABLE;
		}else {
			cancelAllOrder();
			enableDatabase(ENABLE);
			currentStatus(DEBUG_OFF);
			clear_profit    = ENABLE;
			clearAlarmDatabase();
			registerAlarmDatabase();
			
		}
	}
	public CalculateModel createCalculateModel(BybitAlarmsModel parent,double price,Position buy,
	 		Position sell, Balance balance,double qty, boolean debug) {
		CalculateModel model =  new CalculatePositionV3(parent, price, buy, sell, balance, qty, debug);
		model.setSizeValue(20, 5, 3);
		return model;
	}
	
	public void run() {
		try {
			Thread.sleep(1000*3);
			alarmSet();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		while(is_run) {
			try {
				checkBalance();
				Thread.sleep(1000* 60 * 1);
				if(reset_check_time-- < 0) {
					alarmSet();
					idle_check_time = -1;
				}
				if(idle_check_time-- < 0) {
					checkAlarmIdles(5);
					idle_check_time = IDLE_TIME;
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
