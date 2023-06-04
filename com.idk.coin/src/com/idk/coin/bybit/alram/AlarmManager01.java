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
				Thread.sleep(1000* 60 * 10);
				LOG.info(this.getSize() + "  : " + this.getClass().getName());
				this.printListString();
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
			shortStopLoss(end-profitLimit   , QTY2);
			shortStopLoss(start+profitLimit , QTY2);
		}
		
		
	}
	public void createOpenShort()  throws Exception{
		//makeOpenShort(26610, OVER, 26810, QTY, 26610, RR);
		startMakeOpenShort(32110, 32910, 200, true);
		startMakeOpenShort(31110, 31910, 200, true);
		startMakeOpenShort(29110, 29910, 200, true);
		startMakeOpenShort(27610, 28910, 100, false);
		
		makeOpenShort(27360, OVER, 27510, QTY, 27410, RR);
		//makeOpenShort(27260, OVER, 27410, QTY, 27310, RR);
	}
	/** ###########  [Model 01] ########### [SHORT]  **/
	public void setShort() throws Exception{
		closeShort(27410, OVER, 27360, QTY, 3);
		closeShort(27360, OVER, 27310, QTY, TWICE);
		closeShort(27310, OVER, 27260, QTY, TWICE);
		closeShort(27260, OVER, 27210, QTY, 3);
		//closeShort(27210, OVER, 27160, QTY, 3);
		//closeShort(27160, OVER, 27060, QTY, TWICE);
		//closeShort(27110, OVER, 26910, QTY, RR);
		/** ↑↑↑↑ -------  Price Line  27349  -------  Long First ↓↓↓↓  **/
		openShort(27160, UNDER, 27210, QTY, 4);
		openShort(27060, UNDER, 27160, QTY, 4);
		
		openShort(27010, UNDER, 27110, QTY, 4);
		openShort(26910, UNDER, 27060, QTY, 4);
		openShort(26810, UNDER, 27010, QTY, 4);
	
	}
	/** ###########  [Model 01] ########### **/
	public void createCloseShort()throws Exception{
		//makeCloseShort(26960, UNDER, 26810, QTY, 27010, RR);
		
		openShort(26710, UNDER, 26910, QTY, RR, 26760);
		
		startOpenShort(26810, 26210, 100);
		
		startOpenShort(26110, 25310, 200);
		startOpenShort(25110, 24310, 200);
		startOpenShort(24110, 23310, 200);
		startOpenShort(23110, 22310, 200);
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
		
		startOpenLong(27310, 27910, 100);
		openLong(27410, OVER, 27210, QTY, RR, 27360);
		//openLong(27310, OVER, 27110, QTY, RR, 27260);
	
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		//openLong(27210, OVER, 27060, QTY, RR);
		openLong(27310, OVER, 27110, QTY, THIRD);
	//openLong(27260, OVER, 27060, QTY, 2);
		/** ↑↑↑↑ -------  Price Line 27129 ------- short  ↓↓↓↓  **/
		//closeLong(26960, UNDER, 27010, QTY, RR);
		closeLong(27060, UNDER, 27260, QTY, TWICE);
		closeLong(27010, UNDER, 27160, QTY, RR);
		closeLong(26960, UNDER, 27010, QTY, TWICE);
		closeLong(26910, UNDER, 26960, QTY, TWICE);
		closeLong(26860, UNDER, 26910, QTY, TWICE);
		closeLong(26810, UNDER, 26860, QTY, 5);
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
		//makeOpenLong(27160, UNDER, 27010, QTY, 27060, TWICE);
		//makeOpenLong(27110, UNDER, 26960, QTY, 27010, 1);
		//makeOpenLong(27060, UNDER, 26910, QTY, 26960, TWICE);
		//makeOpenLong(26960, UNDER, 26810, QTY, 26910, RR);
		makeOpenLong(26810, UNDER, 26710, QTY, 26760, TWICE);
		makeOpenLong(26760, UNDER, 26610, QTY, 26660, TWICE);
		
		startMakeOpenLong(26510, 26010, 100, false);
		startMakeOpenLong(25810, 25010, 100, false);
		
		startMakeOpenLong(24810, 24010, 200, true);
		startMakeOpenLong(23810, 23010, 200, true);
		startMakeOpenLong(22810, 22010, 200, true);
		
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
			longStopLoss(start-profitLimit  , QTY2);
			longStopLoss(end+profitLimit 	, QTY2);
		}
	}
	
	
}