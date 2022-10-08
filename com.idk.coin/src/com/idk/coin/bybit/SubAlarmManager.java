package com.idk.coin.bybit;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;

public class SubAlarmManager {
	public static boolean OVER  		= true;
	public static boolean UNDER 		= false;
	public static BigDecimal DEFAULT_QTY= new BigDecimal("0.001");
	public static double  QTY	  		= DEFAULT_QTY.doubleValue();
	public static double  QTY2			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public static double  QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public static double  QTY4			= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
	public static double  QTY5			= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
	public static double  LOSS_TRIGGER_QTY	= 0.001;
	public static double  MIN_PROFIT		= 50;
	public static double  PRICE_STEP		= 25;
	public static boolean REVERSE		= true;
	public static boolean REPEAT		= REVERSE;
	public static boolean RR			= REVERSE; //reverse and repeat
	public static boolean ONCE			= false;
	
	public static boolean is_main_account = false;
	public static double CUR_PRICE		 = 0.0;
	public static boolean is_open		= true;
	
	public static Logger LOG =   LoggerFactory.getLogger(SubAlarmManager.class.getName());
	 public static void main(String[] args) {
		 new SubAlarmManager(null, 20169);
	 }
	
	BybitMain main;
	public SubAlarmManager(BybitMain main, double current_price) {
		this.main = main;
		double buyPosition = main.getPositionManager().getBtcBuyPosition().getSize();
		double sellPosition = main.getPositionManager().getBtcSellPosition().getSize();
		if(buyPosition > 0.020 || sellPosition > 0.020) is_open = false;
		resetAlarm(current_price);
		
	}
	public void resetAlarm(double current_price) {
		main.getAlarmPriceManager().clearAllAlarms();
		setPrice(current_price);
		init();
		is_open = !is_open;
	}
	public void init() {
		System.out.println("Default Price : "+CUR_PRICE);
		if(is_open) createOpenSet();
		else createCloseSet();
	}
	public void userSet() {
		DEFAULT_QTY     = new BigDecimal("0.001");
		String sub_key =  System.getProperty(CoinConfig.BYBIT_SUB_KEY);
		String sub_secret = System.getProperty(CoinConfig.BYBIT_SUB_SECRET);
		System.setProperty(CoinConfig.BYBIT_KEY, sub_key);
		System.setProperty(CoinConfig.BYBIT_SECRET, sub_secret);
		
	    QTY		= DEFAULT_QTY.doubleValue();
		QTY2  	= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
		QTY3	= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
		QTY4	= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
		QTY5	= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
		LOSS_TRIGGER_QTY	= 0.001;
	}
	
	
	public void setPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		double d = Math.floor((c_price -m -h)/ 10) * 10;
		CUR_PRICE = m + h + d;
	}
	public void createOpenSet() {
		System.out.println("Open Set");
		createShortOverOpen();
		createShortUnderOpen();
		createLongOverOpen();
		createLongUnderOpen();
	}
	public void createCloseSet() {
		System.out.println("Close Set");
		createShortOverClose();
		createShortUnderClose();
		createLongOverClose();
		createLongUnderClose();
	}
	public void createShortOverClose() {
		for(double i = CUR_PRICE; i < CUR_PRICE + 2000;  i+=PRICE_STEP) {
			double open_price  = i + MIN_PROFIT;
			double close_price  = open_price - MIN_PROFIT;
			addCloseShort(open_price, OVER, close_price, QTY, RR);
		}
	}
	public void createShortUnderClose() {
		for(double i = CUR_PRICE; i > CUR_PRICE - 2000;  i-=PRICE_STEP) {
			double under_trigger = i + PRICE_STEP;
			double close_price  = i - MIN_PROFIT;
			double open_price  = close_price + MIN_PROFIT;
			createCloseShort(under_trigger, UNDER, close_price, QTY, open_price, RR);
		}
	}
	public void createLongOverClose() {
		for(double i = CUR_PRICE; i < CUR_PRICE + 2000;  i+=PRICE_STEP) {
			double over_trigger = i - PRICE_STEP; 
			double close_price = i + MIN_PROFIT;
			double open_price  = close_price - MIN_PROFIT;
			createCloseLong(over_trigger, OVER, close_price, QTY, open_price, RR);
		}
		
	}
	public void createLongUnderClose() {
		for(double i = CUR_PRICE; i > CUR_PRICE - 2000;  i-=PRICE_STEP) {
			double open_price  = i - MIN_PROFIT;
			double close_price  = open_price + MIN_PROFIT;
			addCloseLong(open_price, UNDER, close_price, QTY, RR);;
		}
	}
	/** ----------------------Open Line ---------------------------------------- **/
	public void createShortOverOpen() {
		for(double i = CUR_PRICE; i < CUR_PRICE + 2000;  i+=PRICE_STEP) {
			double over_trigger = i - PRICE_STEP;
			double open_price  = i + MIN_PROFIT;
			double close_price  = open_price - MIN_PROFIT;
			createOpenShort(over_trigger, OVER, open_price, QTY, close_price, RR);
		}
	}
	public void createShortUnderOpen() {
		for(double i = CUR_PRICE; i > CUR_PRICE - 2000;  i-=PRICE_STEP) {
			double under_trigger = i - MIN_PROFIT;
			double open_price  = under_trigger + MIN_PROFIT;
			addOpenShort(under_trigger, UNDER, open_price, QTY, RR);
		}
	}
	
	public void createLongOverOpen() {
		for(double i = CUR_PRICE; i < CUR_PRICE + 2000;  i+=PRICE_STEP) {
			double over_trigger = i + MIN_PROFIT;
			double open_price  = over_trigger - MIN_PROFIT;
			addOpenLong(over_trigger, OVER, open_price, QTY, RR);
		}
		
	}
	public void createLongUnderOpen() {
		for(double i = CUR_PRICE; i > CUR_PRICE - 2000;  i-=PRICE_STEP) {
			double under_trigger = i + PRICE_STEP; 
			double open_price  = i - MIN_PROFIT;
			double close_price  = open_price + MIN_PROFIT;
			createOpenLong(under_trigger, UNDER, open_price, QTY, close_price, RR);;
		}
	}
	 
	
	/* ###################    OPEN LONG   ##################### */
	public void createOpenLong(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) {
		addOpenLong(trigger, is_over, price, qty);
		addCloseLong(price, UNDER, close_price, qty, is_reverse);
	}
	public void createOpenLongs(double price, boolean is_over, double... prices) {
		for(double p : prices) {
			addOpenLong(price,is_over,p,QTY);
		}
	}
	
	public void addOpenLong(double price, boolean is_over, double open_price, double qty) {
		addOpenLong(price, is_over, open_price, qty, false);
	}
	
	public void addOpenLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		main.getAlarmPriceManager().createOpenLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE LONG   ##################### */
	public void createCloseLong(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) {
			addCloseLong(trigger, is_over, price, qty, ONCE);
			addOpenLong(price, OVER, open_price, qty, is_reverse);
	}
	public void createCloseLongs(double price, boolean is_over, double... prices) {
		for(double p : prices) {
			addCloseLong(price,is_over,p,QTY);
		}
	}
	
	public void addCloseLong(double price, boolean is_over, double open_price, double qty) {
		addCloseLong(price, is_over, open_price, qty, false);
	}
	
	/* ###################    OPEN SHORT   ##################### */
	public void createOpenShort(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) {
			addOpenShort(trigger, is_over, price, qty, ONCE);
			addCloseShort(price, OVER, close_price, qty, is_reverse);
	}
	public void createOpenShorts(double trigger, boolean is_over, double... prices) {
		for(double p : prices) {
			addOpenShort(trigger,is_over,p,QTY);
		}
	}
	public void addOpenShort(double price,boolean is_over,double open_price, double qty) {
		addOpenShort(price, is_over, open_price, qty, false);
	}
	
	
	public void addOpenShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		main.getAlarmPriceManager().createOpenShort(price, is_over, open_price, qty,is_reverse);
	}
	
	public void addCloseLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		main.getAlarmPriceManager().createCloseLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE SHORT   ##################### */
	
	public void createCloseShort(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) {
		addCloseShort(trigger, is_over, price, qty, ONCE);
		addOpenShort(price, UNDER, open_price, qty, is_reverse);
	}
	public void createCloseShorts(double price,boolean is_over, double... prices) {
		for(double p : prices) {
			addCloseShort(price,is_over,p,QTY);
		}
	}
	
	public void addCloseShort(double price, boolean is_over, double open_price, double qty) {
		addCloseShort(price, is_over, open_price, qty, false);
	}
	
	public void addCloseShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		main.getAlarmPriceManager().createCloseShort(price, is_over, open_price, qty,is_reverse);
	}
	
	/** Only Long Close **/
	public void longStopLoss(double trigger, boolean is_over, double close_price, double qty) {
		addCloseLong(trigger, is_over, close_price, qty);
		
	}
	/** Long Close And (Long & Short) Open Package  **/
	public void longStopLoss(double trigger, double qty) {
		addOpenLong(trigger + (MIN_PROFIT*2), UNDER, trigger, LOSS_TRIGGER_QTY);
		addCloseLong(trigger, UNDER, trigger+5, qty);
		
		addOpenLong(trigger, UNDER, trigger-MIN_PROFIT, QTY);
		addOpenShort(trigger,UNDER, trigger+MIN_PROFIT, QTY);
		
	}
	/** Only Short Close **/
	public void shortStopLoss(double trigger, boolean is_over, double close_price, double qty) {
		addCloseShort(trigger, OVER, close_price, qty);
	}
	/** Short Close And (Long & Short) Open Package **/
	public void shortStopLoss(double trigger, double qty) {
		addOpenShort(trigger - (MIN_PROFIT*2), OVER, trigger, LOSS_TRIGGER_QTY);
		addCloseShort(trigger, OVER, trigger+5, qty);
		
		addOpenShort(trigger, OVER, trigger+MIN_PROFIT, QTY);
		addOpenLong(trigger, OVER, trigger-MIN_PROFIT, QTY);
	}
	
	
}
