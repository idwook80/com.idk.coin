package com.idk.coin.bybit;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.AlarmSound;
import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.account.OrderRest;
import com.idk.coin.bybit.model.AlarmManagerModel;

public class SubAlarmManager extends AlarmManagerModel {
	public static double  MIN_PROFIT		= 50;
	public static double  PRICE_STEP		= 25;
	public final  double  MAX_PRICE 		= 1000;
	public static double  MAX_POSITION		= 0.030;
	public static double  MIN_POSITION		= 0.010;
	public static double  CUR_PRICE		 	= 0.0;
	public static boolean is_open_first		= true;
	public static boolean is_close_first 	= true;
	public static boolean is_long_more		= true;
	
	public static Logger LOG =   LoggerFactory.getLogger(SubAlarmManager.class.getName());
	 public static void main(String[] args) {
		 SubAlarmManager t = 	 new SubAlarmManager(null);
		 t.test();
	 }
	public SubAlarmManager(BybitMain main) {
		super(main);
		init();
		start();
		
	}
	public void init() {
		userSet();
	}
	public void userSet() {
	
		String sub_key =  System.getProperty(CoinConfig.BYBIT_SUB_KEY);
		String sub_secret = System.getProperty(CoinConfig.BYBIT_SUB_SECRET);
		System.setProperty(CoinConfig.BYBIT_KEY, sub_key);
		System.setProperty(CoinConfig.BYBIT_SECRET, sub_secret);
		DEFAULT_QTY     = new BigDecimal("0.001");
	    QTY				= DEFAULT_QTY.doubleValue();
	    QTY1			= new BigDecimal("0.001").doubleValue();
		QTY2  			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
		QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
		QTY4			= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
		QTY5			= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
		PRICE_STEP		= 25;
	}
	public static int auto_change_minutes = 60;
	public void start() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						AlarmSound.playReset();
						cancelAllOrder();
						double last_price = main.getBybitMarket().getLastPrice();
						resetAlarm(Math.floor(last_price));
						System.out.println("Changed Alarm Values");
						boolean without_cancel = false;
						Thread.sleep(1000 * 60 * auto_change_minutes);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
	public void resetAlarm(double current_price) {
		System.out.println("Alarm Rest Current Price : " + current_price);
		
		main.getAlarmPriceManager().clearAllAlarms();
		double buyPosition = main.getPositionManager().getBtcBuyPosition().getSize();
		double sellPosition = main.getPositionManager().getBtcSellPosition().getSize();
		is_long_more = buyPosition > sellPosition;
		
		is_close_first = buyPosition > MAX_POSITION
							&& sellPosition > MAX_POSITION;
		is_open_first  = buyPosition < MIN_POSITION 
							&& sellPosition < MIN_POSITION;
		setPrice(current_price);
		setAlarm();
	}
	public void setAlarm() {
		System.out.println("Default Price : "+CUR_PRICE);
		if(is_close_first) createCloseFirst();
		else if(is_open_first) createOpenFirst();
		else createAutoSet();
		main.getAlarmPriceManager().printListString();
	}
	public void setPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		double d = Math.floor((c_price -m -h)/ 10) * 10;
		CUR_PRICE = m + h + d;
	}
	
	public void cancelAllOrder() {
		try {
			String api_key = System.getProperty(CoinConfig.BYBIT_KEY);
			String api_secret = System.getProperty(CoinConfig.BYBIT_SECRET);
			OrderRest.cancelAllOrder(api_key,api_secret,"BTCUSDT");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	public void createOpenFirst() {
		System.out.println("Open First");
		createShortOverOpen();
		createShortUnderOpen();
		createLongOverOpen();
		createLongUnderOpen();
	}
	public void createCloseFirst() {
		System.out.println("Close First");
		createShortOverClose();
		createShortUnderClose();
		createLongOverClose();
		createLongUnderClose();
	}
	public void createAutoSet() {
		System.out.println("Auto  Set " +(is_long_more ? "Long" : "Short"));
		if(is_long_more) {
			//double close_price = Math.floor(CUR_PRICE + (PRICE_STEP/2));
			//double open_price  = close_price - MIN_PROFIT;
			//makeLongClose(CUR_PRICE, OVER, close_price, QTY, open_price, ONCE);
			//makeShortOpen(CUR_PRICE, OVER, close_price, QTY, open_price, RR);
			createLongOverClose();
			createLongUnderClose();
			createShortOverOpen();
			createShortUnderOpen();
		}else {
			//double open_price = Math.floor(CUR_PRICE - (PRICE_STEP/2));
			//double close_price  = open_price + MIN_PROFIT;
			//makeLongOpen(CUR_PRICE, UNDER, open_price, QTY, close_price, RR);
			//makeShortClose(CUR_PRICE, UNDER, open_price, QTY, close_price, ONCE);
			createLongOverOpen();
			createLongUnderOpen();
			createShortOverClose();
			createShortUnderClose();
		}
		
		
	}
	
	/** SHORT **/
	public void createShortOverOpen() {
		for(double i = CUR_PRICE; i < CUR_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i ;
			double close_price  = i - PRICE_STEP;
			double open_price = close_price +  MIN_PROFIT;
			
			/*AlarmPrice openAlarm = addOpenShort(over_trigger, OVER, open_price, QTY, ONCE);
			AlarmPrice closeAlarm = new AlarmPrice(open_price, OVER, RR);
			closeAlarm.setCloseShortAction(close_price, QTY);
			openAlarm.setNextAlarm(closeAlarm);*/
			makeShortOpen(over_trigger, OVER, open_price, QTY, close_price, RR);
			//createOpenShort(over_trigger, OVER, open_price, QTY, close_price, RR);
		}
	}
	public void createShortUnderOpen() {
		for(double i = CUR_PRICE; i > CUR_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i - PRICE_STEP;
			double close_price  = i-MIN_PROFIT;
			double open_price = close_price +  MIN_PROFIT;
			/*AlarmPrice openAlarm = addOpenShort(under_trigger, UNDER, open_price, QTY, ONCE);
			AlarmPrice closeAlarm = new AlarmPrice(open_price, OVER, RR);
			closeAlarm.setCloseShortAction(close_price, QTY);
			openAlarm.setNextAlarm(closeAlarm);*/
			makeShortOpen(under_trigger, UNDER, open_price, QTY, close_price, RR);
		}
	}
	public void makeShortOpen(double trigger, boolean is_over, double open_price, double qty, double close_price,boolean is_repeat) {
		AlarmPrice openAlarm = addOpenShort(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new AlarmPrice(open_price, OVER, is_repeat);
		closeAlarm.setCloseShortAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
	}
	
	public void createShortOverClose() {
		for(double i = CUR_PRICE; i < CUR_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i ;
			double close_price  = i  - PRICE_STEP;
			double open_price = close_price +  MIN_PROFIT;
			/*AlarmPrice closeAlarm = addCloseShort(over_trigger, OVER, close_price, QTY, ONCE);
			AlarmPrice openAlarm = new AlarmPrice(close_price, UNDER, RR);
			openAlarm.setOpenShortAction(open_price, QTY);
			closeAlarm.setNextAlarm(openAlarm);*/
			makeShortClose(over_trigger, OVER, close_price, QTY, open_price, RR);
			
		}
	}
	public void createShortUnderClose() {
		for(double i = CUR_PRICE; i > CUR_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i - PRICE_STEP;
			double open_price  = i;
			double close_price = i - MIN_PROFIT;
			/*AlarmPrice closeAlarm = addCloseShort(under_trigger, UNDER, close_price, QTY, ONCE);
			AlarmPrice openAlarm = new AlarmPrice(close_price, UNDER, RR);
			openAlarm.setOpenShortAction(open_price, QTY);
			closeAlarm.setNextAlarm(openAlarm);*/
			makeShortClose(under_trigger, UNDER, close_price, QTY, open_price, RR);
			//createCloseShort(under_trigger, UNDER, close_price, QTY, open_price, RR);
		}
	}
	public void makeShortClose(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) {
		AlarmPrice closeAlarm = addCloseShort(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new AlarmPrice(close_price, UNDER, is_repeat);
		openAlarm.setOpenShortAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
	}
	/** SHORT **/
	
	public void createLongOverOpen() {
		for(double i = CUR_PRICE + PRICE_STEP ; i < CUR_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i ;
			double open_price  = i - PRICE_STEP;
			double close_price = open_price +  MIN_PROFIT;
			/*AlarmPrice openAlarm = addOpenLong(over_trigger, OVER, open_price, QTY, ONCE);
			AlarmPrice closeAlarm = new AlarmPrice(open_price, UNDER, RR);
			closeAlarm.setCloseLongAction(close_price, QTY);
			openAlarm.setNextAlarm(closeAlarm);*/
			makeLongOpen(over_trigger, OVER, open_price, QTY, close_price, RR);
		}
		
	}
	public void createLongUnderOpen() {
		for(double i = CUR_PRICE; i > CUR_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i + PRICE_STEP;
			double open_price  = i-PRICE_STEP;
			double close_price = open_price +  MIN_PROFIT;
			/*AlarmPrice openAlarm = addOpenLong(under_trigger, UNDER, open_price, QTY, ONCE);
			AlarmPrice closeAlarm = new AlarmPrice(open_price, UNDER, RR);
			closeAlarm.setCloseLongAction(close_price, QTY);
			openAlarm.setNextAlarm(closeAlarm);*/
			makeLongOpen(under_trigger, UNDER, open_price, QTY, close_price, RR);
			/*createOpenLong(under_trigger, UNDER, open_price, QTY, close_price, RR);*/
		}
	}
	public void makeLongOpen(double trigger, boolean is_over, double open_price, double qty, double close_price, boolean is_repeat) {
		AlarmPrice openAlarm = addOpenLong(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new AlarmPrice(open_price, UNDER, is_repeat);
		closeAlarm.setCloseLongAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
	}
	public void createLongOverClose() {
		for(double i = CUR_PRICE; i < CUR_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i ;
			double open_price  = i  - PRICE_STEP;
			double close_price = open_price +  MIN_PROFIT;
			/*createCloseLong(over_trigger, OVER, close_price, QTY, open_price, RR);*/
			makeLongClose(over_trigger, OVER, close_price, QTY, open_price, RR);
		}
		
	}
	public void createLongUnderClose() {
		for(double i = CUR_PRICE; i > CUR_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i - PRICE_STEP;
			double open_price  = i - MIN_PROFIT;
			double close_price = i;
			/*AlarmPrice closeAlarm = addCloseLong(under_trigger, UNDER, close_price, QTY, ONCE);
			AlarmPrice openAlarm = new AlarmPrice(close_price, OVER, RR);
			openAlarm.setOpenLongAction(open_price, QTY);
			closeAlarm.setNextAlarm(openAlarm);*/
			makeLongClose(under_trigger, UNDER, open_price, QTY, close_price, RR);
		}
	}
	public void makeLongClose(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) {
		AlarmPrice closeAlarm = addCloseLong(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new AlarmPrice(close_price, OVER, is_repeat);
		openAlarm.setOpenLongAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
	}
	
	/* ###################    OPEN LONG   ##################### 
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
	
	 ###################    CLOSE LONG   ##################### 
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
	
	 ###################    OPEN SHORT   ##################### 
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
	
	 ###################    CLOSE SHORT   ##################### 
	
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
	}*/
	
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
	
	
	
	
	///** TEST VERSION **/
	 public void test() {
		 double max_price = 19410;
		 System.out.println("##현재가 : "+max_price+" ###########");
		 openShortOver(max_price);
		 System.out.println();
		 openShortUnder(max_price);
		 System.out.println();
		 closeShortOver(max_price);
		 System.out.println();
		 closeShortUnder(max_price);
		 System.out.println("##현재가 : "+max_price+" ###########\n");
		 openLongOver(max_price);
		 System.out.println();
		 openLongUnder(max_price);
		 System.out.println();
		 closeLongOver(max_price);
		 System.out.println();
		 closeLongUnder(max_price);
	 }
	
	
	public void openShortOver(double cur_price) {
		for(double i=cur_price; i < cur_price +100; i+=PRICE_STEP) {
			double over_trigger = i ;
			double close_price  = i - PRICE_STEP;
			double open_price = close_price +  MIN_PROFIT;
			System.out.println(over_trigger + " 이상   \t#"+open_price + " open short  \t#" + close_price + " close short");
			//createOpenShort(over_trigger, OVER, open_price, QTY, close_price, RR);
		}
	 }
	 public void openShortUnder(double cur_price) {
			for(double i = cur_price; i > cur_price-100;  i-=PRICE_STEP) {
				double under_trigger = i - PRICE_STEP;
				double close_price  = i-MIN_PROFIT;
				double open_price = close_price +  MIN_PROFIT;
				System.out.println(under_trigger + " 이하    \t#" + open_price + " open short\t#" + close_price + " close short");
				/*AlarmPrice openAlarm = addOpenShort(under_trigger, UNDER, open_price, QTY, ONCE);
				AlarmPrice closeAlarm = new AlarmPrice(open_price, OVER, RR);
				closeAlarm.setCloseShortAction(close_price, QTY);
				openAlarm.setNextAlarm(closeAlarm);*/
			}
	 }
	 
	public void closeShortOver(double cur_price) {
		for(double i=cur_price; i < cur_price +100; i+=PRICE_STEP) {
			double over_trigger = i ;
			double close_price  = i  - PRICE_STEP;
			double open_price = close_price +  MIN_PROFIT;
			System.out.println(over_trigger + "이상   \t#"+close_price + " close short \t#" + open_price + " open short");
			/*AlarmPrice closeAlarm = addCloseShort(over_trigger, OVER, close_price, QTY, ONCE);
			AlarmPrice openAlarm = new AlarmPrice(close_price, UNDER, RR);
			openAlarm.setOpenShortAction(open_price, QTY);
			closeAlarm.setNextAlarm(openAlarm);*/
		}
	}
	public void closeShortUnder(double cur_price) {
		for(double i = cur_price; i > cur_price-100;  i-=PRICE_STEP) {
			double under_trigger = i - PRICE_STEP;
			double open_price  = i;
			double close_price = i - MIN_PROFIT;
			
			System.out.println(under_trigger + " 이하    \t#"+close_price + " close short \t#" + open_price + " open short");
			/*createCloseShort(under_trigger, UNDER, close_price, QTY, open_price, RR);*/
		}
	}
	
	
	
	/*** OPEN **/
	 public void openLongOver(double cur_price) {
			for(double i=cur_price + PRICE_STEP; i < cur_price +100; i+=PRICE_STEP) {
				double over_trigger = i ;
				double open_price  = i - PRICE_STEP;
				double close_price = open_price +  MIN_PROFIT;
				System.out.println(over_trigger + " 이상   \t#"+open_price + " open long \t#" + close_price + " close long");
				/*AlarmPrice openAlarm = addOpenLong(over_trigger, OVER, open_price, QTY, ONCE);
				AlarmPrice closeAlarm = new AlarmPrice(open_price, UNDER, RR);
				openAlarm.setCloseLongAction(close_price, QTY);
				openAlarm.setNextAlarm(closeAlarm);*/
			}
	 }
	 public void openLongUnder(double cur_price) {
			for(double i = cur_price; i > cur_price-100;  i-=PRICE_STEP) {
				double under_trigger = i + PRICE_STEP;
				double open_price  = i-PRICE_STEP;
				double close_price = open_price +  MIN_PROFIT;
				System.out.println(under_trigger + " 이하    \t#"+ open_price + "open long\t#" + close_price + " close long");
			/*	createOpenLong(under_trigger, UNDER, open_price, QTY, close_price, RR);*/
			}
	 }
	 
	public void closeLongOver(double cur_price) {
		for(double i=cur_price; i < cur_price +100; i+=PRICE_STEP) {
			double over_trigger = i ;
			double open_price  = i  - PRICE_STEP;
			double close_price = open_price +  MIN_PROFIT;
			System.out.println(over_trigger + "이상   \t#"+close_price + " close long \t#" + open_price + " open long");
			//createCloseLong(over_trigger, OVER, close_price, QTY, open_price, RR);
		}
	}
	public void closeLongUnder(double cur_price) {
		for(double i = cur_price; i > cur_price-100;  i-=PRICE_STEP) {
			double under_trigger = i - PRICE_STEP;
			double open_price  = i - MIN_PROFIT;
			double close_price = i;
			System.out.println(under_trigger + " 이하    \t#"+close_price + "close long \t#" + open_price + " open long");
			/*AlarmPrice closeAlarm = addCloseLong(under_trigger, UNDER, close_price, QTY, ONCE);
			AlarmPrice openAlarm = new AlarmPrice(close_price, OVER, RR);
			openAlarm.setOpenLongAction(open_price, QTY);
			closeAlarm.setNextAlarm(openAlarm);*/
		}
	}
	
	
}
