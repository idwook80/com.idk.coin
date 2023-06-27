package com.idk.coin.bybit.alram;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.BybitAlarmsModel;

public class AlarmManager01 extends BybitAlarmsModel {
	
	public AlarmManager01(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager01(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty 		= user.getDefault_qty();
		if(default_qty == null) default_qty = "0.001";
		setDefault_qty(Double.valueOf(default_qty));
		LOSS_TRIGGER_QTY			= 0.001;
	    MIN_PROFIT					= 50;
	    
	    IDLE_TIME 		= 10; // 10분마다
	    RESET_TIME 		= 60 * 2;// 1시간마다
	    startCalculateEquity = 0.0;
		max_open_size	= 50;
		over_open_size	= 20;
		min_open_size	= 20;
		
		take_1			= 10;
		take_2 			= 20;
		clear_profit    = DISABLE;
	}
	

	public void alarmSet() throws Exception{
		LOG.info("Alarm Set");
		boolean is_debug  = DEBUG_OFF;
		clearAllAlarms();
		if(is_debug) {
			enableDatabase(DISABLE);
			currentStatus(DEBUG_ON);
		}else {
			cancelAllOrder();
			enableDatabase(ENABLE);
			currentStatus(DEBUG_OFF);
			clearAlarmDatabase();
			registerAlarmDatabase();
			
		}
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
	
/**
 * #################   [SHORT] #############
 * @throws Exception
 */
	public void startMakeOpenShort(double start,double end,double limit, boolean isLoss) throws Exception{
		double profitLimit = 100;
		double alarmLimit  = 150;
		
		for(double i=start; i<=end; i+=limit) {
			double open = i;
			double close  = i - profitLimit;
			double alarm = i-alarmLimit;
			makeOpenShort(alarm, OVER, open, QTY, close, RR);
		}
		if(isLoss) {
			//shortStopLoss(end-profitLimit   , QTY2);
			//shortStopLoss(start+profitLimit , QTY2);
		}
		
	}
	public void createOpenShort()  throws Exception{
		//makeOpenShort(26610, OVER, 26810, QTY, 26610, RR);
		startMakeOpenShort(32110, 32910, 200, true);
		startMakeOpenShort(31110, 31910, 200, true);
		startMakeOpenShort(29110, 29910, 200, true);
		startMakeOpenShort(28110, 28910, 200, true);
		
		startMakeOpenShort(27010, 27910, 100, false);
		
		makeOpenShort(26760, OVER, 26910, QTY, 26810, THIRD);
		makeOpenShort(26660, OVER, 26810, QTY, 26710, THIRD);
		//makeOpenShort(26560, OVER, 26710, QTY, 26610, THIRD);
		//makeOpenShort(25960, OVER, 26110, QTY, 26010, THIRD);
	}
	/** ###########  [Model 01] ########### [SHORT]  **/
	public void setShort() throws Exception{
		
		//closeShort(26810, OVER, 26710, QTY, THIRD);
		closeShort(26710, OVER, 26610, QTY, THIRD);
		closeShort(26610, OVER, 26510, QTY, THIRD);
		//closeShort(26510, OVER, 26410, QTY, THIRD);
		/** ↑↑↑↑ -------  Price Line  26636  -------  Long First ↓↓↓↓  **/
		//openShort(26510, UNDER, 26610, QTY, 6);
		openShort(26410, UNDER, 26510, QTY, 6);
		openShort(25310, UNDER, 25410, QTY, 6);
		openShort(26210, UNDER, 26310, QTY, 6);
		//<-- sync //<--
	}
	/** ###########  [Model 01] ########### **/
	public void createCloseShort()throws Exception{
		
		//makeCloseShort(26560, UNDER, 26410, QTY, 26510, RR);
		//makeCloseShort(26460, UNDER, 26310, QTY, 26410, RR);
		//makeCloseShort(26360, UNDER, 26210, QTY, 26310, RR);
		makeCloseShort(26260, UNDER, 26110, QTY, 26210, RR);
		makeCloseShort(26160, UNDER, 26010, QTY, 26110, RR);
		makeCloseShort(26060, UNDER, 25910, QTY, 26010, RR);
		makeCloseShort(25960, UNDER, 25810, QTY, 25910, RR);
		
		openShort(25710, UNDER, 25860, QTY, RR, 25760);
		
		startOpenShort(25710, 24010, 100);
		
		startOpenShort(24110, 23310, 200);
		startOpenShort(23110, 22310, 200);
		startOpenShort(22110, 21310, 200);
		//makeCloseShort(26210, UNDER, 26010, QTY, 26210, RR);
	}
	public void startOpenShort(double start, double end, double limit) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		//openShort(24110, UNDER, 24310, QTY, RR, 24160);
		
		double profitLimit = 200;
		double alarmLimit  = 150;
		
		for(double i=start; i>=end; i-=limit) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			openShort(close, UNDER, open, QTY, RR, alarm);
		}
	}

	
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */
	public void startOpenLong(double start, double end, double limit) throws Exception{
		//openLong(30010, OVER, 29810, QTY, RR, 29960);
		//openLong(29210, OVER, 29010, QTY, RR, 29160);
		//startOpenLong(29010, 29810, 200);
		double profitLimit = 200;
		double alarmLimit  = 150;
		
		for(double i=start; i<=end; i+=limit) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			openLong(close, OVER, open, QTY, RR, alarm);
		}
	 
		
	}
	public void createCloseLong() throws Exception{
		//makeCloseLong(27810, OVER, 28010, QTY, 27810, RR);
		
		//startOpenLong(32010, 32810, 200);
		//startOpenLong(31010, 31810, 200);
		startOpenLong(30010, 30810, 200);
		startOpenLong(29010, 29810, 200);
		startOpenLong(28010, 28810, 200);
		
		startOpenLong(26910, 27910, 100);
		
		//makeCloseLong(25660, OVER, 25810, QTY, 25610, 8);
		//makeCloseLong(25560, OVER, 25710, QTY, 25510, 8);
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		openLong(26810, OVER, 26610, QTY, 6);//<--
		openLong(26710, OVER, 26510, QTY, 6);//<--
		openLong(26610, OVER, 26410, QTY, 6);
		//openLong(26510, OVER, 26310, QTY, 6);//<--
		/** ↑↑↑↑ -------  Price Line 26256 ------- short  ↓↓↓↓  **/
		closeLong(26310, UNDER, 26510, QTY, THIRD);
		closeLong(26210, UNDER, 26410, QTY, THIRD);
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
		//makeOpenLong(26560, UNDER, 26410, QTY, 26610, THIRD);
		//makeOpenLong(26460, UNDER, 26310, QTY, 26510, THIRD);
		//makeOpenLong(26360, UNDER, 26210, QTY, 26410, THIRD);
		makeOpenLong(26260, UNDER, 26110, QTY, 26310, THIRD);
		makeOpenLong(26160, UNDER, 26010, QTY, 26210, THIRD);
		makeOpenLong(26010, UNDER, 25910, QTY, 26110, THIRD);
		makeOpenLong(25960, UNDER, 25810, QTY, 26010, THIRD);
		makeOpenLong(25860, UNDER, 25710, QTY, 25910, THIRD);
		
		
		startMakeOpenLong(25610, 23010, 100, false);
		
		startMakeOpenLong(22810, 22010, 200, true);
		startMakeOpenLong(21810, 21010, 200, true);
	}
	
	
	
	public void startMakeOpenLong(double start, double end, double limit, boolean isLoss) throws Exception{
		double profitLimit = 100;
		double alarmLimit  = 150;
		
		for(double i=start; i>=end; i-=limit) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i+alarmLimit;
			makeOpenLong(alarm, UNDER, open, QTY, close, RR);
		}
		if(isLoss) {
			//longStopLoss(start-profitLimit  , QTY2);
			//longStopLoss(end+profitLimit 	, QTY2);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}