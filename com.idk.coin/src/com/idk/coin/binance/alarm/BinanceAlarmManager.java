package com.idk.coin.binance.alarm;

import java.util.ArrayList;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.OrderExecution;
import com.idk.coin.bybit.model.Position;
import com.idk.coin.model.AlarmManager;

public class BinanceAlarmManager extends BinanceAlarmsModel {
	public BinanceAlarmManager(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public BinanceAlarmManager(String symbol, String web_id, String web_pw) throws Exception{
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
				LOG.info(current_price + " , "  + this.getSize() + "  : " + this.getClass().getName());
			
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
		
		shortStopLoss(18000, QTY);
		
		createOpenShort(18710, OVER, 18910, QTY, 18710, RR);
		createOpenShort(18510, OVER, 18710, QTY, 18510, RR);
		createOpenShort(18310, OVER, 18510, QTY, 18310, RR);
		createOpenShort(18110, OVER, 18310, QTY, 18110, RR);
		createOpenShort(17910, OVER, 18110, QTY, 17910, RR);
		
		createOpenShort(17710, OVER, 17910, QTY, 17710, RR);
		createOpenShort(17510, OVER, 17710, QTY, 17510, RR);
		createOpenShort(17310, OVER, 17510, QTY, 17310, RR);
		createOpenShort(17110, OVER, 17310, QTY, 17110, RR);
		createOpenShort(16910, OVER, 17110, QTY, 16910, RR);
		
		createOpenShort(16710, OVER, 16910, QTY, 16710, RR);
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<[SHORT]<----------------------------
/**	################## [binance01] ###################### **/
	public void setShort() throws Exception {
		addCloseShort(16710, OVER, 16510, QTY, RR);
		/** ↑↑↑↑ -------  Price Line 16625 1.5 ------- long ↓↓↓↓  **/
		
		addOpenShort(16310, UNDER, 16510, QTY, RR);
		
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void createCloseShort() throws Exception {
		
		//createCloseShort(16710, UNDER, 16510, QTY, 16710, RR);
		//createCloseShort(16510, UNDER, 16310, QTY, 16510, RR);
		createCloseShort(16310, UNDER, 16110, QTY, 16310, RR);
		createCloseShort(16110, UNDER, 15910, QTY, 16110, RR);
		
		createCloseShort(15910, UNDER, 15710, QTY, 15910, RR);
		createCloseShort(15710, UNDER, 15510, QTY, 15710, RR);
		createCloseShort(15510, UNDER, 15310, QTY, 15510, RR);
		createCloseShort(15310, UNDER, 15110, QTY, 15310, RR);
		createCloseShort(15110, UNDER, 14910, QTY, 15110, RR);
		 
	}
	
	
/**   ############### LONG SETTINGS ####################  **/
/**   ############### LONG SETTINGS ####################  **/	
/**   ############### LONG SETTINGS ####################  **/
	public void createCloseLong() throws Exception {
		createCloseLong(18610, OVER, 18810, QTY, 18610, RR);
		createCloseLong(18410, OVER, 18610, QTY, 18410, RR);
		createCloseLong(18210, OVER, 18410, QTY, 18210, RR);
		createCloseLong(18010, OVER, 18210, QTY, 18010, RR);
		createCloseLong(17810, OVER, 18010, QTY, 17810, RR);
		
		createCloseLong(17610, OVER, 17810, QTY, 17610, RR);
		createCloseLong(17410, OVER, 17610, QTY, 17410, RR);
		createCloseLong(17210, OVER, 17410, QTY, 17210, RR);
		createCloseLong(17010, OVER, 17210, QTY, 17010, RR);
		createCloseLong(16810, OVER, 17010, QTY, 16810, RR);
		
		//createCloseLong(16610, OVER, 16810, QTY, 16610, RR);
	}
/**	#######################################[LONG]##### **/
/** ######### Long Order exists  Start ####[LONG]### **///<[LONG]<----------------------------
/**	#######################################[LONG]## **/
	public void setLong() throws Exception {
		addOpenLong(16810, OVER, 16610, QTY, RR);
		addOpenLong(16710, OVER, 16510, QTY, ONCE);
		
		/** ↑↑↑↑ -------  Price Line 16466 2.4 -------  ↓↓↓↓ long **/
		addCloseLong(16410, UNDER, 16610, QTY, RR);
		addCloseLong(16210, UNDER, 16410, QTY, RR);
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void createOpenLong() throws Exception {
		
		//createOpenLong(16610, UNDER, 16410, QTY, 16610, RR);
		//createOpenLong(16410, UNDER, 16210, QTY, 16410, RR);
		createOpenLong(16210, UNDER, 16010, QTY, 16210, RR);
		
		//longStopLoss(15750, QTY);
		longStopLoss(15500, QTY);
		longStopLoss(15250, QTY);
		longStopLoss(15000, QTY);
		
		createOpenLong(16010, UNDER, 15810, QTY, 16010, RR);
		createOpenLong(15810, UNDER, 15610, QTY, 15810, RR);
		createOpenLong(15610, UNDER, 15410, QTY, 15610, RR);
		createOpenLong(15410, UNDER, 15210, QTY, 15410, RR);
		createOpenLong(15210, UNDER, 15010, QTY, 15210, RR);
		
	 
		
	}
	
	public void checkAlarmExecution(OrderExecution execution) {
		LOG.info("★★★★★★★★★★\t Execution Checked " + list.size() + "\t★★★★★★★★★★★★★");
		
		if(list.isEmpty() || !is_run) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() != null) {
					if(execution.getOrder_id().equals(alarm.getParent_order_id())){
						current_price = execution.getPrice();
						list.remove(alarm);
						try {
							alarm.action(user, symbol);
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		if(symbol.equals(execution.getSymbol())) checkAlarmPrice(execution.getPrice());
		
	}
	public void checkAlarmPrice(double price) {
		current_price = price;
		if(list.isEmpty() || !is_run) return;
		synchronized(list) {
			AlarmPrice[] obj = list.toArray(new AlarmPrice[0]);
			for(AlarmPrice alarm : obj) {
				if(alarm.getParent_order_id() == null) {
					if(alarm.compare(price)) {
						LOG.info("found : "+ alarm + " user : " + user);
						list.remove(alarm);
						try {
							alarm.action(user, symbol);
						}catch(Exception e) {
							e.printStackTrace();
						}
						LOG.info("★★★★★★★★★★\t Price Checked " + list.size() + "\t★★★★★★★★★★★★★");
					}
				}
			}
		}
	}
}