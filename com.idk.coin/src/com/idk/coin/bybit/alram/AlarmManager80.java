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
		shortStopLoss(26750, QTY2);
		shortStopLoss(26500, QTY);
		shortStopLoss(26250, QTY2);
		shortStopLoss(26000, QTY);
		//createOpenShort(26710, OVER, 26910, QTY, 26810, RR);
		//createOpenShort(26510, OVER, 26710, QTY, 26610, RR);
		createOpenShort(26310, OVER, 26510, QTY, 26410, RR);
		//createOpenShort(26110, OVER, 26310, QTY, 26210, RR);
		//createOpenShort(25910, OVER, 26110, QTY, 26010, RR);
		
		shortStopLoss(25750, QTY);
		shortStopLoss(25500, QTY2);
		shortStopLoss(25250, QTY);
		shortStopLoss(25000, QTY2);
		//createOpenShort(25710, OVER, 25910, QTY, 25810, RR);
		//createOpenShort(25510, OVER, 25710, QTY, 25610, RR);
		createOpenShort(25310, OVER, 25510, QTY, 25410, RR);
		//createOpenShort(25110, OVER, 25310, QTY, 25210, RR);
		//createOpenShort(24910, OVER, 25110, QTY, 25010, RR);
	
		shortStopLoss(24750, QTY);
		shortStopLoss(24500, QTY2);
		shortStopLoss(24250, QTY);
		//createOpenShort(24710, OVER, 24910, QTY, 24810, RR);
		//createOpenShort(24510, OVER, 24710, QTY, 24610, RR);
		createOpenShort(24310, OVER, 24510, QTY, 24310, RR);
		//createOpenShort(24110, OVER, 24310, QTY, 24210, RR);
		//createOpenShort(23910, OVER, 24110, QTY, 24010, RR);
		
		createOpenShort(23710, OVER, 23910, QTY, 23810, RR);
		addCloseShort(23910, OVER, 23860, QTY, ONCE);
		createOpenShort(23510, OVER, 23710, QTY, 23610, RR);
		addCloseShort(23710, OVER, 23660, QTY, ONCE);
		//createOpenShort(23310, OVER, 23510, QTY, 23410, RR);
		//createOpenShort(23110, OVER, 23310, QTY, 23110, RR);
		//Short First
		
		createOpenShort(23410, OVER, 23610, QTY, 23510, RR);
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<[SHORT]<----------------------------
/**	################## [idwook80] ###################### **/
	public void setShort() throws Exception {
		//addCloseShort(23610, OVER, 23510, QTY, RR);
		addCloseShort(23610, OVER, 23560, QTY, ONCE);
		
		addCloseShort(23510, OVER, 23410, QTY, RR);
		addCloseShort(23510, OVER, 23460, QTY, ONCE);
		
		addCloseShort(23460, OVER, 23310, QTY, RR);
		
		addCloseShort(23410, OVER, 23260, QTY, RR);
		/** ↑↑↑↑ -------  Price Line 22087  ------- long ↓↓↓↓  **/
		
		//addOpenShort(23260, UNDER, 23410, QTY, RR);
	
		//##
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void createCloseShort() throws Exception {
		addOpenShort(23110, UNDER, 23310, QTY, RR);	
		
		addOpenShort(22910, UNDER, 23110, QTY, RR);	
		addOpenShort(22710, UNDER, 22910, QTY, RR);
		addOpenShort(22510, UNDER, 22710, QTY, RR);
		addOpenShort(22310, UNDER, 22510, QTY, RR);
		
		///createCloseShort(23110, UNDER, 22910, QTY, 23110, RR);
		//createCloseShort(22910, UNDER, 22710, QTY, 22910, RR);
		//createCloseShort(22710, UNDER, 22510, QTY, 22710, RR);
		//createCloseShort(22510, UNDER, 22310, QTY, 22510, RR);
		createCloseShort(22310, UNDER, 22110, QTY, 22310, RR);
		
		addOpenShort(21910, UNDER, 22010, QTY, RR);
		addOpenShort(21510, UNDER, 21710, QTY, RR);
		addOpenShort(21110, UNDER, 21210, QTY, RR);
		//createCloseShort(22110, UNDER, 21910, QTY, 22010, RR);
		createCloseShort(21910, UNDER, 21710, QTY, 21810, RR);
		//createCloseShort(21710, UNDER, 21510, QTY, 21710, RR);
		createCloseShort(21510, UNDER, 21310, QTY, 21410, RR);
		//createCloseShort(21310, UNDER, 21110, QTY, 21210, RR);
		
		
		addOpenShort(20710, UNDER, 20910, QTY, RR);
		addOpenShort(20310, UNDER, 20510, QTY, RR);
		createCloseShort(21110, UNDER, 20910, QTY, 21010, RR);
		//createCloseShort(20910, UNDER, 20710, QTY, 20810, RR);
		createCloseShort(20710, UNDER, 20510, QTY, 20610, RR);
		//createCloseShort(20510, UNDER, 20310, QTY, 20410, RR);
		createCloseShort(20310, UNDER, 20110, QTY, 20210, RR);
		
		addOpenShort(19910, UNDER, 20110, QTY, RR);
		addOpenShort(19510, UNDER, 19710, QTY, RR);
		addOpenShort(19110, UNDER, 19310, QTY, RR);
		//createCloseShort(20110, UNDER, 19910, QTY, 20010, RR);
		createCloseShort(19910, UNDER, 19710, QTY, 19810, RR);
		//createCloseShort(19710, UNDER, 19510, QTY, 19610, RR);
		createCloseShort(19510, UNDER, 19310, QTY, 19410, RR);
		//createCloseShort(19310, UNDER, 19110, QTY, 19210, RR);
		
		createCloseShort(19110, UNDER, 18910, QTY, 19010, RR);
		//createCloseShort(18910, UNDER, 18710, QTY, 18810, RR);
		createCloseShort(18710, UNDER, 18510, QTY, 18610, RR);
		//createCloseShort(18510, UNDER, 18310, QTY, 18410, RR);
		createCloseShort(18310, UNDER, 18110, QTY, 18210, RR);
		
		createCloseShort(18110, UNDER, 17910, QTY, 18010, RR);
		//createCloseShort(18010, UNDER, 17810, QTY, 17910, RR);
		createCloseShort(17910, UNDER, 17710, QTY, 17810, RR);
		//createCloseShort(17710, UNDER, 17510, QTY, 17610, RR);
		createCloseShort(17510, UNDER, 17310, QTY, 17410, RR);
		//createCloseShort(17310, UNDER, 17110, QTY, 17210, RR);

		
	}
	
	
	
	
	
/**   ############### LONG SETTINGS ####################  **/
/**   ############### LONG SETTINGS ####################  **/	
/**   ############### LONG SETTINGS ####################  **/
	public void createCloseLong() throws Exception {
		
		//createCloseLong(26810, OVER, 27010, QTY, 26910, RR);
		createCloseLong(26610, OVER, 26810, QTY, 26710, RR);
		//createCloseLong(26410, OVER, 26610, QTY, 26510, RR);
		createCloseLong(26210, OVER, 26410, QTY, 26310, RR);
		//createCloseLong(26010, OVER, 26210, QTY, 26110, RR);
		addOpenLong(27010, OVER, 26910, QTY, RR);
		addOpenLong(26610, OVER, 26510, QTY, RR);
		addOpenLong(26210, OVER, 26110, QTY, RR);
		
		//createCloseLong(25810, OVER, 26010, QTY, 25910, RR);
		//createCloseLong(25610, OVER, 25810, QTY, 25710, RR);
		//createCloseLong(25410, OVER, 25610, QTY, 25510, RR);
		//createCloseLong(25210, OVER, 25410, QTY, 25310, RR);
		//createCloseLong(25010, OVER, 25210, QTY, 25110, RR);
		addOpenLong(25610, OVER, 25510, QTY, RR);
		addOpenLong(25410, OVER, 25310, QTY, RR);
		addOpenLong(25210, OVER, 25110, QTY, RR);
		addOpenLong(25010, OVER, 24910, QTY, RR);
		
		//createCloseLong(24810, OVER, 25010, QTY, 24810, RR);
		//createCloseLong(24610, OVER, 24810, QTY, 24710, RR);
		//createCloseLong(24410, OVER, 24610, QTY, 24410, RR);
		//createCloseLong(24210, OVER, 24410, QTY, 24210, RR);
		//createCloseLong(24010, OVER, 24210, QTY, 24010, RR);
		addOpenLong(24810, OVER, 24710, QTY, RR);
		addOpenLong(24610, OVER, 24410, QTY, RR);
		addOpenLong(24410, OVER, 24210, QTY, RR);
		addOpenLong(24210, OVER, 24010, QTY, RR);
		
		
		//createCloseLong(23810, OVER, 24010, QTY, 23910, RR);
		//createCloseLong(23610, OVER, 23810, QTY, 23710, RR);
		//createCloseLong(23410, OVER, 23610, QTY, 23510, RR);
		//createCloseLong(23210, OVER, 23410, QTY, 23310, RR);
		addOpenLong(24010, OVER, 23810, QTY, RR);
	
		//addOpenLong(23410, OVER, 23210, QTY, RR);
		
		
	}
/**	#######################################[LONG]##### **/
/** ######### Long Order exists  Start ####[LONG]### **///<[LONG]<----------------------------
/**	#######################################[LONG]## **/
	public void setLong() throws Exception {
		
		addOpenLong(23810, OVER, 23710, QTY, RR); //<--
		addOpenLong(23610, OVER, 23410, QTY, RR); 
		
		addOpenLong(23410, OVER, 23260, QTY, ONCE); 
		//addOpenLong(23310, OVER, 23210, QTY, RR); 

		/** ↑↑↑↑ -------  Price Line 21798  ------- short  ↓↓↓↓  **/
		
		
		addCloseLong(23210, UNDER, 23260, QTY, ONCE);
		addCloseLong(23210, UNDER, 23310, QTY, RR);
		
		addCloseLong(23110, UNDER, 23160, QTY, ONCE);
		addCloseLong(23110, UNDER, 23210, QTY, RR);
		
		addCloseLong(23010, UNDER, 23060, QTY, ONCE);
		addCloseLong(23010, UNDER, 23110, QTY, RR);
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void createOpenLong() throws Exception {
		
		
		//addCloseLong(23010, UNDER, 23060, QTY, ONCE);
		//createOpenLong(23210, UNDER, 23010, QTY, 23110, RR);
		addCloseLong(22910, UNDER, 23960, QTY, ONCE);
		createOpenLong(23110, UNDER, 22910, QTY, 23010, RR);
		
		createOpenLong(23010, UNDER, 22810, QTY, 22910, RR);
		createOpenLong(22810, UNDER, 22610, QTY, 22710, RR);
		createOpenLong(22610, UNDER, 22410, QTY, 22510, RR);
		createOpenLong(22410, UNDER, 22210, QTY, 22310, RR);
		createOpenLong(22210, UNDER, 22010, QTY, 22110, RR);
		//longStopLoss(22750, QTY2);
		//longStopLoss(22500, QTY);
		//longStopLoss(22250, QTY2);
		longStopLoss(22000, QTY2);
		
		//createOpenLong(22010, UNDER, 21810, QTY, 21910, RR);
		//createOpenLong(21810, UNDER, 21610, QTY, 21710, RR);
		createOpenLong(21610, UNDER, 21410, QTY, 21510, RR);
		//createOpenLong(21410, UNDER, 21210, QTY, 21310, RR);
		//createOpenLong(21210, UNDER, 21010, QTY, 21110, RR);
		longStopLoss(21750, QTY);
		longStopLoss(21500, QTY2);
		longStopLoss(21250, QTY);
		longStopLoss(21000, QTY2);
		
		
		//createOpenLong(21010, UNDER, 20810, QTY, 20910, RR);
		//createOpenLong(20810, UNDER, 20610, QTY, 20710, RR);
		createOpenLong(20610, UNDER, 20410, QTY, 20510, RR);
		//createOpenLong(20410, UNDER, 20210, QTY, 20310, RR);
		//createOpenLong(20210, UNDER, 20010, QTY, 20110, RR);
		longStopLoss(20750, QTY);
		longStopLoss(20500, QTY2);
		longStopLoss(20250, QTY);
		longStopLoss(20000, QTY2);
		
		createOpenLong(20010, UNDER, 19810, QTY, 19910, RR);
		createOpenLong(19810, UNDER, 19610, QTY, 19710, RR);
		createOpenLong(19610, UNDER, 19410, QTY, 19510, RR);
		createOpenLong(19410, UNDER, 19210, QTY, 19310, RR);
		createOpenLong(19210, UNDER, 19010, QTY, 19110, RR);
		longStopLoss(19750, QTY2);
		longStopLoss(19500, QTY);
		longStopLoss(19250, QTY2);
		longStopLoss(19000, QTY);
		
		createOpenLong(19010, UNDER, 18810, QTY, 18910, RR);
		createOpenLong(18810, UNDER, 18610, QTY, 18710, RR);
		createOpenLong(18610, UNDER, 18410, QTY, 18510, RR);
		createOpenLong(18410, UNDER, 18210, QTY, 18310, RR);
		createOpenLong(18210, UNDER, 18010, QTY, 18110, RR);
		longStopLoss(18750, QTY);
		longStopLoss(18500, QTY);
		longStopLoss(18250, QTY);
		longStopLoss(18000, QTY2);
		/*
		createOpenLong(18010, UNDER, 17810, QTY, 18010, RR);
		createOpenLong(17810, UNDER, 17610, QTY, 17810, RR);
		createOpenLong(17610, UNDER, 17410, QTY, 17610, ONCE);
		createOpenLong(17410, UNDER, 17210, QTY, 17410, RR);
		createOpenLong(17210, UNDER, 17010, QTY, 17110, RR);
		longStopLoss(17750, QTY);
		longStopLoss(17500, QTY2);
		longStopLoss(17250, QTY);
		longStopLoss(17000, QTY2);
		
		createOpenLong(17010, UNDER, 16810, QTY, 16910, RR);
		createOpenLong(16810, UNDER, 16610, QTY, 16710, RR);
		createOpenLong(16610, UNDER, 16410, QTY, 16510, ONCE);
		createOpenLong(16410, UNDER, 16210, QTY, 16310, RR);
		createOpenLong(16210, UNDER, 16010, QTY, 16110, RR);
		longStopLoss(16750, QTY);
		longStopLoss(16500, QTY2);
		longStopLoss(16250, QTY);
		longStopLoss(16000, QTY2);
		
		createOpenLong(16010, UNDER, 15810, QTY, 16010, RR);
		createOpenLong(15810, UNDER, 15610, QTY, 15810, RR);
		createOpenLong(15610, UNDER, 15410, QTY, 15610, ONCE);
		createOpenLong(15410, UNDER, 15210, QTY, 15410, RR);
		createOpenLong(15210, UNDER, 15010, QTY, 15210, RR);
		longStopLoss(15750, QTY);
		longStopLoss(15500, QTY2);
		longStopLoss(15250, QTY);
		longStopLoss(15000, QTY2);
		
		createOpenLong(15010, UNDER, 14810, QTY, 15010, RR);
		createOpenLong(14810, UNDER, 14610, QTY, 14810, RR);
		createOpenLong(14610, UNDER, 14410, QTY, 14610, ONCE);
		createOpenLong(14410, UNDER, 14210, QTY, 14410, RR);
		createOpenLong(14210, UNDER, 14010, QTY, 14210, RR);
		longStopLoss(14750, QTY);
		longStopLoss(14500, QTY2);
		longStopLoss(14250, QTY2);
		longStopLoss(14000, QTY);
		
		createOpenLong(14010, UNDER, 13810, QTY, 14010, RR);
		createOpenLong(13810, UNDER, 13610, QTY, 13810, RR);
		createOpenLong(13610, UNDER, 13410, QTY, 13610, ONCE);
		createOpenLong(13410, UNDER, 13210, QTY, 13410, RR);
		createOpenLong(13210, UNDER, 13010, QTY, 13210, RR);
		longStopLoss(13750, QTY);
		longStopLoss(13500, QTY2);
		longStopLoss(13250, QTY2);
		longStopLoss(13000, QTY);
		
		
		longStopLoss(12750, QTY);
		longStopLoss(12500, QTY2);
		longStopLoss(12250, QTY2);
		longStopLoss(12000, QTY);
		
		createOpenLong(13010, UNDER, 12810, QTY, 13010, RR);
		createOpenLong(12810, UNDER, 12610, QTY, 12810, RR);
		createOpenLong(12610, UNDER, 12410, QTY, 12610, ONCE);
		createOpenLong(12410, UNDER, 12210, QTY, 12410, RR);
		createOpenLong(12210, UNDER, 12010, QTY, 12210, RR); 
		
		longStopLoss(11750, QTY);
		longStopLoss(11500, QTY2);
		longStopLoss(11250, QTY2);
		longStopLoss(11000, QTY);
		
		createOpenLong(12010, UNDER, 11810, QTY, 12010, RR);
		createOpenLong(11810, UNDER, 11610, QTY, 11810, RR);
		createOpenLong(11610, UNDER, 11410, QTY, 11610, ONCE);
		createOpenLong(11410, UNDER, 11210, QTY, 11410, RR);
		createOpenLong(11210, UNDER, 11010, QTY, 11210, RR);
		
		longStopLoss(10750, QTY);
		longStopLoss(10500, QTY2);
		longStopLoss(10250, QTY2);
		longStopLoss(10000, QTY);
		
		createOpenLong(11010, UNDER, 10810, QTY, 11010, RR);
		createOpenLong(10810, UNDER, 10610, QTY, 10810, RR);
		createOpenLong(10610, UNDER, 10410, QTY, 10610, ONCE);
		createOpenLong(10410, UNDER, 10210, QTY, 10410, RR);
		createOpenLong(10210, UNDER, 10010, QTY, 10210, RR);*/
	}
	
	
}
