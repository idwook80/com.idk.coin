package com.idk.coin.bybit.alram;

import java.util.ArrayList;

import com.idk.coin.AlarmSound;
import com.idk.coin.bybit.account.OrderRest;
import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class AlarmManager03 extends BybitAlarmsModel {
	
	public static double  DEF_PRICE		 	= 0.0;
	public static double  PRICE_STEP		= 50;
	public final  double  MAX_PRICE 		= 2000;
	public static int auto_change_minutes 	= 60*12;
	public static int change_trigger = auto_change_minutes+1;
	public static double  MAX_POSITION		= 0.040;
	public static double  MIN_POSITION		= 0.010;
	public static boolean is_open_first		= true;
	public static boolean is_close_first 	= true;
	public static boolean is_long_more		= true;
	
	public AlarmManager03(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager03(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		setDefault_qty(0.001);
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
	  
	}
	public CalculateModel createCalculateModel(BybitAlarmsModel parent,double price,Position buy,
	 		Position sell, Balance balance,double qty, boolean debug) {
	 return new CalculatePositionV3(parent, price, buy, sell, balance, qty, debug);
	}
	
	
	public void run() {
		System.out.println("Alarm03 Start! " +current_price);
		while(is_run) {
		
			try {
				if( current_price > MAX_PRICE) break;
				Thread.sleep(1000 * 1);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		while(is_run) {
			try {
				if(auto_change_minutes <  ++change_trigger) {
					changePosition();
				}
				Thread.sleep(1000 * 60);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		clearAllAlarms();
		cancelAllOrder();
	}
	
	public void changePosition() throws Exception {
		AlarmSound.playReset();
		cancelAllOrder();
		double last_price = current_price;
		resetAlarm(Math.floor(last_price));
		change_trigger = 0;
		System.out.println("Changed Alarm Values");
	}
	
	public void cancelAllOrder() {
		try {
			OrderRest.cancelAllOrder(user.getApi_key(),user.getApi_secret(),symbol);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void resetAlarm(double c_price) throws Exception {
		System.out.println("Alarm Rest Current Price : " + c_price);
		clearAllAlarms();
		ArrayList<Position> ps = PositionRest.getActiveMyPosition(user.getApi_key(),user.getApi_secret(), symbol);
		Position buy =  Position.getPosition(ps, symbol, "Buy");
		Position sell = Position.getPosition(ps, symbol, "Sell");
		
		double buyPosition = buy.getSize();
		double sellPosition = sell.getSize();
		is_long_more = buyPosition > sellPosition;
		
		is_close_first = buyPosition > MAX_POSITION
							&& sellPosition > MAX_POSITION;
		is_open_first  = buyPosition < MIN_POSITION 
							&& sellPosition < MIN_POSITION;
		setPrice(c_price);
		alarmReSet();
	}
	public void setPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		double d = Math.floor((c_price -m -h)/ 10) * 10;
		DEF_PRICE = m + h + d;
	}
	
	
	public void alarmSet() throws Exception {
 
	}
	public void alarmReSet() throws Exception {
		System.out.println(current_price + " Default Price : "+DEF_PRICE);
		try {
			//if(is_close_first) createCloseFirst();
			//else if(is_open_first) createOpenFirst();
			//createAutoSet();
		}catch(Exception e) {
			e.printStackTrace();
			clearAllAlarms();
		}
		LOG.info(printListString());
	}
	public void createOpenFirst() throws Exception {
		System.out.println("Open First");
		createShortOverOpen();
		createShortUnderOpen();
		createLongOverOpen();
		createLongUnderOpen();
	}
	public void createCloseFirst()  throws Exception {
		System.out.println("Close First");
		createShortOverClose();
		createShortUnderClose();
		createLongOverClose();
		createLongUnderClose();
	}
	public void createAutoSet() throws Exception {
		System.out.println("Auto  Set " +(is_long_more ? "Long" : "Short"));
		createLongOverClose();
		createLongUnderOpen();
		
		createShortOverOpen();
		createShortUnderClose();
	}
	public void createAutoSetDefault() throws Exception {
		createLongOverClose();
		createLongUnderOpen();
		
		createShortOverOpen();
		createShortUnderClose();
	}
	
	/** SHORT **/
	public void createShortOverOpen() throws Exception {
		for(double i = DEF_PRICE; i < DEF_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i - PRICE_STEP;
			double close_price  = i;
			double open_price = close_price + PRICE_STEP;
			makeOpenShort(over_trigger, OVER, open_price, QTY, close_price, RR);
		}
	}
	public void createShortUnderOpen() throws Exception {
		for(double i = DEF_PRICE; i > DEF_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i - PRICE_STEP;
			double close_price  = i-MIN_PROFIT;
			double open_price = close_price +  MIN_PROFIT;
			makeOpenShort(under_trigger, UNDER, open_price, QTY, close_price, RR);
		}
	}
	
	public void createShortOverClose() throws Exception {
		for(double i = DEF_PRICE; i < DEF_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i ;
			double close_price  = i  - PRICE_STEP;
			double open_price = close_price +  MIN_PROFIT;
			makeCloseShort(over_trigger, OVER, close_price, QTY, open_price, RR);
			
		}
	}
	public void createShortUnderClose() throws Exception {
		for(double i = DEF_PRICE; i > DEF_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i + PRICE_STEP;
			double open_price  = i;
			double close_price = i - PRICE_STEP;
			makeCloseShort(under_trigger, UNDER, close_price, QTY, open_price, RR);
		}
	}
	
	public void createLongOverOpen() throws Exception {
		for(double i = DEF_PRICE + PRICE_STEP ; i < DEF_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i ;
			double open_price  = i - PRICE_STEP;
			double close_price = open_price +  MIN_PROFIT;
			makeOpenLong(over_trigger, OVER, open_price, QTY, close_price, RR);
		}
		
	}
	public void createLongUnderOpen() throws Exception {
		for(double i = DEF_PRICE; i > DEF_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i + PRICE_STEP;
			double open_price  = i-PRICE_STEP;
			double close_price = open_price +  PRICE_STEP;
			makeOpenLong(under_trigger, UNDER, open_price, QTY, close_price, RR);
		}
	}
	public void createLongOverClose() throws Exception {
		for(double i = DEF_PRICE; i < DEF_PRICE + MAX_PRICE;  i+=PRICE_STEP) {
			double over_trigger = i - PRICE_STEP ;
			double open_price  = i;
			double close_price = open_price +  PRICE_STEP;
			makeCloseLong(over_trigger, OVER, close_price, QTY, open_price, RR);
		}
		
	}
	public void createLongUnderClose() throws Exception {
		for(double i = DEF_PRICE; i > DEF_PRICE - MAX_PRICE;  i-=PRICE_STEP) {
			double under_trigger = i - PRICE_STEP;
			double open_price  = i - MIN_PROFIT;
			double close_price = i;
			makeCloseLong(under_trigger, UNDER, close_price , QTY, open_price, RR);
		}
	}
}
