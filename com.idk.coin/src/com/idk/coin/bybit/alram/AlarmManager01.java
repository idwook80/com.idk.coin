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
		String default_qty = user.getDefault_qty();
		if(default_qty == null) default_qty = "0.001";
		setDefault_qty(Double.valueOf(default_qty));
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
	  
	}
	public void run() {
		while(is_run) {
			try {
				LOG.info(this.getSize() + "  : " + this.getClass().getName());
				this.printListString();
				
				Thread.sleep(1000* 60 * 10);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	public void alarmSet() throws Exception{
		createOpenShort();
		setShort();
		createCloseShort();
		
		createCloseLong();
		setLong();
		createOpenLong();
		enableDatabase();
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
		
		startMakeOpenShort(25810, 27910, 100, false);
		
		makeOpenShort(25560, OVER, 25710, QTY, 25510, THIRD);
		makeOpenShort(25460, OVER, 25610, QTY, 25410, THIRD);
		makeOpenShort(25360, OVER, 25510, QTY, 25310, THIRD);
		makeOpenShort(25260, OVER, 25410, QTY, 25210, THIRD);
		//makeOpenShort(25960, OVER, 26110, QTY, 26010, THIRD);
	}
	/** ###########  [Model 01] ########### [SHORT]  **/
	public void setShort() throws Exception{
		
		closeShort(25310, OVER, 25110, QTY, THIRD);//<--
		closeShort(25210, OVER, 25010, QTY, THIRD);//<--
		/** ↑↑↑↑ -------  Price Line  26636  -------  Long First ↓↓↓↓  **/
		//openShort(25810, UNDER, 26010, QTY, 6);
		//openShort(25710, UNDER, 25910, QTY, 6);
	 
		//<-- sync //<--
	}
	/** ###########  [Model 01] ########### **/
	public void createCloseShort()throws Exception{
		
		//makeCloseShort(26460, UNDER, 26310, QTY, 26510, RR);
		openShort(24910, UNDER, 25110, QTY, RR, 24960);
		
		startOpenShort(25010, 24310, 100);
		
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
		startOpenLong(27010, 27810, 200);
		
		startOpenLong(25710, 26910, 100);
		
		makeCloseLong(25460, OVER, 25610, QTY, 25510, THIRD);
		makeCloseLong(25360, OVER, 25510, QTY, 25410, THIRD);
		makeCloseLong(25260, OVER, 25410, QTY, 25310, THIRD);
		makeCloseLong(25160, OVER, 25310, QTY, 25210, THIRD);
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		openLong(25210, OVER, 25110, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 26256 ------- short  ↓↓↓↓  **/
		closeLong(25010, UNDER, 25110, QTY, THIRD);
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
		
		makeOpenLong(25060, UNDER, 24910, QTY, 25010, THIRD);
		
		startMakeOpenLong(24810, 23010, 100, false);
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