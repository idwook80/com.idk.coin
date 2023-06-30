package com.idk.coin.bybit.alram;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class AlarmManager80 extends BybitAlarmsModel {
	public static int ONE1		= 1;
	public static int SEC2		= 3;
	public static int THR3		= 5;
	public AlarmManager80(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager80(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty = user.getDefault_qty();
		if(default_qty == null) default_qty = "0.001";
		setDefault_qty(Double.valueOf(default_qty));
		
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
	    IDLE_TIME 		= 10; // 10분마다
	    RESET_TIME 		= 60 * 1;// 1시간마다
	    startCalculateEquity = 55.0000;
	    message_count		 = 13;
		//max_open_size	= 20;
		//over_open_size	= 5;
		//min_open_size	= 5;
	
		
		take_1			= 1.5;
		take_2 			= 3;
		loss_1			= -5;
		//clear_profit    = ENABLE;
	}
 
	public void alarmSet() throws Exception{
		LOG.info("Alarm Set");
		boolean is_debug  = DEBUG_ON;
		clearAllAlarms();
		if(is_debug) {
			enableDatabase(DISABLE);
			currentStatus(DEBUG_ON);
			clear_profit = DISABLE;
		}else {
			cancelAllOrder();
			enableDatabase(ENABLE);
			currentStatus(DEBUG_OFF);
			clear_profit = ENABLE;
			clearAlarmDatabase();
			registerAlarmDatabase();
			
		}
		
	}
	 public CalculateModel createCalculateModel(BybitAlarmsModel parent,double price,Position buy,
			 		Position sell, Balance balance,double qty, boolean debug) {
		 CalculateModel model =  new CalculatePositionV4(parent, price, buy, sell, balance, qty, debug);
		 model.setSizeValue(20, 5, 5);
		 //model.setIntervalProfit(50, 100, 50, 100);
		 
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
				
				//if(this.getSize() <= 0) reset_check_time = -1;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
 
	
	
	/**
	 * #################   [SHORT] #############
	 * @throws Exception
	 */
	
	public void createOpenShort() throws Exception {
		/**/
		
		createOpenShort(32710, OVER, 32910, QTY, 32810, RR);
		createOpenShort(32510, OVER, 32710, QTY, 32610, RR);
		shortStopLoss(32610, QTY2);
		createOpenShort(32310, OVER, 32510, QTY, 32410, RR);
		createOpenShort(32110, OVER, 32310, QTY, 32210, RR);
		shortStopLoss(32210, QTY2);
		createOpenShort(31910, OVER, 32110, QTY, 32010, RR);
		
		createOpenShort(31710, OVER, 31910, QTY, 31810, RR);
		shortStopLoss(31810, QTY2);
		createOpenShort(31510, OVER, 31710, QTY, 31610, RR);
		createOpenShort(31310, OVER, 31510, QTY, 31410, RR);
		createOpenShort(31110, OVER, 31310, QTY, 31210, RR);
		shortStopLoss(31210, QTY2);
		createOpenShort(30910, OVER, 31110, QTY, 31010, RR);
		
		createOpenShort(30710, OVER, 30910, QTY, 30810, RR);
		shortStopLoss(30810, QTY2);
		createOpenShort(30510, OVER, 30710, QTY, 30610, RR);
		createOpenShort(30310, OVER, 30510, QTY, 30410, RR);
		createOpenShort(30110, OVER, 30310, QTY, 30210, RR);
		shortStopLoss(30210, QTY2);
		createOpenShort(29910, OVER, 30110, QTY, 30010, RR);
		
		createOpenShort(29710, OVER, 29910, QTY, 29810, THIRD);
		shortStopLoss(29810, QTY2);
		createOpenShort(29510, OVER, 29710, QTY, 29610, THIRD);
		createOpenShort(29310, OVER, 29510, QTY, 29410, THIRD);
		createOpenShort(29110, OVER, 29310, QTY, 29210, THIRD);
		//shortStopLoss(29210, QTY2);
		//createOpenShort(28910, OVER, 29110, QTY, 29010, THIRD);
		
		//createOpenShort(28710, OVER, 28910, QTY, 28810, THIRD);
		//createOpenShort(28510, OVER, 28710, QTY, 28660, THIRD);
		//createOpenShort(28310, OVER, 28510, QTY, 28460, THIRD);
		//createOpenShort(28110, OVER, 28310, QTY, 28260, THIRD);
		//createOpenShort(27960, OVER, 28110, QTY, 28060, THIRD);
		
		//Short First ## http://218.148.204.16:8081/mvest/bybit/
		//POSITION  --> [LONG]
		
	}
	
/**	############################################[SHORT]### **/
/** ######### Short Order exists  Start ########[SHORT]###**/  //<[SHORT]<----------------------------
/**	################## [idwook80 BTCUSDT] ######[SHORT]### **/
	public void setShort() throws Exception {
		//<--sync
		//closeShort(28210, OVER, 28160, QTY, THIRD);
		/** ↑↑↑↑ -------  Price Line 27705  ------- long ↓↓↓↓  **/
		openShort(28760, UNDER, 28910, QTY, 6);
		openShort(28510, UNDER, 28710, QTY, 6);
		openShort(28310, UNDER, 28510, QTY, 6);
		openShort(28110, UNDER, 28310, QTY, 6);
		
		//<--sync //<--
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	################## [idwook80 BTCUSDT] ######### **/
	public void createCloseShort() throws Exception {
	 
		openShort(27710, UNDER, 27910, QTY, 6, 27760);
		openShort(27510, UNDER, 27710, QTY, 6, 27560);
		openShort(27310, UNDER, 27510, QTY, 6, 27360);
		openShort(27110, UNDER, 27310, QTY, 6, 27160);
		
		openShort(26910, UNDER, 27110, QTY, 6, 26960);
		openShort(26710, UNDER, 26910, QTY, 6, 26760);
		openShort(26510, UNDER, 26710, QTY, 6, 26560);
		openShort(26310, UNDER, 26510, QTY, 6, 26360);
		openShort(26110, UNDER, 26310, QTY, 6, 26160);
		
		openShort(25910, UNDER, 26110, QTY, 6, 25960);
		openShort(25710, UNDER, 25910, QTY, 6, 25760);
		openShort(25510, UNDER, 25710, QTY, 6, 25560);
		openShort(25310, UNDER, 25510, QTY, 6, 25360);
		openShort(25110, UNDER, 25310, QTY, 6, 25160);
		
		openShort(24910, UNDER, 25110, QTY, 6, 24960);
		openShort(24710, UNDER, 24910, QTY, 6, 24760);
		openShort(24510, UNDER, 24710, QTY, 6, 24560);
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
	
	
	
	
	
/**   ############### LONG SETTINGS ####################  **/
/**   ############### LONG SETTINGS ####################  **/	
/**   ############### LONG SETTINGS ####################  **/
	public void createCloseLong() throws Exception {
		
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
		
		//openLong(29010, OVER, 28810, QTY, RR, 28960);
		
		//createCloseLong(25260, OVER, 25410, QTY, 25210, RR);
	}
/**	###########################################[LONG]##### **/
/** ######### Long Order exists  Start ########[LONG]##### **///<[LONG]<----------------------------
//**	################## [idwook80 BTCUSDT] #[LONG]##### **/
	public void setLong() throws Exception {
		//<--sync	//<-- 
		//openLong(28010, OVER, 27810, QTY, RR);
		////openLong(27810, OVER, 27610, QTY, RR);
		//openLong(27610, OVER, 27410, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 27256  ------- short  ↓↓↓↓  **/
		closeLong(28760, UNDER, 28860, QTY, ONCE);
		closeLong(28710, UNDER, 28810, QTY, ONCE);
		closeLong(28610, UNDER, 28810, QTY, ONCE);
	  
		//<--sync //<--
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void createOpenLong() throws Exception {
		
		//createOpenLong(28960, UNDER, 28810, QTY, 29010, THIRD);
		//createOpenLong(28760, UNDER, 28610, QTY, 28810, THIRD);
		//createOpenLong(28560, UNDER, 28410, QTY, 28610, THIRD);
		createOpenLong(28360, UNDER, 28210, QTY, 28410, THIRD);
		createOpenLong(28160, UNDER, 28010, QTY, 28210, THIRD);
		
		createOpenLong(27960, UNDER, 27810, QTY, 28010, THIRD);
		createOpenLong(27760, UNDER, 27610, QTY, 27810, THIRD);
		createOpenLong(27560, UNDER, 27410, QTY, 27610, THIRD);
		createOpenLong(27360, UNDER, 27210, QTY, 27410, THIRD);
		createOpenLong(27160, UNDER, 27010, QTY, 27210, THIRD);
		
		createOpenLong(26960, UNDER, 26810, QTY, 27010, THIRD);
		createOpenLong(26760, UNDER, 26610, QTY, 26810, THIRD);
		createOpenLong(26560, UNDER, 26410, QTY, 26510, THIRD);
		createOpenLong(26360, UNDER, 26210, QTY, 26260, THIRD);
		createOpenLong(26160, UNDER, 26010, QTY, 26060, THIRD);
		
		createOpenLong(25960, UNDER, 25810, QTY, 25860, THIRD);
		createOpenLong(25760, UNDER, 25610, QTY, 25660, THIRD);
		createOpenLong(25560, UNDER, 25410, QTY, 25460, THIRD);
		createOpenLong(25360, UNDER, 25210, QTY, 25260, THIRD);
		createOpenLong(25160, UNDER, 25010, QTY, 25060, THIRD);
		
		createOpenLong(24960, UNDER, 24810, QTY, 24860, THIRD);
		createOpenLong(24760, UNDER, 24610, QTY, 24660, THIRD);
		createOpenLong(24560, UNDER, 24410, QTY, 24460, THIRD);
		createOpenLong(24360, UNDER, 24210, QTY, 24260, THIRD);
		longStopLoss(24110, QTY2);
		createOpenLong(24160, UNDER, 24010, QTY, 24060, THIRD);
		
		createOpenLong(24010, UNDER, 23810, QTY, 23860, THIRD);
		longStopLoss(23710, QTY2);
		createOpenLong(23810, UNDER, 23610, QTY, 23810, THIRD);
		createOpenLong(23610, UNDER, 23410, QTY, 23610, THIRD);
		createOpenLong(23410, UNDER, 23210, QTY, 23410, THIRD);
		longStopLoss(23110, QTY2);
		createOpenLong(23210, UNDER, 23010, QTY, 23210, THIRD);
  
		/*
		createOpenLong(11010, UNDER, 10810, QTY, 11010, RR);
		createOpenLong(10810, UNDER, 10610, QTY, 10810, RR);
		createOpenLong(10610, UNDER, 10410, QTY, 10610, ONCE);
		createOpenLong(10410, UNDER, 10210, QTY, 10410, RR);
		createOpenLong(10210, UNDER, 10010, QTY, 10210, RR);*/
	}
	
	
}
