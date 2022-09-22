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
		setShort();
		setLong();
	}
	
	public void setShort() {
		addCloseShort(21910,OVER,21810,0.1,true);
		addCloseShort(21710,OVER,21610,0.1,true);
		addCloseShort(21510,OVER,21410,0.1,true);
		addCloseShort(21310,OVER,21210,0.1,true);
		
		addOpenShort(21200,OVER,21260,0.1);
		addCloseShort(21200,OVER,21205,0.2);
		addCloseShort(21110,OVER,21010,0.1,true);
		addCloseShort(20910,OVER,20810,0.1,true);
		addCloseShort(20710,OVER,20610,0.1,true);
		
		addOpenShort(20700,OVER,20760,0.1);
		addCloseShort(20700,OVER,20705,0.2);
		addCloseShort(20510,OVER,20410,0.1,true);
		addCloseShort(20310,OVER,20210,0.1,true);
		addCloseShort(20210,OVER,20110,0.1,true);
		
		addOpenShort(20200,OVER,20260,0.1);
		addCloseShort(20200,OVER,20205,0.2);
		addCloseShort(20110,OVER,20010,0.1,true);
		addCloseShort(19910,OVER,19810,0.1,true);
		addCloseShort(19710,OVER,19610,0.1,true);
		addCloseShort(19610,OVER,19560,0.1,true);
		addCloseShort(19510,OVER,19460,0.1,true);
		addCloseShort(19410,OVER,19360,0.1,true);
		addCloseShort(19310,OVER,19260,0.1,true);
		addCloseShort(19210,OVER,19110,0.1,true);
		
		addOpenShort(19010,UNDER,19110,0.1,true);
		addOpenShort(18760,UNDER,18960,0.1,true);
		addOpenShort(18710,UNDER,18910,0.1,true);
		addOpenShort(18510,UNDER,18710,0.1,true);
		addOpenShort(18410,UNDER,18610,0.1,true);
	}
	public void setLong() {
		
		addOpenLong(19910,OVER,19710,0.1,true);
		addOpenLong(19710,OVER,19510,0.1,true);
		addOpenLong(19510,OVER,19310,0.1,true);
		addOpenLong(19310,OVER,19110,0.1,true);

		
		
		addCloseLong(19010,UNDER,19110,0.1,true);
		addCloseLong(18910,UNDER,19010,0.1,true);
		addCloseLong(18760,UNDER,18860,0.1,true);
		addCloseLong(18710,UNDER,18810,0.1,true);
		addCloseLong(18660,UNDER,18760,0.1,true);
		addCloseLong(18610,UNDER,18710,0.1,true);
		addCloseLong(18510,UNDER,18610,0.1,true);
		addCloseLong(18460,UNDER,18510,0.1,true);
		addCloseLong(18410,UNDER,18460,0.1,true);
		
		
		addCloseLong(18310,UNDER,18410,0.1,true);
		addCloseLong(18110,UNDER,18210,0.1,true);
		addCloseLong(18000,UNDER,17995,0.2);
		addOpenLong(18000,UNDER,17960,0.1);
		
		
		addCloseLong(17910,UNDER,18010,0.1,true);
		addCloseLong(17710,UNDER,17810,0.1,true);
		
		addCloseLong(17700,UNDER,17695,0.2);
		addOpenLong(17700,UNDER,17660,0.1);
		
		addCloseLong(17510,UNDER,17610,0.1,true);
		addCloseLong(17310,UNDER,17410,0.1,true);
		addCloseLong(17110,UNDER,17210,0.1,true);
	
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