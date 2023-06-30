package com.idk.coin.bybit.alram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

abstract public class CalculateModel {
	public static Logger LOG 			= LoggerFactory.getLogger(CalculatePositionV3.class.getName());
	Position buy,sell;
	Balance balance;
	double price;
	double qty;
	BybitAlarmsModel parent;
	boolean debug 	= false;
	StringBuffer printBuffer;
	public double INTERVAL_100  = 100;
	public double INTERVAL_200  = 200;
	public double PROFIT_100	= 100;
	public double PROFIT_200	= 200;
	
	public int MAX_100_OPEN_SIZE = 20;
	public int MAX_200_OPEN_SIZE = 10;
	public int MAX_OPEN_SIZE 	= MAX_100_OPEN_SIZE+ MAX_200_OPEN_SIZE;
	public int MIN_OPEN_SIZE	= 5;
	
	public CalculateModel(BybitAlarmsModel parent,double price,Position buy, Position sell, Balance balance,double qty, boolean debug) {
		this.parent = parent;
		this.price  = price;
		this.buy 	= buy;
		this.sell	= sell;
		this.balance= balance;
		this.qty	= qty;
		this.debug 	= debug;
		printBuffer = new StringBuffer();
	}
	public void logInfo(String str) {
		printBuffer.append(str+"\n");
	}
	public String toAlarmString() {
		return printBuffer.toString();
	}
	
	public void setIntervalProfit(double interval_100, double interval_200, double profit_100, double profit_200) {
		INTERVAL_100  = interval_100;
		INTERVAL_200  = interval_200;
		PROFIT_100	= profit_100;
		PROFIT_200	= profit_200;
	}

	public void setSizeValue(int open_100,int open_200,int open_min) {
		this.MAX_100_OPEN_SIZE	= open_100;
		this.MAX_200_OPEN_SIZE 	= open_200;
		this.MIN_OPEN_SIZE 		= open_min;
		MAX_OPEN_SIZE 			= open_100 + open_200;
	}
	
	abstract public void calculateStatus() throws Exception;
	abstract public double getDefaultPrice(double c_price) ;

	//#################################### SHORT 
	
	public void startOverOpenShort(double start, double end, double interval, double profit, boolean isLoss,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  				"+msg+"						#");
		for(double i=start; i<end; i+=interval) {
			double open = i;
			double close  = i - profitLimit;
			double alarm = i-alarmLimit;
			logInfo("["+(count++)+"]\t makeOpenShort("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenShort(alarm, parent.OVER, open, parent.QTY, close, parent.RR);
		}
		logInfo("#  										#");
		if(isLoss) {
			//shortStopLoss(end-profitLimit   , QTY2);
			//shortStopLoss(start+profitLimit , QTY2);
		}
	}
	public void startUnderOpenShort(double start, double end, double interval,double profit,String msg) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i>end; i-=interval) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			//logInfo("["+(count++)+"]\t openShort("+close+", UNDER, "+open+", QTY, RR, "+alarm+")  ");
			logInfo("["+(count++)+"]\t makeOpenShort("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenShort(alarm, parent.UNDER, open, parent.QTY, close, parent.RR);
			
		}
	}
	public void startUnderCloseShort(double start, double end, double interval,double profit,String msg) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i>end; i-=interval) {
			double close 	= i;
			double open  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseShort("+alarm+", UNDER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseShort(alarm, parent.UNDER, close, parent.QTY, open, parent.RR);
		}
	}
	public void startOverCloseShort(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i<end; i+=interval) {
			double close 	= i;
			double open  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseShort("+alarm+", OVER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseShort(alarm, parent.OVER, close, parent.QTY, open, parent.RR);
		}
	}
	
	
	//#################################### LONG
	public void startUnderOpenLong(double start, double end, double interval,double profit,boolean isLoss,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"				#");
		for(double i=start; i>end; i-=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeOpenLong("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenLong(alarm, parent.UNDER, open, parent.QTY, close, parent.RR);
		}
		if(isLoss) {
			
		}
	}
	public void startOverOpenLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"				#");
		for(double i=start; i<end; i+=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			//logInfo("["+(count++)+"]\t openLong("+close+", OVER, "+open+", QTY, RR, "+alarm+");");
			logInfo("["+(count++)+"]\t makeOpenLong("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenLong(alarm, parent.OVER, open, parent.QTY, close, parent.RR);
		}
	}
	public void startOverCloseLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"						#");
		for(double i=start; i<end; i+=interval) {
			double close 	= i;
			double open  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseLong("+alarm+", OVER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseLong(alarm, parent.OVER, close, parent.QTY, open, parent.RR);
		}
	}
	public void startUnderCloseLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"						#");
		for(double i=start; i>end; i-=interval) {
			double close 	= i;
			double open  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
	 
			/*logInfo("["+(count++)+"]\tcloseLong("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");*/
			logInfo("["+(count++)+"]\t makeCloseLong("+alarm+", UNDER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseLong(alarm, parent.UNDER, close, parent.QTY, open, parent.RR);
		}
	}
	public void currentStatus() throws Exception{
		LOG.info(balance.toString());
		LOG.info("[LONG]"+buy.toString());
		LOG.info("[SHORT]"+sell.toString());
		double default_price = getDefaultPrice(price);
		
		LOG.info("현재가:["+default_price + "],잔고 : [" + String.format("%.2f",balance.getEquity())  + "]"
				+","+ "SHORT : [" + sell.getEntry_price() + "]," + String.format("%.2f",sell.getSize()) + "주"
				+","+ "LONG : ["+ buy.getEntry_price() +"],"+ String.format("%.2f",buy.getSize()) + "주"); 
	}

	public Position getBuy() {
		return buy;
	}
	public void setBuy(Position buy) {
		this.buy = buy;
	}
	public Position getSell() {
		return sell;
	}
	public void setSell(Position sell) {
		this.sell = sell;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public BybitAlarmsModel getParent() {
		return parent;
	}
	public void setParent(BybitAlarmsModel parent) {
		this.parent = parent;
	}
	
}
