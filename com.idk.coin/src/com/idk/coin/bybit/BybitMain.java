package com.idk.coin.bybit;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.account.OrderRest;
import com.idk.coin.util.ANSI;

import io.contek.invoker.bybit.api.common.constants.TickDirectionKeys;

public class BybitMain {
	public static boolean OVER  		= true;
	public static boolean UNDER 		= false;
	public static BigDecimal DEFAULT_QTY= new BigDecimal("0.15");
	public static double  QTY	  		= DEFAULT_QTY.doubleValue();
	public static double  QTY2			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
	public static double  QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
	public static double  QTY4			= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
	public static double  QTY5			= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
	public static double  LOSS_TRIGGER_QTY	= 0.001;
	public static double  MIN_PROFIT		= 50;
	public static boolean REVERSE		= true;
	public static boolean REPEAT		= REVERSE;
	public static boolean RR			= REVERSE; //reverse and repeat
	public static boolean ONCE			= false;
	
	public static boolean is_main_account = true;
	
	public static void main(String[] args) {
		new BybitMain();
		
	}
	public static Logger LOG =   LoggerFactory.getLogger(BybitMain.class.getName());
	 
	BybitMarket		 	bybitMarket;
	BybitRealTime 	 	bybitRealTime;
	AlarmPriceManager 	alarmPriceManager;
	PositionManager 	positionManager;
	SubAlarmManager 	subAlarm;
	
	public BybitMain() {
		CoinConfig.loadConfig();
		init();
		start();
		LOG.info("Start Bybit Alarm System!!");
	}
	public void init() {
		userSet();
		positionManager 	= new PositionManager(this);
		alarmPriceManager   = new AlarmPriceManager();
		bybitRealTime 		= new BybitRealTime(this);
		bybitMarket 		= new BybitMarket(this);
		
		if(is_main_account) setAlarm();
		else setSubAlarm();
	}
	public void userSet() {
		if(is_main_account) {
			DEFAULT_QTY     = new BigDecimal("0.15");
		}else {
			DEFAULT_QTY     = new BigDecimal("0.001");
			String sub_key =  System.getProperty(CoinConfig.BYBIT_SUB_KEY);
			String sub_secret = System.getProperty(CoinConfig.BYBIT_SUB_SECRET);
			System.setProperty(CoinConfig.BYBIT_KEY, sub_key);
			System.setProperty(CoinConfig.BYBIT_SECRET, sub_secret);
		}
		
	    QTY		= DEFAULT_QTY.doubleValue();
		QTY2  	= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
		QTY3	= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
		QTY4	= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
		QTY5	= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
		LOSS_TRIGGER_QTY	= 0.001;
	}

	public void start() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000 * 60 *60);
						double last_price = bybitMarket.getLastPrice();
						subAlarm.resetAlarm(Math.floor(last_price));
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
	
	public void setSubAlarm() {
		boolean without_cancel = false;
		if(!without_cancel) {
			try {
				String api_key = System.getProperty(CoinConfig.BYBIT_KEY);
				String api_secret = System.getProperty(CoinConfig.BYBIT_SECRET);
				OrderRest.cancelAllOrder(api_key,api_secret,"BTCUSDT");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		double last_price = bybitMarket.getLastPrice();
		
		subAlarm = new SubAlarmManager(this, Math.floor(last_price));
		
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
		shortStopLoss(21750, QTY2);
		shortStopLoss(21500, QTY2);
		shortStopLoss(21250, QTY2);
		shortStopLoss(21000, QTY2);
		
		createOpenShort(20710, OVER, 20910, QTY, 20810, RR);
		createOpenShort(20510, OVER, 20710, QTY, 20610, RR);
		createOpenShort(20310, OVER, 20510, QTY, 20410, RR);
		createOpenShort(20110, OVER, 20310, QTY, 20210, RR);
		createOpenShort(19910, OVER, 20110, QTY, 20010, RR);
		shortStopLoss(20750, QTY2);
		shortStopLoss(20500, QTY2);
		shortStopLoss(20250, QTY2);
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<<<<<<----------------------------
/**	############################################### **/
	public void setShort() {
		addCloseShort(20010, OVER, 19910, QTY, RR);
		addCloseShort(19910, OVER, 19810, QTY, RR);
		addCloseShort(19810, OVER, 19710, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		addOpenShort(19510, UNDER, 19610, QTY, RR);
		addOpenShort(19410, UNDER, 19510, QTY, RR);
		addOpenShort(19310, UNDER, 19410, QTY, RR);
		addOpenShort(19310, UNDER, 19360, QTY2);
		
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void setShortTakeProfit() {
		
		createCloseShort(19310, UNDER, 19110, QTY, 19210, RR);
		addOpenShort(19110, UNDER, 19160, QTY2);
		
		
		createCloseShort(19010, UNDER, 18910, QTY, 19010, RR);
		createCloseShort(18910, UNDER, 18710, QTY, 18910, RR);
		createCloseShort(18710, UNDER, 18510, QTY, 18710, RR);
		createCloseShort(18510, UNDER, 18310, QTY, 18510, RR);
		createCloseShort(18310, UNDER, 18110, QTY, 18310, RR);
		
		createCloseShort(18110, UNDER, 17910, QTY, 18110, RR);
		createCloseShort(17910, UNDER, 17710, QTY, 17910, RR);
		createCloseShort(17710, UNDER, 17510, QTY, 17710, RR);
		createCloseShort(17510, UNDER, 17310, QTY, 17510, RR);
		createCloseShort(17310, UNDER, 17110, QTY, 17310, RR);
		
	}
	
/**   ############### LONG SETTINGS ####################  **/
	public void setLongTakeProfit() {
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
		createCloseLong(20610, OVER, 20810, QTY, 20710, RR);
		createCloseLong(20410, OVER, 20610, QTY, 20510, RR);
		createCloseLong(20210, OVER, 20410, QTY, 20310, RR);
		createCloseLong(20010, OVER, 20210, QTY, 20110, RR);
		
	}
	
/**	############################################### **/
/** ######### Long Order exists  Start ############ **///<<<<<<----------------------------
/**	############################################### **/
	public void setLong() {
		addOpenLong(20010, OVER, 19910, QTY, RR);
		addOpenLong(19910, OVER, 19810, QTY, RR);
		addOpenLong(19810, OVER, 19710, QTY, RR);
		addOpenLong(19710, OVER, 19610, QTY, RR);
		addOpenLong(19685, OVER, 19635, QTY, RR);
		addOpenLong(19660, OVER, 19560, QTY, RR);
		addOpenLong(19635, OVER, 19585, QTY, RR);
		addOpenLong(19610, OVER, 19560, QTY, RR);
		addOpenLong(19585, OVER, 19535, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		addCloseLong(19510, UNDER, 19560, QTY, RR);
		addCloseLong(19485, UNDER, 19535, QTY, RR);
		addCloseLong(19460, UNDER, 19510, QTY, RR);
		addCloseLong(19410, UNDER, 19460, QTY, RR);
		addCloseLong(19360, UNDER, 19410, QTY, RR);
		addCloseLong(19310, UNDER, 19360, QTY, RR);
		
		
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void setLongStopLoss() {
		
		//longStopLoss(19500, QTY2);
		
		createOpenLong(19410, UNDER, 19260, QTY, 19310, RR);
		createOpenLong(19410, UNDER, 19210, QTY, 19310, RR);
	
		longStopLoss(19250, QTY3);
		createOpenLong(19310, UNDER, 19110, QTY, 19210, RR);
		createOpenLong(19210, UNDER, 19010, QTY, 19110, RR);
		longStopLoss(19000, QTY2);
		
		createOpenLong(19010, UNDER, 18810, QTY, 18910, RR);
		createOpenLong(18810, UNDER, 18610, QTY, 18710, RR);
		createOpenLong(18610, UNDER, 18410, QTY, 18510, RR);
		createOpenLong(18410, UNDER, 18210, QTY, 18310, RR);
		createOpenLong(18210, UNDER, 18010, QTY, 18110, RR);
		longStopLoss(18750, QTY2);
		longStopLoss(18500, QTY2);
		longStopLoss(18250, QTY2);
		longStopLoss(18000, QTY2);
		
		createOpenLong(18010, UNDER, 17810, QTY, 17910, RR);
		createOpenLong(17810, UNDER, 17610, QTY, 17710, RR);
		createOpenLong(17610, UNDER, 17410, QTY, 17510, RR);
		createOpenLong(17410, UNDER, 17210, QTY, 17310, RR);
		createOpenLong(17210, UNDER, 17010, QTY, 17110, RR);
		longStopLoss(17750, QTY2);
		longStopLoss(17500, QTY2);
		longStopLoss(17250, QTY2);
		longStopLoss(17000, QTY2);
		
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
		alarmPriceManager.createOpenLong(price, is_over, open_price, qty,is_reverse);
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
	public void createCloseShorts(double price,boolean is_over, double... prices) {
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
	
}