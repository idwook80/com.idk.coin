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
	public void createOpenLong(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) throws Exception{
		addOpenLong(trigger, is_over, price, qty);
		addCloseLong(price, UNDER, close_price, qty, is_reverse);
	}
	
	public AlarmPrice addOpenLong(double price, boolean is_over, double open_price, double qty) throws Exception {
		return addOpenLong(price, is_over, open_price, qty, ONCE);
	}
	
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double open_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == OVER) {
			if(trigger < open_price) throw new Exception("[Warning] trigger < open_price [Long] for reverse"); 
			//addOpenLong(18910, OVER, 18930, QTY, RR); //18920 무한
		}
		
		return main.getAlarmPriceManager().createOpenLong(trigger, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE LONG   ##################### */
	public void createCloseLong(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) throws Exception{
			addCloseLong(trigger, is_over, price, qty, ONCE);
			addOpenLong(price, OVER, open_price, qty, is_reverse);
	}
	
	public AlarmPrice addCloseLong(double price, boolean is_over, double open_price, double qty) throws Exception{
		return addCloseLong(price, is_over, open_price, qty, ONCE);
	}
	
	public AlarmPrice addCloseLong(double trigger, boolean is_over, double close_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == UNDER) {
			if(trigger > close_price) throw new Exception("[Warning] trigger > close_price [Long] for reverse"); 
			//ex addCloseLong(18930, UNDER, 18910, QTY, RR); 18920 무한
		}
		return main.getAlarmPriceManager().createCloseLong(trigger, is_over, close_price, qty,is_reverse);
	}
	/* ###################    OPEN SHORT   ##################### */
	public void createOpenShort(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) throws Exception {
			addOpenShort(trigger, is_over, price, qty, ONCE);
			addCloseShort(price, OVER, close_price, qty, is_reverse);
	}
	public AlarmPrice addOpenShort(double price,boolean is_over,double open_price, double qty) throws Exception {
		return addOpenShort(price, is_over, open_price, qty, ONCE);
	}
	
	
	public AlarmPrice addOpenShort(double trigger, boolean is_over, double open_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == UNDER) {
			if(trigger > open_price) throw new Exception("[Warning] trigger > open_price [Short] for reverse");
			//addOpenShort(18930, UNDER, 18910, QTY, RR); //18920 무한 triger
		}
		
		return main.getAlarmPriceManager().createOpenShort(trigger, is_over, open_price, qty,is_reverse);
	 
	}
	
	
	/* ###################    CLOSE SHORT   ##################### */
	
	public void createCloseShort(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) throws Exception {
		addCloseShort(trigger, is_over, price, qty, ONCE);
		addOpenShort(price, UNDER, open_price, qty, is_reverse);
	}
	
	public AlarmPrice addCloseShort(double price, boolean is_over, double open_price, double qty) throws Exception {
	 return addCloseShort(price, is_over, open_price, qty, ONCE);
	}
	
	public AlarmPrice addCloseShort(double trigger, boolean is_over, double close_price, double qty, boolean is_reverse) throws Exception {
		if(is_reverse && is_over == OVER) {
			if(trigger < close_price) throw new Exception("[Warning] trigger < close_price [Short] for reverse");
			 //ex) addCloseShort(18910, OVER, 18930, QTY, RR); // 18920 무한 
		}
		return main.getAlarmPriceManager().createCloseShort(trigger, is_over, close_price, qty,is_reverse);
		
	}
	
	/** Only Long Close **/
	public void longStopLoss(double trigger, boolean is_over, double close_price, double qty) throws Exception{
		addCloseLong(trigger, is_over, close_price, qty);
		
	}
	/** Long Close And (Long & Short) Open Package  **/
	public void longStopLoss(double trigger, double qty) throws Exception {
		addOpenLong(trigger + (MIN_PROFIT*2), UNDER, trigger, LOSS_TRIGGER_QTY);
		addCloseLong(trigger, UNDER, trigger+5, qty);
		
		addOpenLong(trigger, UNDER, trigger-MIN_PROFIT, QTY);
		addOpenShort(trigger,UNDER, trigger+MIN_PROFIT, QTY);
		
	}
	/** Only Short Close **/
	public void shortStopLoss(double trigger, boolean is_over, double close_price, double qty)  throws Exception {
		addCloseShort(trigger, OVER, close_price, qty);
	}
	/** Short Close And (Long & Short) Open Package **/
	public void shortStopLoss(double trigger, double qty)  throws Exception {
		addOpenShort(trigger - (MIN_PROFIT*2), OVER, trigger, LOSS_TRIGGER_QTY);
		addCloseShort(trigger, OVER, trigger+5, qty);
		
		addOpenShort(trigger, OVER, trigger+MIN_PROFIT, QTY);
		addOpenLong(trigger, OVER, trigger-MIN_PROFIT, QTY);
	}
}
