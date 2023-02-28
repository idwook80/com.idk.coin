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
		createOpenShort(19710, OVER, 19910, QTY, 19710, RR);
		createOpenShort(19510, OVER, 19710, QTY, 19510, RR);
		createOpenShort(19310, OVER, 19510, QTY, 19310, RR);
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
		createOpenShort(17910, OVER, 18110, QTY, 17910, RR);
		shortStopLoss(18750, QTY);
		shortStopLoss(18500, QTY2);
		shortStopLoss(18250, QTY);
		createOpenShort(17710, OVER, 17910, QTY, 17710, RR);
		createOpenShort(17610, OVER, 17810, QTY, 17610, RR);
		createOpenShort(17510, OVER, 17710, QTY, 17510, RR);
		createOpenShort(17410, OVER, 17610, QTY, 17410, RR);
		createOpenShort(17310, OVER, 17510, QTY, 17310, RR);
		createOpenShort(17210, OVER, 17410, QTY, 17210, RR);
		
		createOpenShort(17110, OVER, 17310, QTY, 17210, RR);
		createOpenShort(17010, OVER, 17210, QTY, 17110, RR);
		createOpenShort(16910, OVER, 17160, QTY, 17060, RR);
		createOpenShort(16910, OVER, 17110, QTY, 17010, RR);
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<[SHORT]<----------------------------
/**	################## [binance01] ###################### **/
	public void setShort() throws Exception {
		addCloseShort(17060, OVER, 16910, QTY, RR);
		addCloseShort(17010, OVER, 16810, QTY, RR);
		
		//addCloseShort(16910, OVER, 16710, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 17068 0.016 ------- long ↓↓↓↓  **/
		addOpenShort(16710, UNDER, 16910, QTY, RR);
		
		addOpenShort(16660, UNDER, 16860, QTY, RR);
		addOpenShort(16610, UNDER, 16810, QTY, RR);
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void createCloseShort() throws Exception {
		createCloseShort(16760, UNDER, 16560, QTY, 16760, RR);
		
		createCloseShort(16710, UNDER, 16510, QTY, 16710, RR);
		createCloseShort(16710, UNDER, 16510, QTY, 16610, RR);
		
		createCloseShort(16610, UNDER, 16460, QTY, 16660, RR);
		createCloseShort(16610, UNDER, 16410, QTY, 16510, RR);
		createCloseShort(16510, UNDER, 16310, QTY, 16410, RR);
		
		createCloseShort(16310, UNDER, 16110, QTY, 16210, RR);
		//createCloseShort(16210, UNDER, 16010, QTY, 16110, RR);
		
		createCloseShort(16110, UNDER, 15910, QTY, 16110, RR);
		createCloseShort(15910, UNDER, 15710, QTY, 15910, RR);
		createCloseShort(15710, UNDER, 15510, QTY, 15710, RR);
		//createCloseShort(15510, UNDER, 15310, QTY, 15510, RR);
		createCloseShort(15310, UNDER, 15110, QTY, 15310, RR);
		//createCloseShort(15110, UNDER, 14910, QTY, 15110, RR);
		
		createCloseShort(14910, UNDER, 14710, QTY, 14910, RR);
		//createCloseShort(14710, UNDER, 14510, QTY, 14710, RR);
		createCloseShort(14510, UNDER, 14310, QTY, 14510, RR);
		//createCloseShort(14310, UNDER, 14110, QTY, 14310, RR);
		createCloseShort(14110, UNDER, 13910, QTY, 14110, RR);
		 
	}
	
	
/**   ############### LONG SETTINGS ####################  **/
/**   ############### LONG SETTINGS ####################  **/	
/**   ############### LONG SETTINGS ####################  **/
	public void createCloseLong() throws Exception {
		
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
		createCloseLong(17510, OVER, 17710, QTY, 17610, RR);
		createCloseLong(17410, OVER, 17610, QTY, 17510, RR);
		createCloseLong(17310, OVER, 17510, QTY, 17410, RR);
		createCloseLong(17210, OVER, 17410, QTY, 17310, RR);
		createCloseLong(17110, OVER, 17310, QTY, 17210, RR);
		createCloseLong(17010, OVER, 17210, QTY, 17110, RR);
		createCloseLong(16910, OVER, 17110, QTY, 17010, RR);
		
		
		
	}
/**	#######################################[LONG]##### **/
/** ######### Long Order exists  Start ####[LONG]### **///<[LONG]<----------------------------
/**	#######################################[LONG]## **/
	public void setLong() throws Exception {
		addOpenLong(17010, OVER, 16910, QTY, RR);
		
		addOpenLong(16910, OVER, 16860, QTY, RR);
		addOpenLong(16860, OVER, 16810, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line 16793 0.026 -------  ↓↓↓↓ long **/
		//addCloseLong(16810, UNDER, 16860, QTY, RR);
		addCloseLong(16760, UNDER, 16810, QTY, RR);
		
		addCloseLong(16710, UNDER, 16810, QTY, ONCE);
		addCloseLong(16710, UNDER, 16760, QTY, RR);
		
		addCloseLong(16660, UNDER, 16710, QTY, RR);
		
		addCloseLong(16635, UNDER, 16685, QTY, RR);
		addCloseLong(16610, UNDER, 16660, QTY, RR);
		
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	
	public void createOpenLong() throws Exception {
		createOpenLong(16760, UNDER, 16585, QTY, 16635, RR);
		createOpenLong(16760, UNDER, 16560, QTY, 16610, RR);
		
		createOpenLong(16710, UNDER, 16535, QTY, 16585, RR);
		createOpenLong(16710, UNDER, 16510, QTY, 16560, RR);
		
		createOpenLong(16610, UNDER, 16460, QTY, 16510, RR);
		createOpenLong(16610, UNDER, 16410, QTY, 16460, RR);
		
		createOpenLong(16410, UNDER, 16210, QTY, 16410, RR);
		createOpenLong(16210, UNDER, 16010, QTY, 16210, RR);
		createOpenLong(16010, UNDER, 15810, QTY, 15910, RR);
		
		longStopLoss(15750, QTY2);
		longStopLoss(15500, QTY);
		longStopLoss(15250, QTY2);
		longStopLoss(15000, QTY);
		createOpenLong(15810, UNDER, 15610, QTY, 15810, RR);
		createOpenLong(15610, UNDER, 15410, QTY, 15610, RR);
		createOpenLong(15410, UNDER, 15210, QTY, 15410, RR);
		createOpenLong(15210, UNDER, 15010, QTY, 15210, RR);
		
		longStopLoss(14750, QTY2);
		longStopLoss(14500, QTY);
		longStopLoss(14250, QTY2);
		longStopLoss(14000, QTY);
		
		createOpenLong(15010, UNDER, 14810, QTY, 15010, RR);
		createOpenLong(14810, UNDER, 14610, QTY, 14810, RR);
		createOpenLong(14610, UNDER, 14410, QTY, 14610, RR);
		createOpenLong(14410, UNDER, 14210, QTY, 14410, RR);
		createOpenLong(14210, UNDER, 14010, QTY, 14210, RR);
		
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