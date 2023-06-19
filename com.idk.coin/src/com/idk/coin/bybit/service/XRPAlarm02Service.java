package com.idk.coin.bybit.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.BybitExecutionManager;
import com.idk.coin.bybit.BybitMarketManager;
import com.idk.coin.bybit.alram.BybitAlarmManager;
import com.idk.coin.bybit.alram.XRPAlarmManager02;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.ExecutionModel;
import com.idk.coin.bybit.model.MarketModel;
import com.idk.coin.model.AlarmManager;

public class XRPAlarm02Service {
	public static Logger LOG =   LoggerFactory.getLogger(XRPAlarm02Service.class.getName());
	
	public static void main(String[] args) {
		new XRPAlarm02Service();
		
	}
	BybitMarketManager  	marketManager;
	BybitExecutionManager 	executionManager;
	BybitAlarmManager   	alarmManager;
	
	BybitUser root;
	public static boolean is_main_account =  false;
	public XRPAlarm02Service() {
		load();
		init();
		start();
		LOG.info("Start Bybit Alarm System!!");
	}
	public void load() {
		loadConfig();
		loadDB();
	}
	public void loadDB() {
		try {
			root = 	BybitDao.getInstace().selectUser("idwook80", "1122");
			System.out.println(root);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void loadConfig() {
		CoinConfig.loadConfig();
	}
	
	public void init() {
		initMarket();
		initAlarms();
	}
	public void initMarket() {
		marketManager = new BybitMarketManager();
		//MarketModel btcMarket = marketManager.createMarket("BTCUSDT", root.getApi_key(), root.getApi_secret());
		//btcMarket.startMarket();
		
		MarketModel xrpMarket = marketManager.createMarket("XRPUSDT", root.getApi_key(), root.getApi_secret(),true);
		//xrpMarket.startMarket();
		
		//MarketModel etcMarket = marketManager.createMarket("ETCUSDT", root.getApi_key(), root.getApi_secret());
		//etcMarket.startMarket();
	}
	public void initAlarms() {
		alarmManager = new BybitAlarmManager();
		executionManager = new BybitExecutionManager();
		try {
			ArrayList<BybitUser> users = BybitDao.getInstace().selectUserList();
			for(BybitUser user : users) {
				String symbol = "XRPUSDT";
				AlarmManager am = null;
				
				//if(user.getAlarm_model().equals("0")) 		am = new AlarmManager80(symbol, user);
				 if(user.getAlarm_model().equals("3")) 		 	am = new XRPAlarmManager02(symbol, user);
				 //if(user.getAlarm_model().equals("2"))  	am = new AlarmManager02(symbol, user);
				 //else if(user.getAlarm_model().equals("2"))  	am = new AlarmManager02(symbol, user);
				if(am != null) {
					System.out.println(user);
					setAlarmManager(am);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setAlarmManager(AlarmManager am) {
		alarmManager.addAlarmManager(am);
		MarketModel m = getMarketModel(am.symbol);
	    m.addPriceListener(am);
		ExecutionModel em = getExecutionModel(am.user);
		em.addPriceListener(am);
	}
	public MarketModel getMarketModel(String symbol) {
		MarketModel m = marketManager.getMarketModel(symbol);
		if(m == null) {
			m  = marketManager.createMarket(symbol, root.getApi_key(), root.getApi_secret(),false);
		}
		return m;
	}
	public ExecutionModel getExecutionModel(BybitUser user) {
		ExecutionModel em = executionManager.getExecutionModel(user.getId());
		if(em == null) em = executionManager.createExecution(user);
		return em;
	}
	
	public void start() {
		 marketManager.startAllMarkets();
		 executionManager.startAllExecutions();
		 alarmManager.startAllAlarms();
	}
	
}