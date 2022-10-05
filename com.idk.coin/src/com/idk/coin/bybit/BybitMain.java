<<<<<<< HEAD
package com.idk.coin.bybit;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.util.ANSI;

import io.contek.invoker.bybit.api.common.constants.TickDirectionKeys;

public class BybitMain {
	public static boolean OVER  		= true;
	public static boolean ov			= OVER;
	public static boolean UNDER 		= false;
	public static boolean ud			= UNDER;
	public static BigDecimal DEFAULT_QTY= new BigDecimal("0.15");
	public static double  QTY	  		= DEFAULT_QTY.doubleValue();
	public static double  QTY2			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public static double  QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public static boolean REVERSE		= true;
	public static boolean REPEAT		= REVERSE;
	public static boolean RR			= REVERSE; //reverse and repeat
	public static boolean ONCE			= false;
	
	public static void main(String[] args) {
		new BybitMain();
		
	}
	public static Logger LOG =   LoggerFactory.getLogger(BybitMain.class.getName());
	 
	BybitMarket		 	bybitMarket;
	BybitRealTime 	 	bybitRealTime;
	AlarmPriceManager 	alarmPriceManager;
	PositionManager 	positionManager;
	
	
	public BybitMain() {
		CoinConfig.loadConfig();
		init();
		start();
		LOG.info("Start Bybit Alarm System!!");
	}
	public void init() {
		positionManager = new PositionManager(this);
		alarmPriceManager = new AlarmPriceManager();
		setAlarm();
		bybitRealTime = new BybitRealTime(this);
	}

	public void start() {
		bybitMarket = new BybitMarket(this);
	}
	
	
	public void setAlarm() {
		setShortStopLoss();
		setShort();
		setShortTakeProfit();
		setLongTakeProfit();
		setLong();
		setLongStopLoss();
	}

	public void setShortStopLoss() {
		createOpenShort(21170, OVER, 21910, QTY, 21810, RR);
		createOpenShort(21510, OVER, 21710, QTY, 21610, RR);
		createOpenShort(21310, OVER, 21510, QTY, 21410, RR);
		createOpenShort(21110, OVER, 21310, QTY, 21210, RR);
		createOpenShort(21010, OVER, 21110, QTY, 21010, RR);
		
		addOpenLong(21200, OVER, 21160, QTY);
		addOpenShort(21200, OVER, 21260, QTY);
		shortStopLoss(21200, OVER, 21205, QTY3);
		
		createOpenShort(20710, OVER, 21010, QTY, 20910, RR);
		createOpenShort(20710, OVER, 20910, QTY, 20810, RR);
		createOpenShort(20510, OVER, 20810, QTY, 20710, RR);
		createOpenShort(20510, OVER, 20710, QTY, 20610, RR);
		createOpenShort(20510, OVER, 20610, QTY, 20510, RR);
		
		addOpenLong(20700, OVER, 20660, QTY);
		addOpenShort(20700, OVER, 20760, QTY);
		shortStopLoss(20700, OVER, 20705, QTY3);
		
		addOpenShort(20500, OVER, 20560, QTY);
		addOpenLong(20500, OVER, 20460, QTY);
		shortStopLoss(20500, OVER, 20505, QTY3);
		createOpenShort(20310, OVER, 20510, QTY, 20410, RR);
		
	}
	public void setShort() {
		
		/**	###############################################**/
		//-----------## Short Order Exists------------------->
		
		//addCloseShort(20510, OVER, 20410, QTY, RR);
		addCloseShort(20410, OVER, 20360, QTY, RR);
		addCloseShort(20360, OVER, 20310, QTY, RR);
		addCloseShort(20310, OVER, 20260, QTY, RR);
		addCloseShort(20260, OVER, 20210, QTY, RR);
		addCloseShort(20210, OVER, 20160, QTY, RR);
		/**	###  up down line ####**/
		addOpenShort(20110, UNDER, 20160, QTY, RR);
		addOpenShort(20060, UNDER, 20110, QTY, RR);
		addOpenShort(20010, UNDER, 20060, QTY, RR);
		addOpenShort(19960, UNDER, 20010, QTY, RR);
		addOpenShort(19910, UNDER, 20010, QTY, RR);
		
		//-----------## Short Order Exists------------------->
		/**	###############################################**/
		createCloseShort(20010, UNDER, 19810, QTY, 19910, RR);

		
	}
	public void setShortTakeProfit() {
		
		createCloseShort(19910, UNDER, 19710, QTY, 19810, RR);
		createCloseShort(19810, UNDER, 19610, QTY, 19710, RR);
		createCloseShort(19710, UNDER, 19510, QTY, 19610, RR);
		createCloseShort(19610, UNDER, 19410, QTY, 19510, RR);
		
		createCloseShort(19510, UNDER, 19310, QTY, 19410, RR);
		createCloseShort(19410, UNDER, 19210, QTY, 19310, RR);
		createCloseShort(19310, UNDER, 19110, QTY, 19210, RR);
		createCloseShort(19210, UNDER, 19010, QTY, 19110, RR);
		
		createCloseShort(19010, UNDER, 18910, QTY, 19010, RR);
		createCloseShort(19010, UNDER, 18710, QTY, 18910, RR);
		createCloseShort(18910, UNDER, 18510, QTY, 18710, RR);
		createCloseShort(18710, UNDER, 18310, QTY, 18510, RR);
		createCloseShort(18510, UNDER, 18110, QTY, 18310, RR);
		
		createCloseShort(18110, UNDER, 17910, QTY, 18110, RR);
		createCloseShort(18110, UNDER, 17710, QTY, 17910, RR);
		createCloseShort(17710, UNDER, 17510, QTY, 17710, RR);
		createCloseShort(17510, UNDER, 17310, QTY, 17510, RR);
		createCloseShort(17510, UNDER, 17110, QTY, 17310, RR);
		
	}
	
/**   ############### LONG SETTINGS ####################  **/
	public void setLongTakeProfit() {
		createCloseLong(20910, OVER, 21110, QTY, 21010, RR);
		createCloseLong(20710, OVER, 20910, QTY, 20810, RR);
		createCloseLong(20510, OVER, 20710, QTY, 20610, RR);
		
		createCloseLong(20310, OVER, 20510, QTY, 20410, RR);
		
	}
	public void setLong() {
		
		/**	###############################################**/
		//-------------## Long Order exists------------------>
		//addOpenLong(20510, OVER, 20410, QTY, RR);
		addOpenLong(20310, OVER, 20110, QTY, RR);
		/**	###  up down line ####**/
		addCloseLong(20010, UNDER, 20210, QTY, RR);
		addCloseLong(19910, UNDER, 20010, QTY, RR);
		
		//-------------## Long Order exists------------------>
		/**	###############################################**/
		
		createOpenLong(20010, UNDER, 19810, QTY, 19910, RR);
		createOpenLong(19910, UNDER, 19710, QTY, 19810, RR);
		createOpenLong(19810, UNDER, 19610, QTY, 19710, RR);
		createOpenLong(19710, UNDER, 19510, QTY, 19610, RR);
		createOpenLong(19610, UNDER, 19410, QTY, 19510, RR);
		createOpenLong(19510, UNDER, 19360, QTY, 19410, RR);
		
		
	}
	
	public void setLongStopLoss() {
		createOpenLong(19510, UNDER, 19310, QTY, 19360, RR);
		createOpenLong(19410, UNDER, 19210, QTY, 19310, RR);
		createOpenLong(19310, UNDER, 19110, QTY, 19210, RR);
		createOpenLong(19210, UNDER, 19010, QTY, 19110, RR);
		
		createOpenLong(19210, UNDER, 18960, QTY, 19060, RR);
		createOpenLong(19010, UNDER, 18910, QTY, 19010, RR);
		createOpenLong(19010, UNDER, 18810, QTY, 18910, RR);
		
		longStopLoss(18860, UNDER, 18855, QTY2);
		addOpenLong(18860, UNDER, 18810, QTY);
		
		createOpenLong(18910, UNDER, 18710, QTY, 18810, RR);
		createOpenLong(18810, UNDER, 18610, QTY, 18710, RR);
		
		longStopLoss(18600, UNDER, 18595, QTY2);
		addOpenLong(18600, UNDER, 18560, QTY);
		createOpenLong(18710, UNDER, 18510, QTY, 18610, RR);
		
		
		createOpenLong(18510, UNDER, 18410, QTY, 18510, RR);
		createOpenLong(18510, UNDER, 18310, QTY, 18410, RR);
		createOpenLong(18510, UNDER, 18210, QTY, 18310, RR);
		createOpenLong(18510, UNDER, 18110, QTY, 18210, RR);
		createOpenLong(18510, UNDER, 18010, QTY, 18110, RR);
				
		longStopLoss(18300,UNDER,18295,QTY3);
		addOpenLong(18300,UNDER,18260,QTY);
		addOpenShort(18300,UNDER,18360,QTY);
		
		longStopLoss(18000,UNDER,17995,QTY2);
		addOpenLong(18000,UNDER,17960,QTY);
		
		createOpenLong(18010, UNDER, 17910, QTY, 18010, RR);
		createOpenLong(18010, UNDER, 17810, QTY, 17910, RR);
		createOpenLong(18010, UNDER, 17710, QTY, 17810, RR);
		createOpenLong(18010, UNDER, 17610, QTY, 17710, RR);
		createOpenLong(18010, UNDER, 17510, QTY, 17610, RR);
		longStopLoss(17700, UNDER, 17695, QTY2);
		addOpenLong(17700, UNDER, 17660, QTY);
		
		createOpenLong(17510, UNDER, 17410, QTY, 17510, RR);
		createOpenLong(17510, UNDER, 17310, QTY, 17410, RR);
		createOpenLong(17510, UNDER, 17210, QTY, 17310, RR);
		createOpenLong(17510, UNDER, 17110, QTY, 17210, RR);
		createOpenLong(17510, UNDER, 17010, QTY, 17110, RR);
		
		longStopLoss(17000, UNDER, 16995, QTY2);
		addOpenLong(17000, UNDER, 16960, QTY);
		addOpenLong(17000, UNDER, 16910, QTY);
		
	}
	
	/* ###################    OPEN LONG   ##################### */
	public void createOpenLong(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) {
		addOpenLong(trigger, is_over, price, qty);
		addCloseLong(price, UNDER, close_price, qty, is_reverse);
	}
	public void createOpenLong(double price, boolean is_over, double... prices) {
		for(double p : prices) {
			addOpenLong(price,is_over,p,QTY);
		}
	}
	
	public void addOpenLong(double price, boolean is_over, double open_price, double qty) {
		addOpenLong(price, is_over, open_price, qty, false);
	}
	
	public void addOpenLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createOpenLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE LONG   ##################### */
	public void createCloseLong(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) {
			addCloseLong(trigger, is_over, price, qty, ONCE);
			addOpenLong(price, OVER, open_price, qty, is_reverse);
	}
	public void createCloseLong(double price, boolean is_over, double... prices) {
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
	public void createOpenShort(double trigger, boolean is_over, double... prices) {
		for(double p : prices) {
			addOpenShort(trigger,is_over,p,QTY);
		}
	}
	public void addOpenShort(double price,boolean is_over,double open_price, double qty) {
		addOpenShort(price, is_over, open_price, qty, false);
	}
	
	
	public void addOpenShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createOpenShort(price, is_over, open_price, qty,is_reverse);
	}
	
	public void addCloseLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createCloseLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE SHORT   ##################### */
	
	public void createCloseShort(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) {
		addCloseShort(trigger, is_over, price, qty, ONCE);
		addOpenShort(price, UNDER, open_price, qty, is_reverse);
	}
	public void createCloseShort(double price,boolean is_over, double... prices) {
		for(double p : prices) {
			addCloseShort(price,is_over,p,QTY);
		}
	}
	
	public void addCloseShort(double price, boolean is_over, double open_price, double qty) {
		addCloseShort(price, is_over, open_price, qty, false);
	}
	
	public void addCloseShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createCloseShort(price, is_over, open_price, qty,is_reverse);
	}
	
	
	public void longStopLoss(double trigger, boolean is_over, double close_price, double qty) {
		addCloseLong(trigger, is_over, close_price, qty);
		
	}
	public void shortStopLoss(double trigger, boolean is_over, double close_price, double qty) {
		addCloseShort(trigger, OVER, close_price, qty);
	}
	
	
	
	
	public BybitMarket getBybitMarket() {
		return bybitMarket;
	}
	public BybitRealTime getBybitRealTime() {
		return bybitRealTime;
	}
	public AlarmPriceManager getAlarmPriceManager() {
		return alarmPriceManager;
	}
	public PositionManager getPositionManager() {
		return positionManager;
	}
	
=======
package com.idk.coin.bybit;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.util.ANSI;

import io.contek.invoker.bybit.api.common.constants.TickDirectionKeys;

public class BybitMain {
	public static boolean OVER  		= true;
	public static boolean ov			= OVER;
	public static boolean UNDER 		= false;
	public static boolean ud			= UNDER;
	public static BigDecimal DEFAULT_QTY= new BigDecimal("0.15");
	public static double  QTY	  		= DEFAULT_QTY.doubleValue();
	public static double  QTY2			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public static double  QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public static boolean REVERSE		= true;
	public static boolean REPEAT		= REVERSE;
	public static boolean RR			= REVERSE; //reverse and repeat
	public static boolean ONCE			= false;
	
	public static void main(String[] args) {
		new BybitMain();
		
	}
	public static Logger LOG =   LoggerFactory.getLogger(BybitMain.class.getName());
	 
	BybitMarket		 	bybitMarket;
	BybitRealTime 	 	bybitRealTime;
	AlarmPriceManager 	alarmPriceManager;
	PositionManager 	positionManager;
	
	
	public BybitMain() {
		CoinConfig.loadConfig();
		init();
		start();
		LOG.info("Start Bybit Alarm System!!");
	}
	public void init() {
		positionManager = new PositionManager(this);
		alarmPriceManager = new AlarmPriceManager();
		setAlarm();
		bybitRealTime = new BybitRealTime(this);
	}

	public void start() {
		bybitMarket = new BybitMarket(this);
	}
	
	
	public void setAlarm() {
		setShortStopLoss();
		setShort();
		setShortTakeProfit();
		setLongTakeProfit();
		setLong();
		setLongStopLoss();
	}

	public void setShortStopLoss() {
		createOpenShort(21170, OVER, 21910, QTY, 21810, RR);
		createOpenShort(21510, OVER, 21710, QTY, 21610, RR);
		createOpenShort(21310, OVER, 21510, QTY, 21410, RR);
		createOpenShort(21110, OVER, 21310, QTY, 21210, RR);
		createOpenShort(21010, OVER, 21110, QTY, 21010, RR);
		
		addOpenLong(21200, OVER, 21160, QTY);
		addOpenShort(21200, OVER, 21260, QTY);
		shortStopLoss(21200, OVER, 21205, QTY3);
		
		createOpenShort(20710, OVER, 21010, QTY, 20910, RR);
		createOpenShort(20710, OVER, 20910, QTY, 20810, RR);
		createOpenShort(20510, OVER, 20810, QTY, 20710, RR);
		createOpenShort(20510, OVER, 20710, QTY, 20610, RR);
		createOpenShort(20510, OVER, 20610, QTY, 20510, RR);
		
		addOpenLong(20700, OVER, 20660, QTY);
		addOpenShort(20700, OVER, 20760, QTY);
		shortStopLoss(20700, OVER, 20705, QTY3);
		
		addOpenShort(20500, OVER, 20560, QTY);
		addOpenLong(20500, OVER, 20460, QTY);
		shortStopLoss(20500, OVER, 20505, QTY3);
		createOpenShort(20310, OVER, 20510, QTY, 20410, RR);
		
	}
	public void setShort() {
		
		/**	###############################################**/
		//-----------## Short Order Exists------------------->
		
		//addCloseShort(20510, OVER, 20410, QTY, RR);
		addCloseShort(20410, OVER, 20360, QTY, RR);
		addCloseShort(20360, OVER, 20310, QTY, RR);
		addCloseShort(20310, OVER, 20260, QTY, RR);
		addCloseShort(20260, OVER, 20210, QTY, RR);
		addCloseShort(20210, OVER, 20160, QTY, RR);
		/**	###  up down line ####**/
		addOpenShort(20110, UNDER, 20160, QTY, RR);
		addOpenShort(20060, UNDER, 20110, QTY, RR);
		addOpenShort(20010, UNDER, 20060, QTY, RR);
		addOpenShort(19960, UNDER, 20010, QTY, RR);
		addOpenShort(19910, UNDER, 20010, QTY, RR);
		
		//-----------## Short Order Exists------------------->
		/**	###############################################**/
		createCloseShort(20010, UNDER, 19810, QTY, 19910, RR);

		
	}
	public void setShortTakeProfit() {
		
		createCloseShort(19910, UNDER, 19710, QTY, 19810, RR);
		createCloseShort(19810, UNDER, 19610, QTY, 19710, RR);
		createCloseShort(19710, UNDER, 19510, QTY, 19610, RR);
		createCloseShort(19610, UNDER, 19410, QTY, 19510, RR);
		
		createCloseShort(19510, UNDER, 19310, QTY, 19410, RR);
		createCloseShort(19410, UNDER, 19210, QTY, 19310, RR);
		createCloseShort(19310, UNDER, 19110, QTY, 19210, RR);
		createCloseShort(19210, UNDER, 19010, QTY, 19110, RR);
		
		createCloseShort(19010, UNDER, 18910, QTY, 19010, RR);
		createCloseShort(19010, UNDER, 18710, QTY, 18910, RR);
		createCloseShort(18910, UNDER, 18510, QTY, 18710, RR);
		createCloseShort(18710, UNDER, 18310, QTY, 18510, RR);
		createCloseShort(18510, UNDER, 18110, QTY, 18310, RR);
		
		createCloseShort(18110, UNDER, 17910, QTY, 18110, RR);
		createCloseShort(18110, UNDER, 17710, QTY, 17910, RR);
		createCloseShort(17710, UNDER, 17510, QTY, 17710, RR);
		createCloseShort(17510, UNDER, 17310, QTY, 17510, RR);
		createCloseShort(17510, UNDER, 17110, QTY, 17310, RR);
		
	}
	
/**   ############### LONG SETTINGS ####################  **/
	public void setLongTakeProfit() {
		createCloseLong(20910, OVER, 21110, QTY, 21010, RR);
		createCloseLong(20710, OVER, 20910, QTY, 20810, RR);
		createCloseLong(20510, OVER, 20710, QTY, 20610, RR);
		
		createCloseLong(20310, OVER, 20510, QTY, 20410, RR);
		
	}
	public void setLong() {
		
		/**	###############################################**/
		//-------------## Long Order exists------------------>
		//addOpenLong(20510, OVER, 20410, QTY, RR);
		addOpenLong(20310, OVER, 20110, QTY, RR);
		/**	###  up down line ####**/
		addCloseLong(20010, UNDER, 20210, QTY, RR);
		addCloseLong(19910, UNDER, 20010, QTY, RR);
		
		//-------------## Long Order exists------------------>
		/**	###############################################**/
		
		createOpenLong(20010, UNDER, 19810, QTY, 19910, RR);
		createOpenLong(19910, UNDER, 19710, QTY, 19810, RR);
		createOpenLong(19810, UNDER, 19610, QTY, 19710, RR);
		createOpenLong(19710, UNDER, 19510, QTY, 19610, RR);
		createOpenLong(19610, UNDER, 19410, QTY, 19510, RR);
		createOpenLong(19510, UNDER, 19360, QTY, 19410, RR);
		
		
	}
	
	public void setLongStopLoss() {
		createOpenLong(19510, UNDER, 19310, QTY, 19360, RR);
		createOpenLong(19410, UNDER, 19210, QTY, 19310, RR);
		createOpenLong(19310, UNDER, 19110, QTY, 19210, RR);
		createOpenLong(19210, UNDER, 19010, QTY, 19110, RR);
		
		createOpenLong(19210, UNDER, 18960, QTY, 19060, RR);
		createOpenLong(19010, UNDER, 18910, QTY, 19010, RR);
		createOpenLong(19010, UNDER, 18810, QTY, 18910, RR);
		
		longStopLoss(18860, UNDER, 18855, QTY2);
		addOpenLong(18860, UNDER, 18810, QTY);
		
		createOpenLong(18910, UNDER, 18710, QTY, 18810, RR);
		createOpenLong(18810, UNDER, 18610, QTY, 18710, RR);
		
		longStopLoss(18600, UNDER, 18595, QTY2);
		addOpenLong(18600, UNDER, 18560, QTY);
		createOpenLong(18710, UNDER, 18510, QTY, 18610, RR);
		
		
		createOpenLong(18510, UNDER, 18410, QTY, 18510, RR);
		createOpenLong(18510, UNDER, 18310, QTY, 18410, RR);
		createOpenLong(18510, UNDER, 18210, QTY, 18310, RR);
		createOpenLong(18510, UNDER, 18110, QTY, 18210, RR);
		createOpenLong(18510, UNDER, 18010, QTY, 18110, RR);
				
		longStopLoss(18300,UNDER,18295,QTY3);
		addOpenLong(18300,UNDER,18260,QTY);
		addOpenShort(18300,UNDER,18360,QTY);
		
		longStopLoss(18000,UNDER,17995,QTY2);
		addOpenLong(18000,UNDER,17960,QTY);
		
		createOpenLong(18010, UNDER, 17910, QTY, 18010, RR);
		createOpenLong(18010, UNDER, 17810, QTY, 17910, RR);
		createOpenLong(18010, UNDER, 17710, QTY, 17810, RR);
		createOpenLong(18010, UNDER, 17610, QTY, 17710, RR);
		createOpenLong(18010, UNDER, 17510, QTY, 17610, RR);
		longStopLoss(17700, UNDER, 17695, QTY2);
		addOpenLong(17700, UNDER, 17660, QTY);
		
		createOpenLong(17510, UNDER, 17410, QTY, 17510, RR);
		createOpenLong(17510, UNDER, 17310, QTY, 17410, RR);
		createOpenLong(17510, UNDER, 17210, QTY, 17310, RR);
		createOpenLong(17510, UNDER, 17110, QTY, 17210, RR);
		createOpenLong(17510, UNDER, 17010, QTY, 17110, RR);
		
		longStopLoss(17000, UNDER, 16995, QTY2);
		addOpenLong(17000, UNDER, 16960, QTY);
		addOpenLong(17000, UNDER, 16910, QTY);
		
	}
	
	/* ###################    OPEN LONG   ##################### */
	public void createOpenLong(double trigger, boolean is_over, double price, double qty, double close_price, boolean is_reverse) {
		addOpenLong(trigger, is_over, price, qty);
		addCloseLong(price, UNDER, close_price, qty, is_reverse);
	}
	public void createOpenLong(double price, boolean is_over, double... prices) {
		for(double p : prices) {
			addOpenLong(price,is_over,p,QTY);
		}
	}
	
	public void addOpenLong(double price, boolean is_over, double open_price, double qty) {
		addOpenLong(price, is_over, open_price, qty, false);
	}
	
	public void addOpenLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createOpenLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE LONG   ##################### */
	public void createCloseLong(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) {
			addCloseLong(trigger, is_over, price, qty, ONCE);
			addOpenLong(price, OVER, open_price, qty, is_reverse);
	}
	public void createCloseLong(double price, boolean is_over, double... prices) {
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
	public void createOpenShort(double trigger, boolean is_over, double... prices) {
		for(double p : prices) {
			addOpenShort(trigger,is_over,p,QTY);
		}
	}
	public void addOpenShort(double price,boolean is_over,double open_price, double qty) {
		addOpenShort(price, is_over, open_price, qty, false);
	}
	
	
	public void addOpenShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createOpenShort(price, is_over, open_price, qty,is_reverse);
	}
	
	public void addCloseLong(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createCloseLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE SHORT   ##################### */
	
	public void createCloseShort(double trigger, boolean is_over, double price, double qty, double open_price, boolean is_reverse) {
		addCloseShort(trigger, is_over, price, qty, ONCE);
		addOpenShort(price, UNDER, open_price, qty, is_reverse);
	}
	public void createCloseShort(double price,boolean is_over, double... prices) {
		for(double p : prices) {
			addCloseShort(price,is_over,p,QTY);
		}
	}
	
	public void addCloseShort(double price, boolean is_over, double open_price, double qty) {
		addCloseShort(price, is_over, open_price, qty, false);
	}
	
	public void addCloseShort(double price, boolean is_over, double open_price, double qty, boolean is_reverse) {
		alarmPriceManager.createCloseShort(price, is_over, open_price, qty,is_reverse);
	}
	
	
	public void longStopLoss(double trigger, boolean is_over, double close_price, double qty) {
		addCloseLong(trigger, is_over, close_price, qty);
		
	}
	public void shortStopLoss(double trigger, boolean is_over, double close_price, double qty) {
		addCloseShort(trigger, OVER, close_price, qty);
	}
	
	
	
	
	public BybitMarket getBybitMarket() {
		return bybitMarket;
	}
	public BybitRealTime getBybitRealTime() {
		return bybitRealTime;
	}
	public AlarmPriceManager getAlarmPriceManager() {
		return alarmPriceManager;
	}
	public PositionManager getPositionManager() {
		return positionManager;
	}
	
>>>>>>> refs/remotes/origin/dell-b001
}