package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.model.AlarmManagerModel;

public class BybitMain {
	public static void main(String[] args) {
		new BybitMain();
		
	}
	public static Logger LOG =   LoggerFactory.getLogger(BybitMain.class.getName());
	 
	BybitMarket		 	bybitMarket;
	BybitRealTime 	 	bybitRealTime;
	AlarmPriceManager 	alarmPriceManager;
	PositionManager 	positionManager;
	AlarmManagerModel	alarmManager;
	public static boolean is_main_account =  false;
	public BybitMain() {
		CoinConfig.loadConfig();
		loadUserInfo();
		init();
		start();
		LOG.info("Start Bybit Alarm System!!");
	}
	public void init() {
		positionManager 	= new PositionManager(this);
		alarmPriceManager   = new AlarmPriceManager();
		bybitRealTime 		= new BybitRealTime(this);
		bybitMarket 		= new BybitMarket(this, !is_main_account);
	}
	public void loadUserInfo() {
		if(is_main_account) {
			
		}else {
			String sub_key 		=  System.getProperty(CoinConfig.BYBIT_SUB_KEY);
			String sub_secret 	= System.getProperty(CoinConfig.BYBIT_SUB_SECRET);
			System.setProperty(CoinConfig.BYBIT_KEY, sub_key);
			System.setProperty(CoinConfig.BYBIT_SECRET, sub_secret);
		}
	}
	public void start() {
		if(is_main_account) alarmManager = new MainAlarmManager(this);
		else 				alarmManager = new SubAlarmManager(this);
	}
	
	public AlarmManagerModel getAlarmManagerModel() {
		return alarmManager;
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