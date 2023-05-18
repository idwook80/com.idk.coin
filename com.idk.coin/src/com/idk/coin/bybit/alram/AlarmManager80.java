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
		createOpenShort(28110, OVER, 28310, QTY, 28210, RR);
		shortStopLoss(28110, QTY2);
		createOpenShort(27910, OVER, 28110, QTY, 28010, RR);
		
		createOpenShort(27710, OVER, 27910, QTY, 27810, RR);
		createOpenShort(27610, OVER, 27810, QTY, 27760, ONCE);
		createOpenShort(27510, OVER, 27710, QTY, 27610, RR);
		//createOpenShort(27410, OVER, 27610, QTY, 27560, ONCE);
		//createOpenShort(27310, OVER, 27510, QTY, 27410, RR);
		//Short First ## http://218.148.204.16:8081/mvest/bybit/
		
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<[SHORT]<----------------------------
/**	################## [idwook80] ###################### **/
	public void setShort() throws Exception {
		//addCloseShort(29210, OVER, 29210, QTY2, ONCE);
		//<--sync
		addCloseShort(27610, OVER, 27560, QTY, ONCE);
		addCloseShort(27510, OVER, 27410, QTY, RR);
		addCloseShort(27460, OVER, 27410, QTY, ONCE);
		
		addCloseShort(27410, OVER, 27360, QTY, ONCE);
		 
		
		/** ↑↑↑↑ -------  Price Line 27746  ------- long ↓↓↓↓  **/
		
		addOpenShort(27310, UNDER, 27360, QTY, ONCE);
		addOpenShort(27310, UNDER, 27410, QTY, RR);
		addOpenShort(27260, UNDER, 27360, QTY, RR);
		
		addOpenShort(27210, UNDER, 27260, QTY, ONCE);
		addOpenShort(27210, UNDER, 27310, QTY, RR);
		
		addOpenShort(27110, UNDER, 27260, QTY, RR);
		//<--sync
		//addOpenShort(26810, UNDER, 26810, QTY, ONCE);//<-- 
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void createCloseShort() throws Exception {
		
		makeOpenShort(27110, UNDER, 27210, QTY, 27010, RR);
		makeOpenShort(26910, UNDER, 27010, QTY, 26810, RR);
		makeOpenShort(26710, UNDER, 26810, QTY, 26610, RR);
		makeOpenShort(26510, UNDER, 26610, QTY, 26410, RR);
		makeOpenShort(26410, UNDER, 26510, QTY, 26310, RR);
		makeOpenShort(26210, UNDER, 26310, QTY, 26110, RR);
		makeOpenShort(26110, UNDER, 26210, QTY, 26010, RR);
		//createCloseShort(27110, UNDER, 26910, QTY, 27110, RR);
		//createCloseShort(26910, UNDER, 26710, QTY, 26910, RR);
		//createCloseShort(26710, UNDER, 26510, QTY, 26710, RR);
		//createCloseShort(26510, UNDER, 26310, QTY, 26510, RR);
		//createCloseShort(26310, UNDER, 26110, QTY, 26310, RR);
		
		makeOpenShort(26010, UNDER, 26110, QTY, 25910, RR);
		makeOpenShort(25810, UNDER, 25910, QTY, 25710, RR);
		makeOpenShort(25610, UNDER, 25710, QTY, 25510, RR);
		makeOpenShort(25410, UNDER, 25510, QTY, 25310, RR);
		makeOpenShort(25210, UNDER, 25310, QTY, 25110, RR);
		//createCloseShort(26110, UNDER, 25910, QTY, 26110, RR);
		//createCloseShort(25910, UNDER, 25710, QTY, 25910, RR);
		//createCloseShort(25710, UNDER, 25510, QTY, 25710, RR);
		//createCloseShort(25510, UNDER, 25310, QTY, 25510, RR);
		//createCloseShort(25310, UNDER, 25110, QTY, 25310, RR);
		
		addOpenShort(24910, UNDER, 25110, QTY, RR);
		addOpenShort(24710, UNDER, 24910, QTY, RR);
		//addOpenShort(24510, UNDER, 24710, QTY, RR);
		addOpenShort(24310, UNDER, 24510, QTY, RR);
		addOpenShort(24110, UNDER, 24310, QTY, RR);
		//createCloseShort(25110, UNDER, 24910, QTY, 25110, RR);
		//createCloseShort(24910, UNDER, 24710, QTY, 24910, RR);
		createCloseShort(24710, UNDER, 24510, QTY, 24710, RR);
		//createCloseShort(24510, UNDER, 24310, QTY, 24510, RR);
		//createCloseShort(24310, UNDER, 24110, QTY, 24310, RR);
		
		addOpenShort(23910, UNDER, 24110, QTY, RR);
		addOpenShort(23710, UNDER, 23910, QTY, RR);
		//addOpenShort(23510, UNDER, 23710, QTY, RR);
		addOpenShort(23310, UNDER, 23510, QTY, RR);
		addOpenShort(23110, UNDER, 23310, QTY, RR);
		//createCloseShort(24110, UNDER, 23910, QTY, 24110, RR);
		//createCloseShort(23910, UNDER, 23710, QTY, 23910, RR);
		createCloseShort(23710, UNDER, 23510, QTY, 23710, RR);
		//createCloseShort(23510, UNDER, 23310, QTY, 23510, RR);
		//createCloseShort(23310, UNDER, 23110, QTY, 23310, RR);
		
	}
	
	
	
	
	
/**   ############### LONG SETTINGS ####################  **/
/**   ############### LONG SETTINGS ####################  **/	
/**   ############### LONG SETTINGS ####################  **/
	public void createCloseLong() throws Exception {
		
		//createCloseLong(32810, OVER, 33010, QTY, 32810, RR);
		createCloseLong(32610, OVER, 32810, QTY, 32610, RR);
		//createCloseLong(32410, OVER, 32610, QTY, 32410, RR);
		createCloseLong(32210, OVER, 32410, QTY, 32210, RR);
		//createCloseLong(32010, OVER, 32210, QTY, 32010, RR);
		
		//createCloseLong(31810, OVER, 32010, QTY, 31810, RR);
		//createCloseLong(31610, OVER, 31810, QTY, 31610, RR);
		createCloseLong(31410, OVER, 31610, QTY, 31410, RR);
		//createCloseLong(31210, OVER, 31410, QTY, 31210, RR);
		//createCloseLong(31010, OVER, 31210, QTY, 31010, RR);
		addOpenLong(32010, OVER, 31810, QTY, RR);
		addOpenLong(31810, OVER, 31610, QTY, RR);
		addOpenLong(31410, OVER, 31210, QTY, RR);
		addOpenLong(31210, OVER, 31010, QTY, RR);
		
		//createCloseLong(30810, OVER, 31010, QTY, 30810, RR);
		//createCloseLong(30610, OVER, 30810, QTY, 30610, RR);
		createCloseLong(30410, OVER, 30610, QTY, 30410, RR);
		//createCloseLong(30210, OVER, 30410, QTY, 30210, RR);
		//createCloseLong(30010, OVER, 30210, QTY, 30010, RR);
		addOpenLong(31010, OVER, 30810, QTY, RR);
		addOpenLong(30810, OVER, 30610, QTY, RR);
		addOpenLong(30410, OVER, 30210, QTY, RR);
		addOpenLong(30210, OVER, 30010, QTY, RR);
		
		//createCloseLong(29810, OVER, 30010, QTY, 29810, RR);
		//createCloseLong(29610, OVER, 29810, QTY, 29610, RR);
		createCloseLong(29410, OVER, 29610, QTY, 29410, RR);
		//createCloseLong(29210, OVER, 29410, QTY, 29210, RR);
		//createCloseLong(29010, OVER, 29210, QTY, 29010, RR);
		addOpenLong(30010, OVER, 29810, QTY, RR);
		addOpenLong(29810, OVER, 29610, QTY, RR);
		//addOpenLong(29610, OVER, 29410, QTY, RR);
		addOpenLong(29410, OVER, 29210, QTY, RR);
		addOpenLong(29210, OVER, 29010, QTY, RR);
		
		
		//createCloseLong(28810, OVER, 29010, QTY, 28810, RR);
		//createCloseLong(28610, OVER, 28810, QTY, 28610, RR);
		//createCloseLong(28410, OVER, 28610, QTY, 28410, RR);
		//createCloseLong(28210, OVER, 28410, QTY, 28210, RR);
		//createCloseLong(28010, OVER, 28210, QTY, 28110, RR);
		addOpenLong(29010, OVER, 28810, QTY, RR);
		addOpenLong(28810, OVER, 28610, QTY, RR);
		addOpenLong(28610, OVER, 28410, QTY, RR);
		addOpenLong(28410, OVER, 28210, QTY, RR);
		addOpenLong(28210, OVER, 28010, QTY, RR);
		
		addOpenLong(28010, OVER, 27810, QTY, RR);
		makeOpenLong(27710, OVER, 27610, QTY, 27810, RR);
		makeOpenLong(27560, OVER, 27410, QTY, 27610, RR);
		//makeOpenLong(27410, OVER, 27310, QTY, 27510, RR);
		//makeOpenLong(27310, OVER, 27210, QTY, 27410, RR);
		//makeOpenLong(27110, OVER, 27010, QTY, 27210, RR);
		//createCloseLong(26810, OVER, 27010, QTY, 26810, RR);
		//makeOpenLong(27010, OVER, 26910, QTY, 27110, RR);
	}
/**	#######################################[LONG]##### **/
/** ######### Long Order exists  Start ####[LONG]### **///<[LONG]<----------------------------
/**	#######################################[LONG]## **/
	public void setLong() throws Exception {
		//addOpenLong(28710, OVER, 28710, QTY, ONCE);//<--
		//<--sync
		//<-- 
		
		//addOpenLong(27510, OVER, 27310, QTY, RR);
		//addOpenLong(27410, OVER, 27210, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 27301  ------- short  ↓↓↓↓  **/
		addCloseLong(27310, UNDER, 27510, QTY, RR);	
		addCloseLong(27210, UNDER, 27410, QTY, RR);	
		addCloseLong(27110, UNDER, 27310, QTY, RR);	
		//addCloseLong(27010, UNDER, 27210, QTY, RR);	
		//<--sync
		//addCloseLong(28060, UNDER, 28060, QTY2, ONCE);//<--
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void createOpenLong() throws Exception {
		//createOpenLong(27210, UNDER, 27010, QTY, 27210, RR);
		createOpenLong(27210, UNDER, 27010, QTY, 27160, RR);
		createOpenLong(27110, UNDER, 26910, QTY, 26960, ONCE);
		createOpenLong(27010, UNDER, 26810, QTY, 26910, RR);
		createOpenLong(26910, UNDER, 26710, QTY, 26760, ONCE);
		createOpenLong(26810, UNDER, 26610, QTY, 26710, RR);
		createOpenLong(26710, UNDER, 26510, QTY, 26560, ONCE);
		
		
		createOpenLong(26610, UNDER, 26410, QTY, 26510, RR);
		createOpenLong(26410, UNDER, 26210, QTY, 26310, RR);
		createOpenLong(26210, UNDER, 26010, QTY, 26110, RR);
		
		createOpenLong(26110, UNDER, 25910, QTY, 25960, ONCE);
		createOpenLong(26010, UNDER, 25810, QTY, 25910, RR);
		longStopLoss(25710, QTY2);
		createOpenLong(25810, UNDER, 25610, QTY, 25710, RR);
		createOpenLong(25610, UNDER, 25410, QTY, 25510, RR);
		createOpenLong(25410, UNDER, 25210, QTY, 25310, RR);
		longStopLoss(25110, QTY2);
		createOpenLong(25210, UNDER, 25010, QTY, 25110, RR);
		
		
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
