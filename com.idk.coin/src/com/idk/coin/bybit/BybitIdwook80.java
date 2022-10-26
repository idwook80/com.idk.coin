package com.idk.coin.bybit;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.alram.AlarmManager01;
import com.idk.coin.bybit.alram.AlarmManager02;
import com.idk.coin.bybit.alram.AlarmManager80;
import com.idk.coin.bybit.alram.BybitAlarmManager;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.AlarmManager;
import com.idk.coin.bybit.model.ExecutionModel;
import com.idk.coin.bybit.model.MarketModel;

public class BybitIdwook80 {
	public static Logger LOG =   LoggerFactory.getLogger(BybitIdwook80.class.getName());
	
	public static void main(String[] args) {
		new BybitIdwook80();
		
	}
	BybitMarketManager  	marketManager;
	BybitExecutionManager 	executionManager;
	BybitAlarmManager   	alarmManager;
	
	BybitUser root;
	public static boolean is_main_account =  false;
	public BybitIdwook80() {
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
		MarketModel btcMarket = marketManager.createMarket("BTCUSDT", root.getApi_key(), root.getApi_secret());
	}
	public void initAlarms() {
		alarmManager = new BybitAlarmManager();
		executionManager = new BybitExecutionManager();
		try {
			ArrayList<BybitUser> users = BybitDao.getInstace().selectUserList();
			for(BybitUser user : users) {
				System.out.println(user);
				String symbol = "BTCUSDT";
				AlarmManager am = null;
				if(user.getAlarm_model().equals("0")) 		am = new AlarmManager80(symbol, user);
				/*else if(user.getAlarm_model().equals("1")) 		am = new AlarmManager01(symbol, user);
				else if(user.getAlarm_model().equals("2"))  am = new AlarmManager02(symbol, user);
				*/
				if(am != null) setAlarmManager(am);
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
			m  = marketManager.createMarket(symbol, root.getApi_key(), root.getApi_secret());
			m.setDebug(true);
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