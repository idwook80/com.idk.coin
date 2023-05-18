package com.idk.coin.bybit.alram;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.BybitAlarmsModel;

public class AlarmManager02 extends BybitAlarmsModel {
	public AlarmManager02(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager02(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty = user.getDefault_qty();
		if(default_qty == null) default_qty = "0.05";
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
	public void createOpenShort()  throws Exception{
		makeOpenShort(32710, OVER, 32910, QTY, 32810, RR);
		makeOpenShort(32510, OVER, 32710, QTY, 32610, RR);
		shortStopLoss(32610, QTY2);
		makeOpenShort(32310, OVER, 32510, QTY, 32410, RR);
		makeOpenShort(32110, OVER, 32310, QTY, 32210, RR);
		shortStopLoss(32210, QTY2);
		makeOpenShort(31910, OVER, 32110, QTY, 32010, RR);
		
		makeOpenShort(31710, OVER, 31910, QTY, 31810, RR);
		shortStopLoss(31810, QTY2);
		makeOpenShort(31510, OVER, 31710, QTY, 31610, RR);
		makeOpenShort(31310, OVER, 31510, QTY, 31410, RR);
		makeOpenShort(31110, OVER, 31310, QTY, 31210, RR);
		shortStopLoss(31210, QTY2);
		makeOpenShort(30910, OVER, 31110, QTY, 31010, RR);
		 
		makeOpenShort(30710, OVER, 30910, QTY, 30810, RR);
		shortStopLoss(30810, QTY2);
		makeOpenShort(30510, OVER, 30710, QTY, 30610, RR);
		makeOpenShort(30310, OVER, 30510, QTY, 30410, RR);
		makeOpenShort(30110, OVER, 30310, QTY, 30210, RR);
		shortStopLoss(30210, QTY2);
		makeOpenShort(29910, OVER, 30110, QTY, 30010, RR);
		
		makeOpenShort(29710, OVER, 29910, QTY, 29810, RR);
		shortStopLoss(29810, QTY2);
		makeOpenShort(29510, OVER, 29710, QTY, 29610, RR);
		makeOpenShort(29310, OVER, 29510, QTY, 29410, RR);
		makeOpenShort(29110, OVER, 29310, QTY, 29210, RR);
		shortStopLoss(29210, QTY2);
		makeOpenShort(28910, OVER, 29110, QTY, 29010, RR);
		
		makeOpenShort(28710, OVER, 28910, QTY, 28810, RR);
		shortStopLoss(28810, QTY2);
		makeOpenShort(28510, OVER, 28710, QTY, 28610, RR);
		makeOpenShort(28310, OVER, 28510, QTY, 28410, RR);
		makeOpenShort(28110, OVER, 28310, QTY, 28210, RR);
		makeOpenShort(27910, OVER, 28110, QTY, 28010, RR);
		makeOpenShort(27810, OVER, 28010, QTY, 28960, ONCE);
		
		makeOpenShort(27710, OVER, 27910, QTY, 27810, RR);
		makeOpenShort(27610, OVER, 27810, QTY, 27760, ONCE);
		makeOpenShort(27510, OVER, 27710, QTY, 27610, RR);
		//makeOpenShort(27410, OVER, 27610, QTY, 27560, ONCE);
		
	}
	/** ###########  [Model 02] ########### [SHORT]  **/
	public void setShort() throws Exception{
		//addCloseShort(29960, OVER, 29960, QTY2, ONCE);
		//<--sync
		//addCloseShort(27710, OVER, 27610, QTY, RR);
		addCloseShort(27610, OVER, 27560, QTY, ONCE);
		addCloseShort(27510, OVER, 27410, QTY, RR);
		/** ↑↑↑↑ -------  Price Line  27722  -------  Long First ↓↓↓↓  **/
		addOpenShort(27310, UNDER, 27410, QTY, RR);
		addOpenShort(27210, UNDER, 27310, QTY, RR);
		addOpenShort(27110, UNDER, 27210, QTY, RR);
		//<-- sync
		//addOpenShort(26810, UNDER, 26810, QTY, ONCE);//<--
	}
	/** ###########  [Model 02] ########### **/
	public void createCloseShort()throws Exception{
		
		//makeCloseShort(27310, UNDER, 27110, QTY, 27210, RR);
		makeCloseShort(27210, UNDER, 27010, QTY, 27160, RR);
		
		makeOpenShort(27010, UNDER, 27110, QTY, 26910, RR);
		makeOpenShort(26810, UNDER, 26910, QTY, 26710, RR);
		makeOpenShort(26610, UNDER, 26710, QTY, 26510, RR);
		makeOpenShort(26410, UNDER, 26510, QTY, 26310, RR);
		makeOpenShort(26210, UNDER, 26310, QTY, 26110, RR);
		
		//makeCloseShort(27110, UNDER, 26910, QTY, 27110, RR);
		//makeCloseShort(26910, UNDER, 26710, QTY, 26910, RR);
		//makeCloseShort(26710, UNDER, 26510, QTY, 26710, RR);
		//makeCloseShort(26510, UNDER, 26310, QTY, 26510, RR);
		//makeCloseShort(26310, UNDER, 26110, QTY, 26310, RR);
		
		addOpenShort(25910, UNDER, 26110, QTY, RR);
		addOpenShort(25710, UNDER, 25910, QTY, RR);
		addOpenShort(25510, UNDER, 25710, QTY, RR);
		addOpenShort(25310, UNDER, 25510, QTY, RR);
		addOpenShort(25110, UNDER, 25310, QTY, RR);
		//makeCloseShort(26110, UNDER, 25910, QTY, 26110, RR);
		//makeCloseShort(25910, UNDER, 25710, QTY, 25910, RR);
		//makeCloseShort(25710, UNDER, 25510, QTY, 25710, RR);
		//makeCloseShort(25510, UNDER, 25310, QTY, 25510, RR);
		//makeCloseShort(25310, UNDER, 25110, QTY, 25310, RR);
		
		addOpenShort(24910, UNDER, 25110, QTY, RR);
		addOpenShort(24710, UNDER, 24910, QTY, RR);
		addOpenShort(24510, UNDER, 24710, QTY, RR);
		addOpenShort(24310, UNDER, 24510, QTY, RR);
		addOpenShort(24110, UNDER, 24310, QTY, RR);
		//makeCloseShort(25110, UNDER, 24910, QTY, 25110, RR);
		//makeCloseShort(24910, UNDER, 24710, QTY, 24910, RR);
		//makeCloseShort(24710, UNDER, 24510, QTY, 24710, RR);
		//makeCloseShort(24510, UNDER, 24310, QTY, 24510, RR);
		//makeCloseShort(24310, UNDER, 24110, QTY, 24310, RR);
		
		
		addOpenShort(23910, UNDER, 25110, QTY, RR);
		addOpenShort(23710, UNDER, 24910, QTY, RR);
		addOpenShort(23510, UNDER, 24710, QTY, RR);
		addOpenShort(23310, UNDER, 24510, QTY, RR);
		addOpenShort(23110, UNDER, 24310, QTY, RR);
		//makeCloseShort(24110, UNDER, 23910, QTY, 24110, RR);
		//makeCloseShort(23910, UNDER, 23710, QTY, 23910, RR);
		//makeCloseShort(23710, UNDER, 23510, QTY, 23710, RR);
		//makeCloseShort(23510, UNDER, 23310, QTY, 23510, RR);
		//makeCloseShort(23310, UNDER, 23110, QTY, 23310, RR);
	}

	
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */
	public void createCloseLong() throws Exception{
		 
		//makeCloseLong(32810, OVER, 33010, QTY, 32810, RR);
		makeCloseLong(32610, OVER, 32810, QTY, 32610, RR);
		//makeCloseLong(32410, OVER, 32610, QTY, 32410, RR);
		makeCloseLong(32210, OVER, 32410, QTY, 32210, RR);
		//makeCloseLong(32010, OVER, 32210, QTY, 32010, RR);
		
		//makeCloseLong(31810, OVER, 32010, QTY, 31910, RR);
		//makeCloseLong(31610, OVER, 31810, QTY, 31610, RR);
		makeCloseLong(31410, OVER, 31610, QTY, 31510, RR);
		//makeCloseLong(31210, OVER, 31410, QTY, 31210, RR);
		//makeCloseLong(31010, OVER, 31210, QTY, 31010, RR);
		addOpenLong(32010, OVER, 31910, QTY, RR);
		addOpenLong(31810, OVER, 31610, QTY, RR);
		addOpenLong(31410, OVER, 31210, QTY, RR);
		addOpenLong(31210, OVER, 31010, QTY, RR);
		
		
		//makeCloseLong(30810, OVER, 31010, QTY, 30810, RR);
		//makeCloseLong(30610, OVER, 30810, QTY, 30610, RR);
		makeCloseLong(30410, OVER, 30610, QTY, 30410, RR);
		//makeCloseLong(30210, OVER, 30410, QTY, 30210, RR);
		//makeCloseLong(30010, OVER, 30210, QTY, 30010, RR);
		addOpenLong(31010, OVER, 30810, QTY, RR);
		addOpenLong(30810, OVER, 30610, QTY, RR);
		addOpenLong(30410, OVER, 30210, QTY, RR);
		addOpenLong(30210, OVER, 30010, QTY, RR);
		
		//makeCloseLong(29810, OVER, 30010, QTY, 29810, RR);
		//makeCloseLong(29610, OVER, 29810, QTY, 29610, RR);
		//makeCloseLong(29410, OVER, 29610, QTY, 29410, RR);
		//makeCloseLong(29210, OVER, 29410, QTY, 29210, RR);
		//makeCloseLong(29010, OVER, 29210, QTY, 29010, RR);
		addOpenLong(30010, OVER, 29810, QTY, RR);
		addOpenLong(29810, OVER, 29610, QTY, RR);
		addOpenLong(29610, OVER, 29410, QTY, RR);
		addOpenLong(29410, OVER, 29210, QTY, RR);
		addOpenLong(29210, OVER, 29010, QTY, RR);
		
		//makeCloseLong(28810, OVER, 29010, QTY, 28810, RR);
		//makeCloseLong(28610, OVER, 28810, QTY, 28610, RR);
		//makeCloseLong(28410, OVER, 28610, QTY, 28410, RR);
		//makeCloseLong(28210, OVER, 28410, QTY, 28210, RR);
		//makeCloseLong(28010, OVER, 28210, QTY, 28110, RR);
		addOpenLong(29010, OVER, 28810, QTY, RR);
		addOpenLong(28810, OVER, 28610, QTY, RR);
		addOpenLong(28610, OVER, 28410, QTY, RR);
		addOpenLong(28410, OVER, 28210, QTY, RR);
		addOpenLong(28210, OVER, 28010, QTY, RR);
		
		//addOpenLong(28010, OVER, 27810, QTY, RR);
		makeOpenLong(27910, OVER, 27810, QTY, 28010, RR);
		makeOpenLong(27710, OVER, 27610, QTY, 27810, RR);
		makeOpenLong(27510, OVER, 27410, QTY, 27610, RR);
	}
	
	/** ###########  [Model 02] ########### [LONG] **/
	public void setLong() throws Exception{
		//addOpenLong(30210, OVER, 30210, QTY, ONCE);//<--
		//<-- sync
		//addOpenLong(27610, OVER, 27410, QTY, RR);
		addOpenLong(27510, OVER, 27310, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 27529 ------- short  ↓↓↓↓  **/
		
		//addCloseLong(27310, UNDER, 27510, QTY, RR);
		addCloseLong(27210, UNDER, 27410, QTY, RR);
		addCloseLong(27110, UNDER, 27310, QTY, RR);
		
		//<-- sync
		//addCloseLong(27010, UNDER, 27010, QTY2, ONCE);//<--
	}
	/** ###########  [Model 02] ########### **/
	public void createOpenLong() throws Exception{
		makeOpenLong(27210, UNDER, 27010, QTY, 27110, RR);
		makeOpenLong(27110, UNDER, 26910, QTY, 26960, ONCE);
		makeOpenLong(27010, UNDER, 26810, QTY, 26910, RR);
		makeOpenLong(26910, UNDER, 26710, QTY, 26760, ONCE);
		makeOpenLong(26810, UNDER, 26610, QTY, 26710, RR);
		makeOpenLong(26710, UNDER, 26510, QTY, 26560, ONCE);
		makeOpenLong(26610, UNDER, 26410, QTY, 26510, RR);
		makeOpenLong(26510, UNDER, 26310, QTY, 26360, ONCE);
		makeOpenLong(26410, UNDER, 26210, QTY, 26310, RR);
		makeOpenLong(26310, UNDER, 26110, QTY, 26260, ONCE);
		makeOpenLong(26210, UNDER, 26010, QTY, 26110, RR);
		
		makeOpenLong(26110, UNDER, 25910, QTY, 25960, ONCE);
		makeOpenLong(26010, UNDER, 25810, QTY, 25910, RR);
		makeOpenLong(25810, UNDER, 25610, QTY, 25710, RR);
		makeOpenLong(25610, UNDER, 25410, QTY, 25510, RR);
		makeOpenLong(25410, UNDER, 25210, QTY, 25310, RR);
		longStopLoss(25110, QTY2);
		makeOpenLong(25210, UNDER, 25010, QTY, 25110, RR);
		
		makeOpenLong(25010, UNDER, 24810, QTY, 24910, RR);
		makeOpenLong(24810, UNDER, 24610, QTY, 24710, RR);
		longStopLoss(24510, QTY2);
		makeOpenLong(24610, UNDER, 24410, QTY, 24510, RR);
		makeOpenLong(24410, UNDER, 24210, QTY, 24310, RR);
		longStopLoss(24110, QTY2);
		makeOpenLong(24210, UNDER, 24010, QTY, 24110, RR);
		
		
		/**
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
	}
	
	
}
