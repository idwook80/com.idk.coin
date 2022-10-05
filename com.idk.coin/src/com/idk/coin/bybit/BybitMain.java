package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.util.ANSI;

public class BybitMain {
	public static boolean OVER  = true;
	public static boolean UNDER = false;
	public static double  DEFAULT_QTY = 0.1;
	
	public static void main(String[] args) {
		new BybitMain();
	}
	public static Logger LOG =   LoggerFactory.getLogger(BybitMain.class.getName());
	 
	BybitMarket bybitMarket;
	BybitRealTime bybitRealTime;
	AlarmPriceManager alarmPriceManager;
	
	public BybitMain() {
		CoinConfig.loadConfig();
		init();
		start();
		LOG.info("Start Bybit Alarm System!!");
	}
	public void init() {
		alarmPriceManager = new AlarmPriceManager();
		setAlarm();
		bybitRealTime = new BybitRealTime(alarmPriceManager);
		
	}
	public void setAlarm() {
		createShort();
		setShort();
		createLong();
		setLong();
	}

	public void createShort() {
		addCloseShort(21910,OVER,21810,0.1,true);
		addCloseShort(21710,OVER,21610,0.1,true);
		addCloseShort(21510,OVER,21410,0.1,true);
		addCloseShort(21310,OVER,21210,0.1,true);
		addCloseShort(21110,OVER,21010,0.1,true);
		addOpenShort(21200,OVER,21260,0.1);
		addCloseShort(21200,OVER,21205,0.3);
		
		createOpenShort(21000,OVER,21310,21510,21710,21910);
		
		addCloseShort(21010,OVER,20910,0.1,true);
		addCloseShort(20910,OVER,20810,0.1,true);
		addCloseShort(20810,OVER,20710,0.1,true);
		addCloseShort(20710,OVER,20610,0.1,true);
		addOpenShort(20700,OVER,20760,0.1);
		addCloseShort(20700,OVER,20705,0.3);
		addCloseShort(20610,OVER,20510,0.1,true);
		createOpenShort(20510,OVER,20610,20710,20810,20910,21010);
		
		addCloseShort(20510,OVER,20410,0.1,true);
		addOpenShort(20400,OVER,20460,0.1);
		addCloseShort(20400,OVER,20405,0.3);
		addCloseShort(20410,OVER,20310,0.1,true);
		addCloseShort(20310,OVER,20210,0.1,true);
		addOpenShort(20300,OVER,20360,0.1);
		addCloseShort(20300,OVER,20305,0.2);
		addCloseShort(20210,OVER,20110,0.1,true);
		addCloseShort(20110,OVER,20010,0.1,true);
		createOpenShort(20010,OVER,20110,20210,20310,20410,20510);
		
		
	}
	public void setShort() {
		
		//---------------------------------->
		addCloseShort(20010,OVER,19910,0.1,true);
		addCloseShort(19910,OVER,19810,0.1,true);
		addCloseShort(19810,OVER,19710,0.1,true);
		addCloseShort(19710,OVER,19610,0.1,true);
		addCloseShort(19610,OVER,19510,0.1,true);
		//createOpenShort(19510,OVER,19610,19710,19810,19910,20010);
		
		//createCloseShort(19510,UNDER,19010,19110,19210,19310,19410);
		addOpenShort(19410,UNDER,19510,0.1,true);
		addOpenShort(19310,UNDER,19410,0.1,true);
		addOpenShort(19210,UNDER,19310,0.1,true);
		addOpenShort(19110,UNDER,19210,0.1,true);
		addOpenShort(19010,UNDER,19110,0.1,true);
		//---------------------------------->
		createCloseShort(19010,UNDER,18110,18310,18510,18710,18910);
		addOpenShort(18910,UNDER,19010,0.1,true);
		addOpenShort(18710,UNDER,18910,0.1,true);
		addOpenShort(18510,UNDER,18710,0.1,true);
		addOpenShort(18310,UNDER,18510,0.1,true);
		addOpenShort(18110,UNDER,18310,0.1,true);
		
		createCloseShort(18110,UNDER,17110,17310,17510,17710,17910);
		addOpenShort(17910,UNDER,18110,0.1);
		addOpenShort(17710,UNDER,17910,0.1);
		addOpenShort(17510,UNDER,17710,0.1);
		addOpenShort(17310,UNDER,17510,0.1);
		addOpenShort(17110,UNDER,17310,0.1);
	}
	public void setLong() {
		
		addOpenLong(20910,OVER,20710,0.1,true);
		addOpenLong(20710,OVER,20510,0.1,true);
		addOpenLong(20510,OVER,20310,0.1,true);
		addOpenLong(20310,OVER,20110,0.1,true);
		addOpenLong(20110,OVER,20010,0.1,true);
		createCloseLong(20010, OVER, 20110,20310,20510,20710,20910);
		
		//---------------------------------->
		addOpenLong(20010,OVER,19910,0.1,true);
		addOpenLong(19910,OVER,19810,0.1,true);
		addOpenLong(19810,OVER,19710,0.1,true);
		addOpenLong(19710,OVER,19610,0.1,true);
		addOpenLong(19610,OVER,19510,0.1,true);
		//createCloseLong(19510,OVER,19610,19710,19810,19910,20010);
		
		//createOpenLong(19510,UNDER,19010,19110,19210,19310,19410);
		addCloseLong(19410,UNDER,19510,0.1,true);
		addCloseLong(19310,UNDER,19410,0.1,true);
		addCloseLong(19210,UNDER,19310,0.1,true);
		addCloseLong(19110,UNDER,19210,0.1,true);
		addCloseLong(19010,UNDER,19110,0.1,true);
		
		
	//---------------------------------->
		
		createOpenLong(19010,UNDER,18510,18610,18710,18810,18910);
		addCloseLong(18910,UNDER,19010,0.1,true);
		addCloseLong(18810,UNDER,18910,0.1,true);
		addCloseLong(18710,UNDER,18810,0.1,true);
		addCloseLong(18610,UNDER,18710,0.1,true);
		addCloseLong(18600,UNDER,18595,0.2);
		addOpenLong(18600,UNDER,18560,0.1);
		addCloseLong(18510,UNDER,18610,0.1,true);
		
		
		createOpenLong(18510,UNDER,18010,18110,18210,18310,18410);
		addCloseLong(18410,UNDER,18510,0.1,true);
		addCloseLong(18310,UNDER,18410,0.1,true);
		addCloseLong(18300,UNDER,18295,0.3);
		addOpenLong(18300,UNDER,18260,0.1);
		addOpenShort(18300,UNDER,18360,0.1);
		addCloseLong(18210,UNDER,18310,0.1,true);
		addCloseLong(18110,UNDER,18210,0.1,true);
		addCloseLong(18010,UNDER,18110,0.1,true);
		addCloseLong(18000,UNDER,17995,0.2);
		addOpenLong(18000,UNDER,17960,0.1);
		
	}
	public void createLong() {
		
		createOpenLong(18010,UNDER,17510,17610,17710,17810,17910);
		addCloseLong(17910,UNDER,18010,0.1,true);
		addCloseLong(17810,UNDER,17910,0.1,true);
		addCloseLong(17710,UNDER,17810,0.1,true);
		addCloseLong(17700,UNDER,17695,0.2);
		addOpenLong(17700,UNDER,17660,0.1);
		addCloseLong(17610,UNDER,17510,0.1,true);
		addCloseLong(17510,UNDER,17610,0.1,true);
		
		createOpenLong(17510,UNDER,17010,17110,17210,17310,17410);
		addCloseLong(17410,UNDER,17310,0.1,true);
		addCloseLong(17310,UNDER,17410,0.1,true);
		addCloseLong(17210,UNDER,17110,0.1,true);
		addCloseLong(17110,UNDER,17210,0.1,true);
		addCloseLong(17010,UNDER,17110,0.1,true);
		addCloseLong(17000,UNDER,16995,0.2);
		addOpenLong(17000,UNDER,16960,0.1);
		addOpenLong(17000,UNDER,16910,0.1);
		
	}
	
	public void start() {
		bybitMarket = new BybitMarket(alarmPriceManager);
	}
	
	
	/* ###################    OPEN LONG   ##################### */
	public void createOpenLong(double price,boolean is_over,double... prices) {
		for(double p : prices) {
			addOpenLong(price,is_over,p,DEFAULT_QTY);
		}
	}
	
	public void addOpenLong(double price,boolean is_over,double open_price,double qty) {
		addOpenLong(price, is_over, open_price, qty, false);
	}
	
	public void addOpenLong(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createOpenLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    OPEN SHORT   ##################### */
	public void createOpenShort(double price,boolean is_over,double... prices) {
		for(double p : prices) {
			addOpenShort(price,is_over,p,DEFAULT_QTY);
		}
	}
	public void addOpenShort(double price,boolean is_over,double open_price,double qty) {
		addOpenShort(price, is_over, open_price, qty, false);
	}
	
	
	public void addOpenShort(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createOpenShort(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE LONG   ##################### */
	public void createCloseLong(double price,boolean is_over,double... prices) {
		for(double p : prices) {
			addCloseLong(price,is_over,p,DEFAULT_QTY);
		}
	}
	
	public void addCloseLong(double price,boolean is_over,double open_price,double qty) {
		addCloseLong(price, is_over, open_price, qty, false);
	}
	
	public void addCloseLong(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createCloseLong(price, is_over, open_price, qty,is_reverse);
	}
	
	/* ###################    CLOSE SHORT   ##################### */
	
	public void createCloseShort(double price,boolean is_over,double... prices) {
		for(double p : prices) {
			addCloseShort(price,is_over,p,DEFAULT_QTY);
		}
	}
	
	public void addCloseShort(double price,boolean is_over,double open_price,double qty) {
		addCloseShort(price, is_over, open_price, qty, false);
	}
	
	public void addCloseShort(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createCloseShort(price, is_over, open_price, qty,is_reverse);
	}
	
	
	
}