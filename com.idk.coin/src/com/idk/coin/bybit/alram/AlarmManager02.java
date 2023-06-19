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
	public static int IDLE_TIME = 10;
	public int idle_check_time = 0; //min
	
	public void run() {
		while(is_run) {
			try {
				status();
				if(idle_check_time-- < 0) {
					this.printListString();
					checkAlarmIdles();
					idle_check_time = IDLE_TIME;
				}
				Thread.sleep(1000* 60 *1);
				//this.printListString();
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
		enableDatabase(false);
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
		
		makeOpenShort(28710, OVER, 28910, QTY, 28860, THIRD);
		shortStopLoss(28810, QTY2);
		makeOpenShort(28510, OVER, 28710, QTY, 28660, THIRD);
		makeOpenShort(28310, OVER, 28510, QTY, 28460, THIRD);
		makeOpenShort(28110, OVER, 28310, QTY, 28260, THIRD);
		makeOpenShort(27910, OVER, 28110, QTY, 28060, THIRD);
		
		makeOpenShort(27760, OVER, 27910, QTY, 27860, THIRD);
		makeOpenShort(27560, OVER, 27710, QTY, 27660, THIRD);
		makeOpenShort(27360, OVER, 27510, QTY, 27460, THIRD);
		makeOpenShort(27160, OVER, 27310, QTY, 27260, THIRD);
		makeOpenShort(26960, OVER, 27110, QTY, 27060, THIRD);
		
		makeOpenShort(26760, OVER, 26910, QTY, 26860, THIRD);
		makeOpenShort(26660, OVER, 26810, QTY, 26760, THIRD);
		makeOpenShort(26560, OVER, 26710, QTY, 26660, THIRD);
		//makeOpenShort(26510, OVER, 26660, QTY, 26610, THIRD);
		//makeOpenShort(26360, OVER, 26510, QTY, 26460, THIRD);
		//makeOpenShort(26310, OVER, 26460, QTY, 26410, THIRD);
		//makeOpenShort(26260, OVER, 26410, QTY, 26360, THIRD);
		
		//Short First ## http://172.16.203.17:8081/mvest/bybit/
		//POSITION  --> [LONG] 
		
	}
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	public void setShort() throws Exception{
		//closeShort(26810, OVER, 26760, QTY, THIRD);
		//closeShort(26710, OVER, 26660, QTY, THIRD);
		closeShort(26660, OVER, 26610, QTY, THIRD);
		closeShort(26610, OVER, 26560, QTY, THIRD);
		closeShort(26560, OVER, 26510, QTY, THIRD);
		//closeShort(26510, OVER, 26460, QTY, THIRD);
		//closeShort(26460, OVER, 26410, QTY, THIRD);
		/** ↑↑↑↑ -------  Price Line  27432  -------  Long First ↓↓↓↓  **/
		openShort(26460, UNDER, 26510, QTY, 6);
		openShort(26410, UNDER, 26460, QTY, 6);
		openShort(26360, UNDER, 26460, QTY, 6);//<--
		openShort(26310, UNDER, 26410, QTY, 6); 
		openShort(26260, UNDER, 26410, QTY, 6);//<--
		
		//<-- sync //<--
	}
	/** ###########  [Model 02] ########### **/
	public void createCloseShort()throws Exception{
		
		makeCloseShort(26360, UNDER, 26210, QTY, 26360, RR);
		makeCloseShort(26260, UNDER, 26110, QTY, 26310, RR);
		makeCloseShort(26160, UNDER, 26010, QTY, 26210, RR);
		
		//openShort(26010, UNDER, 26210, QTY, 6, 25960);
		openShort(25910, UNDER, 26110, QTY, 6, 25960);
		openShort(25710, UNDER, 25910, QTY, 6, 25760);
		openShort(25510, UNDER, 25710, QTY, 6, 25560);
		openShort(25310, UNDER, 25510, QTY, 6, 25360);
		openShort(25110, UNDER, 25310, QTY, 6, 25160);
		
		openShort(24910, UNDER, 25110, QTY, 6, 24960);
		openShort(24710, UNDER, 24910, QTY, 6, 24760);
		openShort(24510, UNDER, 24710, QTY, 6, 24560);
		openShort(24410, UNDER, 24610, QTY, 6, 24460);
		openShort(24310, UNDER, 24510, QTY, 6, 24360);
		openShort(24110, UNDER, 24310, QTY, 6, 24160);
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
		
		openLong(28010, OVER, 27810, QTY, RR, 27960);
		openLong(27810, OVER, 27610, QTY, RR, 27760);
		openLong(27610, OVER, 27410, QTY, RR, 27560);
		openLong(27410, OVER, 27210, QTY, RR, 27360);
		openLong(27210, OVER, 27010, QTY, RR, 27160);
		
		openLong(27010, OVER, 26810, QTY, RR, 26960);
		openLong(26810, OVER, 26610, QTY, RR, 26760);
		
		//createCloseLong(25210, OVER, 25310, QTY, 25210, 6);
	}
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	public void setLong() throws Exception{
		
		//<-- sync //<--
		openLong(26710, OVER, 26510, QTY, RR);//<--
		openLong(26610, OVER, 26410, QTY, RR);//<--
		/** ↑↑↑↑ -------  Price Line 27049 ------- short  ↓↓↓↓  **/
		//closeLong(26410, UNDER, 26610, QTY, THIRD); 
		closeLong(26310, UNDER, 26510, QTY, THIRD);//<--
		closeLong(26210, UNDER, 26410, QTY, THIRD);
		//<-- sync //<--
	}
	/** ###########  [Model 02] ########### **/
	public void createOpenLong() throws Exception{
		
		//makeOpenLong(26360, UNDER, 26210, QTY, 26410, THIRD);
		makeOpenLong(26260, UNDER, 26110, QTY, 26310, THIRD);
		makeOpenLong(26160, UNDER, 26010, QTY, 26210, THIRD);
	 
		makeOpenLong(26060, UNDER, 25910, QTY, 26110, THIRD);
		makeOpenLong(25960, UNDER, 25810, QTY, 25860, THIRD);
		makeOpenLong(25860, UNDER, 25710, QTY, 25760, THIRD);
		makeOpenLong(25760, UNDER, 25610, QTY, 25660, THIRD);
		makeOpenLong(25560, UNDER, 25410, QTY, 25460, THIRD);
		makeOpenLong(25360, UNDER, 25210, QTY, 25260, THIRD);
		makeOpenLong(25160, UNDER, 25010, QTY, 25060, THIRD);
		
		makeOpenLong(24960, UNDER, 24810, QTY, 24860, THIRD);
		makeOpenLong(24760, UNDER, 24610, QTY, 24660, THIRD);
		makeOpenLong(24560, UNDER, 24410, QTY, 24460, THIRD);
		makeOpenLong(24360, UNDER, 24210, QTY, 24260, THIRD);
		makeOpenLong(24160, UNDER, 24010, QTY, 24110, THIRD);
		
		
		makeOpenLong(24010, UNDER, 23810, QTY, 23860, THIRD);
		longStopLoss(23710, QTY2);
		makeOpenLong(23810, UNDER, 23610, QTY, 23660, THIRD);
		makeOpenLong(23610, UNDER, 23410, QTY, 23460, THIRD);
		makeOpenLong(23410, UNDER, 23210, QTY, 23260, THIRD);
		longStopLoss(23110, QTY2);
		makeOpenLong(23210, UNDER, 23010, QTY, 23060, THIRD);
		
		makeOpenLong(23010, UNDER, 22810, QTY, 22860, THIRD);
		longStopLoss(22710, QTY2);
		makeOpenLong(22810, UNDER, 22610, QTY, 22660, THIRD);
		makeOpenLong(22610, UNDER, 22410, QTY, 22460, THIRD);
		makeOpenLong(22410, UNDER, 22210, QTY, 22260, THIRD);
		longStopLoss(22110, QTY2);
		makeOpenLong(22210, UNDER, 22010, QTY, 22060, THIRD);
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