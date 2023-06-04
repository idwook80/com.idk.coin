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
		makeOpenShort(28510, OVER, 28710, QTY, 28660, ONCE);
		makeOpenShort(28310, OVER, 28510, QTY, 28460, ONCE);
		makeOpenShort(28110, OVER, 28310, QTY, 28260, ONCE);
		makeOpenShort(27910, OVER, 28110, QTY, 28060, ONCE);
		
		makeOpenShort(27860, OVER, 28010, QTY, 27960, ONCE);
		makeOpenShort(27710, OVER, 27910, QTY, 27860, RR);
		makeOpenShort(27610, OVER, 27810, QTY, 27760, ONCE);
		makeOpenShort(27510, OVER, 27710, QTY, 27660, RR);
		makeOpenShort(27410, OVER, 27610, QTY, 27560, ONCE);
		makeOpenShort(27310, OVER, 27510, QTY, 27460, RR);
		//makeOpenShort(27210, OVER, 27410, QTY, 27360, ONCE);
		//makeOpenShort(27110, OVER, 27310, QTY, 27110, RR);
		
		//Short First ## http://172.16.203.17:8081/mvest/bybit/
		//POSITION  --> [LONG] 
		
	}
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	public void setShort() throws Exception{
		//closeShort(27510, OVER, 27460, QTY, RR);
		closeShort(27410, OVER, 27360, QTY, TWICE);
		closeShort(27360, OVER, 27310, QTY, ONCE);
		closeShort(27310, OVER, 27110, QTY, RR);
		
		
		//closeShort(27210, OVER, 27010, QTY, RR);
		//closeShort(27110, OVER, 26910, QTY, RR); 
		 
		/** ↑↑↑↑ -------  Price Line  27432  -------  Long First ↓↓↓↓  **/
		openShort(27010, UNDER, 27210, QTY, RR);
		openShort(26910, UNDER, 27110, QTY, RR);
		//openShort(26910, UNDER, 27110, QTY, RR);
		openShort(26810, UNDER, 27010, QTY, RR);
	
	
		//<-- sync //<--
	}
	/** ###########  [Model 02] ########### **/
	public void createCloseShort()throws Exception{
		openShort(26710, UNDER, 26910, QTY, RR, 26760);
		openShort(26610, UNDER, 26810, QTY, RR, 26660);
		openShort(26510, UNDER, 26710, QTY, RR, 26560);
		openShort(26410, UNDER, 26610, QTY, RR, 26460);
		openShort(26310, UNDER, 26510, QTY, RR, 26310);
		openShort(26110, UNDER, 26310, QTY, RR, 26160);
		openShort(25910, UNDER, 26110, QTY, RR, 25960);
		
		openShort(25810, UNDER, 26010, QTY, RR, 25860);
		openShort(25710, UNDER, 25910, QTY, RR, 25760);
		openShort(25510, UNDER, 25710, QTY, RR, 25560);
		openShort(25310, UNDER, 25510, QTY, RR, 25360);
		openShort(25110, UNDER, 25310, QTY, RR, 25160);
		//createCloseShort(26110, UNDER, 25910, QTY, 26110, RR);
		
		openShort(24910, UNDER, 25110, QTY, RR, 24960);
		openShort(24710, UNDER, 24910, QTY, RR, 24760);
		openShort(24510, UNDER, 24710, QTY, RR, 24560);
		openShort(24310, UNDER, 24510, QTY, RR, 24360);
		openShort(24110, UNDER, 24310, QTY, RR, 24160);
		//createCloseShort(25110, UNDER, 24910, QTY, 25110, RR);
		
		openShort(23910, UNDER, 24110, QTY, RR, 23960);
		openShort(23710, UNDER, 23910, QTY, RR, 23760);
		openShort(23510, UNDER, 23710, QTY, RR, 23560);
		openShort(23310, UNDER, 23510, QTY, RR, 23360);
		openShort(23110, UNDER, 23310, QTY, RR, 23160);
		//createCloseShort(24110, UNDER, 23910, QTY, 24110, RR);
		
		openShort(22910, UNDER, 23110, QTY, RR, 22960);
		openShort(22710, UNDER, 22910, QTY, RR, 22760);
		openShort(22510, UNDER, 22710, QTY, RR, 22560);
		openShort(22310, UNDER, 22510, QTY, RR, 22360);
		openShort(22110, UNDER, 22310, QTY, RR, 22160);
	}

	
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */
	public void createCloseLong() throws Exception{
		openLong(33010, OVER, 32810, QTY, RR, 32960);
		openLong(32810, OVER, 32610, QTY, RR, 32760);
		openLong(32610, OVER, 32410, QTY, RR, 32560);
		openLong(32410, OVER, 32210, QTY, RR, 32360);
		openLong(32210, OVER, 32010, QTY, RR, 32160);
		
		openLong(32010, OVER, 31810, QTY, RR, 31960);
		openLong(31810, OVER, 31610, QTY, RR, 31760);
		openLong(31610, OVER, 31410, QTY, RR, 31560);
		openLong(31410, OVER, 31210, QTY, RR, 31360);
		openLong(31210, OVER, 31010, QTY, RR, 31160);
		
		openLong(31010, OVER, 30810, QTY, RR, 30960);
		openLong(30810, OVER, 30610, QTY, RR, 30760);
		openLong(30610, OVER, 30410, QTY, RR, 30560);
		openLong(30410, OVER, 30210, QTY, RR, 30360);
		openLong(30210, OVER, 30010, QTY, RR, 30160);
		
		//createCloseLong(29010, OVER, 29210, QTY, 29010, RR);
		openLong(30010, OVER, 29810, QTY, RR, 29960);
		openLong(29810, OVER, 29610, QTY, RR, 29760);
		openLong(29610, OVER, 29410, QTY, RR, 29560);
		openLong(29410, OVER, 29210, QTY, RR, 29360);
		openLong(29210, OVER, 29010, QTY, RR, 29160);
		
		//createCloseLong(28010, OVER, 28210, QTY, 28110, RR);
		openLong(29010, OVER, 28810, QTY, RR, 28960);
		openLong(28810, OVER, 28610, QTY, RR, 28760);
		openLong(28610, OVER, 28410, QTY, RR, 28560);
		openLong(28410, OVER, 28210, QTY, RR, 28360);
		openLong(28210, OVER, 28010, QTY, RR, 28160);
		
		openLong(28110, OVER, 27910, QTY, RR, 28060);
		openLong(28010, OVER, 27810, QTY, RR, 27960);
		openLong(27910, OVER, 27710, QTY, RR, 27860);
		openLong(27810, OVER, 27610, QTY, RR, 27760);
		openLong(27710, OVER, 27510, QTY, RR, 27660);
		openLong(27610, OVER, 27410, QTY, RR, 27560);
		openLong(27510, OVER, 27310, QTY, RR, 27460);
		openLong(27410, OVER, 27210, QTY, RR, 27360);
		//openLong(27310, OVER, 27110, QTY, RR, 27260);
		
	}
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	public void setLong() throws Exception{
		
		//<-- sync //<--
		//openLong(27210, OVER, 27010, QTY, RR);
		openLong(27310, OVER, 27110, QTY, RR);
		//openLong(27210, OVER, 27010, QTY, RR);
		//openLong(27110, OVER, 27010, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 27049 ------- short  ↓↓↓↓  **/
		closeLong(27010, UNDER, 27210, QTY, RR);
		closeLong(27010, UNDER, 27110, QTY, RR);//<--
		closeLong(26960, UNDER, 27010, QTY, RR); 
		closeLong(26910, UNDER, 26960, QTY, TWICE);
		closeLong(26860, UNDER, 26910, QTY, ONCE);
		closeLong(26810, UNDER, 26860, QTY, RR);
		closeLong(26710, UNDER, 26760, QTY, THIRD);
		//<-- sync //<--
	}
	/** ###########  [Model 02] ########### **/
	public void createOpenLong() throws Exception{
		//makeOpenLong(27010, UNDER, 26860, QTY, 26910, ONCE);
		//makeOpenLong(26960, UNDER, 26810, QTY, 26860, RR);
		//makeOpenLong(26860, UNDER, 26710, QTY, 26760, ONCE);
		makeOpenLong(26760, UNDER, 26610, QTY, 26660, RR);
		makeOpenLong(26660, UNDER, 26510, QTY, 26560, ONCE);
		makeOpenLong(26560, UNDER, 26410, QTY, 26460, RR);
		makeOpenLong(26460, UNDER, 26310, QTY, 26360, ONCE);
		makeOpenLong(26360, UNDER, 26210, QTY, 26260, RR);
		makeOpenLong(26260, UNDER, 26110, QTY, 26160, ONCE);
		makeOpenLong(26160, UNDER, 26010, QTY, 26060, ONCE);
		
		makeOpenLong(26010, UNDER, 25810, QTY, 25860, ONCE);
		makeOpenLong(25810, UNDER, 25610, QTY, 25660, ONCE);
		makeOpenLong(25610, UNDER, 25410, QTY, 25460, ONCE);
		makeOpenLong(25410, UNDER, 25210, QTY, 25260, ONCE);
		longStopLoss(25110, QTY2);
		makeOpenLong(25210, UNDER, 25010, QTY, 25110, RR);
		
		makeOpenLong(25010, UNDER, 24810, QTY, 24910, RR);
		longStopLoss(24710, QTY2);
		makeOpenLong(24810, UNDER, 24610, QTY, 24710, RR);
		makeOpenLong(24610, UNDER, 24410, QTY, 24510, RR);
		makeOpenLong(24410, UNDER, 24210, QTY, 24310, RR);
		longStopLoss(24110, QTY2);
		makeOpenLong(24210, UNDER, 24010, QTY, 24110, RR);
		
		
		makeOpenLong(24010, UNDER, 23810, QTY, 23910, RR);
		longStopLoss(23710, QTY2);
		makeOpenLong(23810, UNDER, 23610, QTY, 23710, RR);
		makeOpenLong(23610, UNDER, 23410, QTY, 23510, RR);
		makeOpenLong(23410, UNDER, 23210, QTY, 23310, RR);
		longStopLoss(23110, QTY2);
		makeOpenLong(23210, UNDER, 23010, QTY, 23110, RR);
		
		makeOpenLong(23010, UNDER, 22810, QTY, 22910, RR);
		longStopLoss(22710, QTY2);
		makeOpenLong(22810, UNDER, 22610, QTY, 22710, RR);
		makeOpenLong(22610, UNDER, 22410, QTY, 22510, RR);
		makeOpenLong(22410, UNDER, 22210, QTY, 22310, RR);
		longStopLoss(22110, QTY2);
		makeOpenLong(22210, UNDER, 22010, QTY, 22110, RR);
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
