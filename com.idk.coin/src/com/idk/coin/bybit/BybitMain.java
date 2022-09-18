package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;

public class BybitMain {
	public static boolean OVER  = true;
	public static boolean UNDER = false;
	
	public static void main(String[] args) {
		new BybitMain();
	}
	public static Logger LOG =   LoggerFactory.getLogger(BybitMain.class.getName());
	 
	BybitMarket bybitMarket;
	AlarmPriceManager alarmPriceManager;
	
	public BybitMain() {
		CoinConfig.loadConfig();
		
		alarmPriceManager = new AlarmPriceManager();
		init();
		start();
		LOG.info("Start Bybit Alarm System!!");
	}
	public void init() {
		addCloseShort(22810,OVER,22812,0.2);
		
		addCloseShort(21110,OVER,21010,0.1);
		addCloseShort(20910,OVER,20810,0.1);
		addCloseShort(20710,OVER,20610,0.1);
		addCloseShort(20510,OVER,20410,0.1);
		addCloseShort(20110,OVER,20010,0.1,true);
		addCloseShort(20010,OVER,19910,0.1,true);
		
		addOpenLong(20510,OVER,20410,0.1);
		addOpenLong(20310,OVER,20210,0.1);
		addOpenLong(20210,OVER,20110,0.1);

		addOpenLong(20110,OVER,20010,0.1,true);
		addOpenLong(20060,OVER,19960,0.1,true);
		addOpenLong(20010,OVER,19910,0.1,true);
		addOpenLong(19960,OVER,19860,0.1,true);
		
		//addCloseLong(19860,UNDER,19960,0.1,true);
		addCloseLong(19810,UNDER,19860,0.1,true);
		addCloseLong(19760,UNDER,19810,0.1,true);
		addCloseLong(19710,UNDER,19810,0.1,true);
		addCloseLong(19510,UNDER,19560,0.1,true);
		addCloseLong(19310,UNDER,19360,0.1);
		addCloseLong(19110,UNDER,19160,0.1);
		addCloseLong(18910,UNDER,18960,0.1);
		addCloseLong(18710,UNDER,18760,0.1);
		
		addCloseLong(19410,UNDER,19405,0.2);
		addCloseLong(19210,UNDER,19205,0.2);
		addCloseLong(19010,UNDER,19005,0.2);
	}
	
	public void start() {
		bybitMarket = new BybitMarket(alarmPriceManager);
	}
	
	
	public void addOpenLong(double price,boolean is_over,double open_price,double qty) {
		addOpenLong(price, is_over, open_price, qty, false);
	}
	
	public void addOpenLong(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createOpenLong(price, is_over, open_price, qty,is_reverse);
	}
	
	public void addOpenShort(double price,boolean is_over,double open_price,double qty) {
		addOpenShort(price, is_over, open_price, qty, false);
	}
	
	public void addOpenShort(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createOpenShort(price, is_over, open_price, qty,is_reverse);
	}
	
	public void addCloseLong(double price,boolean is_over,double open_price,double qty) {
		addCloseLong(price, is_over, open_price, qty, false);
	}
	
	public void addCloseLong(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createCloseLong(price, is_over, open_price, qty,is_reverse);
	}
	
	public void addCloseShort(double price,boolean is_over,double open_price,double qty) {
		addCloseShort(price, is_over, open_price, qty, false);
	}
	
	public void addCloseShort(double price,boolean is_over,double open_price,double qty,boolean is_reverse) {
		alarmPriceManager.createCloseShort(price, is_over, open_price, qty,is_reverse);
	}
	
	
	
}
