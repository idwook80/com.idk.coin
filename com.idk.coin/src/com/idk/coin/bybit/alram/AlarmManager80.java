package com.idk.coin.bybit.alram;

import java.util.ArrayList;

import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.AlarmManager;
import com.idk.coin.bybit.model.Position;

public class AlarmManager80 extends AlarmManager {
	public AlarmManager80(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager80(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		setDefault_qty(0.15);
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
	  
	}
	public void run() {
		while(is_run) {
			try {
				LOG.info(current_price + " , "  + this.getSize() + "  : " + this.getClass().getName());
				ArrayList<Position> ps = PositionRest.getActiveMyPosition(user.getApi_key(),user.getApi_secret(), symbol);
				double balance 		 =   WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
				Position buy =  Position.getPosition(ps, symbol, "Buy");
				Position sell = Position.getPosition(ps, symbol, "Sell");
				LOG.info(buy.getSize() + " , [" +  String.format("%.2f",(buy.getSize()/QTY)/10) + "]:" 
						 + "[" +  String.format("%.2f", (sell.getSize()/QTY)/10)+"] , "+ sell.getSize()
				 		+" , ["+String.format("%.2f",balance) + "]");
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
		shortStopLoss(23000, QTY2);
		createOpenShort(22710, OVER, 22910, QTY, 22810, RR);
		createOpenShort(22510, OVER, 22710, QTY, 22610, RR);
		createOpenShort(22310, OVER, 22510, QTY, 22410, RR);
		createOpenShort(22110, OVER, 22310, QTY, 22210, RR);
		createOpenShort(21910, OVER, 22110, QTY, 22010, RR);
		shortStopLoss(22750, QTY2);
		shortStopLoss(22500, QTY2);
		shortStopLoss(22250, QTY2);
		shortStopLoss(22000, QTY2);
		
		createOpenShort(21710, OVER, 21910, QTY, 21810, RR);
		createOpenShort(21510, OVER, 21710, QTY, 21610, RR);
		createOpenShort(21310, OVER, 21510, QTY, 21410, RR);
		createOpenShort(21110, OVER, 21310, QTY, 21210, RR);
		createOpenShort(20910, OVER, 21110, QTY, 21010, RR);
		shortStopLoss(21750, QTY3);
		shortStopLoss(21500, QTY3);
		shortStopLoss(21250, QTY3);
		shortStopLoss(21000, QTY3);
		
		
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<[SHORT]<----------------------------
/**	############################################### **/
	public void setShort() throws Exception {
		
		addCloseShort(20910, OVER, 20810, QTY, RR);
		addCloseShort(20760, OVER, 20610, QTY, RR);
		addCloseShort(20710, OVER, 20660, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		//addOpenShort(20660, UNDER, 20710, QTY, RR);
		addOpenShort(20510, UNDER, 20610, QTY, RR);
		addOpenShort(20410, UNDER, 20510, QTY, RR);
		addOpenShort(20310, UNDER, 20410, QTY, RR);
		addOpenShort(20210, UNDER, 20310, QTY, RR);
		
		addOpenShort(20110, UNDER, 20210, QTY, RR);
		addOpenShort(20010, UNDER, 20110, QTY, RR);
		addOpenShort(19910, UNDER, 20010, QTY, RR);
		
		
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void createCloseShort() throws Exception {
		
		createCloseShort(20010, UNDER, 19810, QTY, 19910, RR);
		createCloseShort(19910, UNDER, 19710, QTY, 19810, RR);
		createCloseShort(19810, UNDER, 19610, QTY, 19710, RR);
		createCloseShort(19710, UNDER, 19510, QTY, 19610, RR);
		
		createCloseShort(19610, UNDER, 19410, QTY, 19510, RR);
		createCloseShort(19510, UNDER, 19310, QTY, 19410, RR);
		createCloseShort(19410, UNDER, 19210, QTY, 19310, RR);
		createCloseShort(19310, UNDER, 19110, QTY, 19210, RR);
		createCloseShort(19210, UNDER, 19010, QTY, 19110, RR);

		
		createCloseShort(19110, UNDER, 18910, QTY, 19010, RR);
		createCloseShort(18910, UNDER, 18710, QTY, 18810, RR);
		createCloseShort(18710, UNDER, 18510, QTY, 18610, RR);
		createCloseShort(18510, UNDER, 18310, QTY, 18410, RR);
		createCloseShort(18310, UNDER, 18110, QTY, 18310, RR);
		
		createCloseShort(18110, UNDER, 17910, QTY, 18110, RR);
		createCloseShort(17910, UNDER, 17710, QTY, 17910, RR);
		createCloseShort(17710, UNDER, 17510, QTY, 17710, RR);
		createCloseShort(17510, UNDER, 17310, QTY, 17510, RR);
		createCloseShort(17310, UNDER, 17110, QTY, 17310, RR);
		
		createCloseShort(17110, UNDER, 16910, QTY, 17110, RR);
		createCloseShort(16910, UNDER, 16710, QTY, 16910, RR);
		createCloseShort(16710, UNDER, 16510, QTY, 16710, RR);
		createCloseShort(16510, UNDER, 16310, QTY, 16510, RR);
		createCloseShort(16310, UNDER, 16110, QTY, 16310, RR);
	}
	
	
/**   ############### LONG SETTINGS ####################  **/
	public void createCloseLong() throws Exception {
		createCloseLong(22810, OVER, 23010, QTY, 22910, RR);
		createCloseLong(22610, OVER, 22810, QTY, 22710, RR);
		createCloseLong(22410, OVER, 22610, QTY, 22510, RR);
		createCloseLong(22210, OVER, 22410, QTY, 22310, RR);
		createCloseLong(22010, OVER, 22210, QTY, 22110, RR);
		
		createCloseLong(21810, OVER, 22010, QTY, 21910, RR);
		createCloseLong(21610, OVER, 21810, QTY, 21710, RR);
		createCloseLong(21410, OVER, 21610, QTY, 21510, RR);
		createCloseLong(21210, OVER, 21410, QTY, 21310, RR);
		createCloseLong(21010, OVER, 21210, QTY, 21110, RR);
		
		createCloseLong(20810, OVER, 21010, QTY, 20910, RR);
		
	}
/**	#######################################[LONG]##### **/
/** ######### Long Order exists  Start ####[LONG]### **///<[LONG]<----------------------------
/**	#######################################[LONG]### **/
	public void setLong() throws Exception {
		
		addOpenLong(20810, OVER, 20710, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		addCloseLong(20510, UNDER, 20610, QTY, RR);
		addCloseLong(20310, UNDER, 20410, QTY, RR);
		
		addCloseLong(20110, UNDER, 20210, QTY, RR);
		addCloseLong(20010, UNDER, 20310, QTY, ONCE);
		addCloseLong(19910, UNDER, 20010, QTY, RR);
		
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void createOpenLong() throws Exception {
		createOpenLong(20010, UNDER, 19810, QTY, 20010, ONCE);
		createOpenLong(19910, UNDER, 19710, QTY, 19810, RR);
		createOpenLong(19810, UNDER, 19610, QTY, 19810, ONCE);
		createOpenLong(19710, UNDER, 19510, QTY, 19610, RR);
		
		
		createOpenLong(19610, UNDER, 19410, QTY, 19510, RR);
		createOpenLong(19510, UNDER, 19310, QTY, 19410, RR);
		createOpenLong(19410, UNDER, 19210, QTY, 19310, RR);
		createOpenLong(19310, UNDER, 19110, QTY, 19210, RR);
		createOpenLong(19210, UNDER, 19010, QTY, 19110, RR);
		
		longStopLoss(19250, QTY);
		longStopLoss(19000, QTY);
		
		createOpenLong(19010, UNDER, 18810, QTY, 18910, RR);
		createOpenLong(18810, UNDER, 18610, QTY, 18710, RR);
		createOpenLong(18610, UNDER, 18410, QTY, 18510, RR);
		createOpenLong(18410, UNDER, 18210, QTY, 18310, RR);
		createOpenLong(18210, UNDER, 18010, QTY, 18110, RR);
		
		longStopLoss(18750, QTY2);
		longStopLoss(18500, QTY2);
		longStopLoss(18250, QTY3);
		longStopLoss(18000, QTY3);
		
		createOpenLong(18010, UNDER, 17810, QTY, 17910, RR);
		createOpenLong(17810, UNDER, 17610, QTY, 17710, RR);
		createOpenLong(17610, UNDER, 17410, QTY, 17510, RR);
		createOpenLong(17410, UNDER, 17210, QTY, 17310, RR);
		createOpenLong(17210, UNDER, 17010, QTY, 17110, RR);
		longStopLoss(17750, QTY3);
		longStopLoss(17500, QTY3);
		longStopLoss(17250, QTY3);
		longStopLoss(17000, QTY3);
		
		createOpenLong(17010, UNDER, 16810, QTY, 16910, RR);
		createOpenLong(16810, UNDER, 16610, QTY, 16710, RR);
		createOpenLong(16610, UNDER, 16410, QTY, 16510, RR);
		createOpenLong(16410, UNDER, 16210, QTY, 16310, RR);
		createOpenLong(16210, UNDER, 16010, QTY, 16110, RR);
		longStopLoss(16750, QTY3);
		longStopLoss(16500, QTY3);
		longStopLoss(16250, QTY3);
		longStopLoss(16000, QTY3);
		
	}
	
	
}
