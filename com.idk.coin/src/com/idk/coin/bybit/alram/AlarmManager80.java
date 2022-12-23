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
		/*shortStopLoss(23000, QTY2);
		
		shortStopLoss(22750, QTY);
		shortStopLoss(22500, QTY2);
		shortStopLoss(22250, QTY2);
		shortStopLoss(22000, QTY);
		createOpenShort(22710, OVER, 22910, QTY, 22710, RR);
		createOpenShort(22510, OVER, 22710, QTY, 22510, RR);
		createOpenShort(22310, OVER, 22510, QTY, 22310, RR);
		createOpenShort(22110, OVER, 22310, QTY, 22110, RR);
		createOpenShort(21910, OVER, 22110, QTY, 21910, RR);
		
		shortStopLoss(21750, QTY);
		shortStopLoss(21500, QTY2);
		shortStopLoss(21250, QTY2);
		shortStopLoss(21000, QTY);
		
		createOpenShort(21710, OVER, 21910, QTY, 21710, RR);
		createOpenShort(21510, OVER, 21710, QTY, 21510, RR);
		createOpenShort(21310, OVER, 21510, QTY, 21310, RR);
		createOpenShort(21110, OVER, 21310, QTY, 21110, RR);
		createOpenShort(20910, OVER, 21110, QTY, 20910, RR);*/
		
		createOpenShort(20710, OVER, 20910, QTY, 20710, RR);
		createOpenShort(20510, OVER, 20710, QTY, 20510, RR);
		createOpenShort(20310, OVER, 20510, QTY, 20310, RR);
		createOpenShort(20110, OVER, 20310, QTY, 20110, RR);
		createOpenShort(19910, OVER, 20110, QTY, 20010, RR);
		createOpenShort(19810, OVER, 20010, QTY, 19910, RR);
		shortStopLoss(20750, QTY);
		shortStopLoss(20500, QTY2);
		shortStopLoss(20250, QTY2);
		shortStopLoss(20000, QTY);
		
		createOpenShort(19710, OVER, 19910, QTY, 19710, RR);
		createOpenShort(19510, OVER, 19710, QTY, 19510, RR);
		//createOpenShort(19310, OVER, 19510, QTY, 19310, RR);
		createOpenShort(19110, OVER, 19310, QTY, 19110, RR);
		createOpenShort(18910, OVER, 19110, QTY, 18910, RR);
		shortStopLoss(19750, QTY);
		shortStopLoss(19500, QTY2);
		shortStopLoss(19250, QTY);
		shortStopLoss(19000, QTY2);
		
		createOpenShort(18710, OVER, 18910, QTY, 18710, RR);
		createOpenShort(18510, OVER, 18710, QTY, 18510, RR);
		createOpenShort(18310, OVER, 18510, QTY, 18310, RR);
		createOpenShort(18110, OVER, 18310, QTY, 18110, RR);
		createOpenShort(17910, OVER, 18110, QTY, 18010, RR);
		shortStopLoss(18750, QTY2);
		shortStopLoss(18500, QTY2);
		shortStopLoss(18250, QTY);
		
		createOpenShort(17710, OVER, 17910, QTY, 17810, RR);
		createOpenShort(17510, OVER, 17710, QTY, 17610, RR);
		
		createOpenShort(17310, OVER, 17510, QTY, 17410, RR);
		createOpenShort(17210, OVER, 17410, QTY, 17310, RR);
		createOpenShort(17110, OVER, 17310, QTY, 17210, RR);
		createOpenShort(17010, OVER, 17210, QTY, 17110, RR);
		createOpenShort(16910, OVER, 17110, QTY, 17010, RR);
		
		//Short First
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<[SHORT]<----------------------------
/**	################## [idwook80] ###################### **/
	public void setShort() throws Exception {
		
		addCloseShort(17010, OVER, 16910, QTY, RR);
		addCloseShort(16910, OVER, 16710, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 17506 2.2  ------- long ↓↓↓↓  **/
		//addOpenShort(16710, UNDER, 16910, QTY, RR);
		addOpenShort(16610, UNDER, 16810, QTY, RR);
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void createCloseShort() throws Exception {
		createCloseShort(16710, UNDER, 16510, QTY, 16710, RR);
		createCloseShort(16510, UNDER, 16310, QTY, 16510, RR);
		createCloseShort(16310, UNDER, 16110, QTY, 16310, RR);
		
		createCloseShort(16110, UNDER, 15910, QTY, 16110, RR);
		//createCloseShort(16010, UNDER, 15810, QTY, 16010, RR);
		createCloseShort(15910, UNDER, 15710, QTY, 15910, RR);
		//createCloseShort(15710, UNDER, 15510, QTY, 15710, RR);
		createCloseShort(15510, UNDER, 15310, QTY, 15510, RR);
		//createCloseShort(15310, UNDER, 15110, QTY, 15310, RR);
		
		createCloseShort(15110, UNDER, 14910, QTY, 15110, RR);
		//createCloseShort(14910, UNDER, 14710, QTY, 14910, RR);
		createCloseShort(14710, UNDER, 14510, QTY, 14710, RR);
		//createCloseShort(14510, UNDER, 14310, QTY, 14510, RR);
		createCloseShort(14310, UNDER, 14110, QTY, 14310, RR);
		
		//createCloseShort(14110, UNDER, 13910, QTY, 14110, RR);
		createCloseShort(13910, UNDER, 13710, QTY, 13910, RR);
		//createCloseShort(13710, UNDER, 13510, QTY, 13710, RR);
		createCloseShort(13510, UNDER, 13310, QTY, 13510, RR);
		//createCloseShort(13310, UNDER, 13110, QTY, 13310, RR);
		
		createCloseShort(13110, UNDER, 12910, QTY, 13110, RR);
		//createCloseShort(12910, UNDER, 12710, QTY, 12910, RR);
		createCloseShort(12710, UNDER, 12510, QTY, 12710, RR);
		//createCloseShort(12510, UNDER, 12310, QTY, 12510, RR);
		createCloseShort(12310, UNDER, 12110, QTY, 12310, RR);
		
		//createCloseShort(12110, UNDER, 11910, QTY, 12110, RR);
		createCloseShort(11910, UNDER, 11710, QTY, 11910, RR);
		//createCloseShort(11710, UNDER, 11510, QTY, 11710, RR);
		createCloseShort(11510, UNDER, 11310, QTY, 11510, RR);
		//createCloseShort(11310, UNDER, 11110, QTY, 11310, RR);
		
		createCloseShort(11110, UNDER, 10910, QTY, 11110, RR);
		//createCloseShort(10910, UNDER, 10710, QTY, 10910, RR);
		createCloseShort(10710, UNDER, 10510, QTY, 10710, RR);
		//createCloseShort(10510, UNDER, 10310, QTY, 10510, RR);
		createCloseShort(10310, UNDER, 10110, QTY, 10310, RR);
		
	}
	
	
	
	
	
/**   ############### LONG SETTINGS ####################  **/
/**   ############### LONG SETTINGS ####################  **/	
/**   ############### LONG SETTINGS ####################  **/
	public void createCloseLong() throws Exception {
		
		
		//createCloseLong(22810, OVER, 23010, QTY, 22810, RR);
		createCloseLong(22610, OVER, 22810, QTY, 22610, RR);
		//createCloseLong(22410, OVER, 22610, QTY, 22410, RR);
		createCloseLong(22210, OVER, 22410, QTY, 22210, RR);
		//createCloseLong(22010, OVER, 22210, QTY, 22010, RR);
		createCloseLong(21810, OVER, 22010, QTY, 21810, RR);
		
		//createCloseLong(21610, OVER, 21810, QTY, 21610, RR);
		createCloseLong(21410, OVER, 21610, QTY, 21410, RR);
		//createCloseLong(21210, OVER, 21410, QTY, 21210, RR);
		createCloseLong(21010, OVER, 21210, QTY, 21010, RR);
		//createCloseLong(20810, OVER, 21010, QTY, 20810, RR);
		
		createCloseLong(20610, OVER, 20810, QTY, 20610, RR);
		//createCloseLong(20410, OVER, 20610, QTY, 20410, RR);
		createCloseLong(20210, OVER, 20410, QTY, 20210, RR);
		//createCloseLong(20010, OVER, 20210, QTY, 20010, RR);
		createCloseLong(19810, OVER, 20010, QTY, 19810, RR);
		
		//createCloseLong(19610, OVER, 19810, QTY, 19610, RR);
		createCloseLong(19410, OVER, 19610, QTY, 19410, RR);
		//createCloseLong(19210, OVER, 19410, QTY, 19210, RR);
		createCloseLong(19010, OVER, 19210, QTY, 19010, RR);
		//createCloseLong(18810, OVER, 19010, QTY, 18810, RR);
		
		createCloseLong(18610, OVER, 18810, QTY, 18610, RR);
		//createCloseLong(18410, OVER, 18610, QTY, 18410, RR);
		createCloseLong(18210, OVER, 18410, QTY, 18210, RR);
		//createCloseLong(18010, OVER, 18210, QTY, 18010, RR);
		createCloseLong(17810, OVER, 18010, QTY, 17810, RR);
		
		createCloseLong(17610, OVER, 17810, QTY, 17710, RR);
		createCloseLong(17410, OVER, 17610, QTY, 17510, RR);
		createCloseLong(17310, OVER, 17510, QTY, 17410, RR);
		createCloseLong(17210, OVER, 17410, QTY, 17310, RR);
		createCloseLong(17110, OVER, 17310, QTY, 17260, RR);
		
		createCloseLong(17010, OVER, 17260, QTY, 17210, ONCE);
		createCloseLong(17010, OVER, 17210, QTY, 17160, RR);
		
		createCloseLong(16910, OVER, 17160, QTY, 17110, ONCE);
		createCloseLong(16910, OVER, 17110, QTY, 17060, RR);
		
		
	}
/**	#######################################[LONG]##### **/
/** ######### Long Order exists  Start ####[LONG]### **///<[LONG]<----------------------------
/**	#######################################[LONG]## **/
	public void setLong() throws Exception {
		
		addOpenLong(17060, OVER, 17010, QTY, RR);
		addOpenLong(17010, OVER, 16960, QTY, RR);
		addOpenLong(17010, OVER, 16860, QTY, ONCE);
		addOpenLong(16960, OVER, 16910, QTY, RR);
		addOpenLong(16910, OVER, 16860, QTY, RR);
		addOpenLong(16910, OVER, 16760, QTY, ONCE);
		addOpenLong(16860, OVER, 16810, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 17207  2.1-------  ↓↓↓↓ long **/
		//addCloseLong(16810, UNDER, 16860, QTY, RR);
		addCloseLong(16760, UNDER, 16810, QTY, RR);
		addCloseLong(16710, UNDER, 16760, QTY, RR);
		addCloseLong(16660, UNDER, 16710, QTY, RR);
		addCloseLong(16610, UNDER, 16660, QTY, RR);
		
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void createOpenLong() throws Exception {
		//createOpenLong(16810, UNDER, 16660, QTY, 16710, RR);
		//createOpenLong(16810, UNDER, 16610, QTY, 16660, RR);
		
		createOpenLong(16710, UNDER, 16560, QTY, 16610, RR);
		createOpenLong(16710, UNDER, 16510, QTY, 16560, RR);
		
		createOpenLong(16610, UNDER, 16460, QTY, 16510, RR);
		createOpenLong(16610, UNDER, 16410, QTY, 16460, RR);
		
		
		createOpenLong(16510, UNDER, 16360, QTY, 16410, RR);
		createOpenLong(16510, UNDER, 16310, QTY, 16360, RR);
		createOpenLong(16410, UNDER, 16210, QTY, 16310, RR);
		createOpenLong(16310, UNDER, 16110, QTY, 16210, RR);
		createOpenLong(16210, UNDER, 16010, QTY, 16110, RR);
		
		createOpenLong(16010, UNDER, 15810, QTY, 16010, RR);
		createOpenLong(15810, UNDER, 15610, QTY, 15810, RR);
		createOpenLong(15610, UNDER, 15410, QTY, 15610, RR);
		createOpenLong(15410, UNDER, 15210, QTY, 15410, RR);
		createOpenLong(15210, UNDER, 15010, QTY, 15210, RR);
		longStopLoss(15750, QTY2);
		longStopLoss(15500, QTY);
		longStopLoss(15250, QTY2);
		longStopLoss(15000, QTY);
		
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
		
		/*longStopLoss(11750, QTY);
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
