package com.idk.coin.bybit.alram;

import java.util.ArrayList;

import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class AlarmManager80 extends BybitAlarmsModel {
	public AlarmManager80(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager80(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty = user.getDefault_qty();
		if(default_qty == null) default_qty = "0.1";
		setDefault_qty(Double.valueOf(default_qty));
		
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
	  
	}
	public void run() {
		while(is_run) {
			try {
				LOG.info(current_price + " , "  + this.getSize() + "  : " + this.getClass().getName());
				ArrayList<Position> ps = PositionRest.getActiveMyPosition(user.getApi_key(),user.getApi_secret(), symbol);
				Balance balance 		 =   WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
				Position buy =  Position.getPosition(ps, symbol, "Buy");
				Position sell = Position.getPosition(ps, symbol, "Sell");
				LOG.info(buy.getSize() + " , [" +  String.format("%.2f",(buy.getSize()/QTY)/10) + "]:" 
						 + "[" +  String.format("%.2f", (sell.getSize()/QTY)/10)+"] , "+ sell.getSize()
				 		+" , ["+String.format("%.2f",balance.getEquity()) + "]");
				Thread.sleep(1000* 30);
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
	}
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
		
		createOpenShort(29710, OVER, 29910, QTY, 29810, RR);
		shortStopLoss(29810, QTY2);
		createOpenShort(29510, OVER, 29710, QTY, 29610, RR);
		createOpenShort(29310, OVER, 29510, QTY, 29410, RR);
		createOpenShort(29110, OVER, 29310, QTY, 29210, RR);
		shortStopLoss(29210, QTY2);
		createOpenShort(28910, OVER, 29110, QTY, 29010, RR);
		
		createOpenShort(28710, OVER, 28910, QTY, 28810, RR);
		shortStopLoss(28810, QTY2);
		createOpenShort(28510, OVER, 28710, QTY, 28610, RR);
		createOpenShort(28310, OVER, 28510, QTY, 28410, RR);
		createOpenShort(28110, OVER, 28310, QTY, 28260, ONCE);
		createOpenShort(28010, OVER, 28210, QTY, 28160, ONCE);
		createOpenShort(27960, OVER, 28110, QTY, 28060, ONCE);
		
		createOpenShort(27860, OVER, 28010, QTY, 27960, ONCE);
		createOpenShort(27760, OVER, 27910, QTY, 27860, RR);
		createOpenShort(27660, OVER, 27810, QTY, 27760, ONCE);
		createOpenShort(27560, OVER, 27710, QTY, 27660, RR);
		createOpenShort(27460, OVER, 27610, QTY, 27560, ONCE);
		createOpenShort(27360, OVER, 27510, QTY, 27460, RR);
		//createOpenShort(27260, OVER, 27410, QTY, 27360, ONCE);
		//createOpenShort(27160, OVER, 27310, QTY, 27260, RR);
		//createOpenShort(27060, OVER, 27210, QTY, 27160, ONCE);
		//createOpenShort(27260, OVER, 27310, QTY, 27110, RR);
		//Short First ## http://218.148.204.16:8081/mvest/bybit/
		//POSITION  --> [SHORT]
		
	}
	
/**	############################################[SHORT]### **/
/** ######### Short Order exists  Start ########[SHORT]###**/  //<[SHORT]<----------------------------
/**	################## [idwook80 BTCUSDT] ######[SHORT]### **/
	public void setShort() throws Exception {
		//<--sync
		closeShort(27410, OVER, 27360, QTY, TWICE);
		closeShort(27310, OVER, 27260, QTY, RR);
		closeShort(27260, OVER, 27210, QTY, TWICE);
		//closeShort(27210, OVER, 27110, QTY, RR);
		//closeShort(27160, OVER, 27010, QTY, RR);
		//closeShort(27110, OVER, 26910, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 27705  ------- long ↓↓↓↓  **/
		openShort(27110, UNDER, 27210, QTY, RR);
		openShort(27010, UNDER, 27160, QTY, RR);
		openShort(26910, UNDER, 27110, QTY, RR);
		//openShort(27060, UNDER, 27160, QTY, RR);
		//openShort(26910, UNDER, 27110, QTY, RR);
		//openShort(26910, UNDER, 27110, QTY, RR);
		//openShort(26810, UNDER, 27010, QTY, RR);
		//openShort(26710, UNDER, 26910, QTY, RR);
	
		//<--sync //<--
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	################## [idwook80 BTCUSDT] ######### **/
	public void createCloseShort() throws Exception {
		//openShort(27210, UNDER, 27310, QTY, RR, 27160);
		//openShort(26910, UNDER, 27110, QTY, RR, 26960);
		openShort(26810, UNDER, 27010, QTY, RR, 26860);
		openShort(26710, UNDER, 26910, QTY, RR, 26760);
		openShort(26610, UNDER, 26810, QTY, RR, 26660);
		openShort(26510, UNDER, 26710, QTY, RR, 26560);
		openShort(26310, UNDER, 26510, QTY, RR, 26360);
		openShort(26110, UNDER, 26310, QTY, RR, 26160);
		
		openShort(25910, UNDER, 26110, QTY, RR, 25960);
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
		
		//createCloseLong(28010, OVER, 28210, QTY, 28110, RR);
		openLong(29010, OVER, 28810, QTY, RR, 28960);
		openLong(28810, OVER, 28610, QTY, RR, 28760);
		openLong(28610, OVER, 28410, QTY, RR, 28560);
		openLong(28410, OVER, 28210, QTY, RR, 28360);
		openLong(28210, OVER, 28010, QTY, RR, 28060);
		
		openLong(28010, OVER, 27810, QTY, RR, 27960);
		openLong(27810, OVER, 27610, QTY, RR, 27760);
		openLong(27610, OVER, 27410, QTY, RR, 27560);
		openLong(27510, OVER, 27310, QTY, RR, 27460);
		openLong(27410, OVER, 27210, QTY, RR, 27360);
		
		//openLong(27210, OVER, 27010, QTY, RR, 27160);
	
	
	}
/**	###########################################[LONG]##### **/
/** ######### Long Order exists  Start ########[LONG]##### **///<[LONG]<----------------------------
//**	################## [idwook80 BTCUSDT] #[LONG]##### **/
	public void setLong() throws Exception {
		//<--sync	//<-- 
		//openLong(27310, OVER, 27110, QTY, RR);
		//openLong(27210, OVER, 27010, QTY, RR);
		//openLong(27110, OVER, 27010, QTY, RR);//<--
		//openLong(27060, OVER, 26960, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 27256  ------- short  ↓↓↓↓  **/
		
		closeLong(27110, UNDER, 27310, QTY, RR);
		closeLong(27010, UNDER, 27210, QTY, RR);
		//closeLong(27010, UNDER, 27110, QTY, RR);
		closeLong(26960, UNDER, 27060, QTY, RR);
		
		
		closeLong(26910, UNDER, 27010, QTY, RR);
		closeLong(26860, UNDER, 26910, QTY, TWICE);
		closeLong(26810, UNDER, 26860, QTY, THIRD);
	
		//<--sync //<--
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void createOpenLong() throws Exception {
		
		createOpenLong(26860, UNDER, 26710, QTY, 26760, ONCE);
		createOpenLong(26760, UNDER, 26610, QTY, 26660, RR);
		createOpenLong(26660, UNDER, 26510, QTY, 26560, ONCE);
		createOpenLong(26560, UNDER, 26410, QTY, 26460, RR);
		createOpenLong(26460, UNDER, 26310, QTY, 26360, ONCE);
		createOpenLong(26360, UNDER, 26210, QTY, 26260, RR);
		createOpenLong(26260, UNDER, 26110, QTY, 26160, ONCE);
		createOpenLong(26160, UNDER, 26010, QTY, 26060, RR);
		
		createOpenLong(26060, UNDER, 25910, QTY, 25960, ONCE);
		createOpenLong(25960, UNDER, 25810, QTY, 25860, ONCE);
		longStopLoss(25710, QTY2);
		createOpenLong(25760, UNDER, 25610, QTY, 25710, RR);
		createOpenLong(25560, UNDER, 25410, QTY, 25510, RR);
		createOpenLong(25360, UNDER, 25210, QTY, 25310, RR);
		longStopLoss(25110, QTY2);
		createOpenLong(25160, UNDER, 25010, QTY, 25110, RR);
		
		
		createOpenLong(25010, UNDER, 24810, QTY, 24910, RR);
		longStopLoss(24710, QTY2);
		createOpenLong(24810, UNDER, 24610, QTY, 24710, RR);
		createOpenLong(24610, UNDER, 24410, QTY, 24510, RR);
		createOpenLong(24410, UNDER, 24210, QTY, 24310, RR);
		longStopLoss(24110, QTY2);
		createOpenLong(24210, UNDER, 24010, QTY, 24110, RR);
		
		createOpenLong(24010, UNDER, 23810, QTY, 24010, RR);
		longStopLoss(23710, QTY2);
		createOpenLong(23810, UNDER, 23610, QTY, 23810, RR);
		createOpenLong(23610, UNDER, 23410, QTY, 23610, RR);
		createOpenLong(23410, UNDER, 23210, QTY, 23410, RR);
		longStopLoss(23110, QTY2);
		createOpenLong(23210, UNDER, 23010, QTY, 23210, RR);
  
		/*
		createOpenLong(11010, UNDER, 10810, QTY, 11010, RR);
		createOpenLong(10810, UNDER, 10610, QTY, 10810, RR);
		createOpenLong(10610, UNDER, 10410, QTY, 10610, ONCE);
		createOpenLong(10410, UNDER, 10210, QTY, 10410, RR);
		createOpenLong(10210, UNDER, 10010, QTY, 10210, RR);*/
	}
	
	
}
