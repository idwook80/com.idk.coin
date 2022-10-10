package com.idk.coin.bybit.model;

import java.math.BigDecimal;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.BybitMain;

abstract public class AlarmManagerModel {
	public static boolean OVER  		= true;
	public static boolean UNDER 		= false;
	public static boolean REVERSE		= true;
	public static boolean REPEAT		= REVERSE;
	public static boolean RR			= REVERSE; //reverse and repeat
	public static boolean ONCE			= false;
	
	public static BigDecimal DEFAULT_QTY= new BigDecimal("0.15");
	public static double  QTY	  		= DEFAULT_QTY.doubleValue();
	public static double  QTY1			= new BigDecimal("0.2").doubleValue();
	public static double  QTY2			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public static double  QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public static double  QTY4			= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
	public static double  QTY5			= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
	public static double  LOSS_TRIGGER_QTY	= 0.001;
	public static double  MIN_PROFIT		= 50;
	
	public BybitMain main;
	
	public AlarmManagerModel(BybitMain main) {
		this.main = main;
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
	
	public AlarmPrice addOpenLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		return main.getAlarmPriceManager().createOpenLong(price, is_over, open_price, qty,is_reverse);
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
	
	
	public AlarmPrice addOpenShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		return main.getAlarmPriceManager().createOpenShort(price, is_over, open_price, qty,is_reverse);
	}
	
	public AlarmPrice addCloseLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		return main.getAlarmPriceManager().createCloseLong(price, is_over, open_price, qty,is_reverse);
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
	
	public AlarmPrice addCloseShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		return main.getAlarmPriceManager().createCloseShort(price, is_over, open_price, qty,is_reverse);
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
