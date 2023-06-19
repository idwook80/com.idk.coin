package com.idk.coin.bybit.service;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.BybitExecutionManager;
import com.idk.coin.bybit.BybitMarketManager;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.alram.AlarmManager80;
import com.idk.coin.bybit.alram.BybitAlarmManager;
import com.idk.coin.bybit.db.BybitBalanceDao;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.ExecutionModel;
import com.idk.coin.bybit.model.MarketModel;
import com.idk.coin.model.AlarmManager;
import com.idk.coin.util.TimeUtil;

public class BTCAlarm80Service {
	public static Logger LOG =   LoggerFactory.getLogger(BTCAlarm80Service.class.getName());
	
	public static void main(String[] args) {
		new BTCAlarm80Service();
		
	}
	BybitMarketManager  	marketManager;
	BybitExecutionManager 	executionManager;
	BybitAlarmManager   	alarmManager;
	
	BybitUser root;
	public static boolean is_main_account =  false;
	public BTCAlarm80Service() {
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
		MarketModel btcMarket = marketManager.createMarket("BTCUSDT", root.getApi_key(), root.getApi_secret(), true);
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
			m  = marketManager.createMarket(symbol, root.getApi_key(), root.getApi_secret(),false);
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
		if(alarmManager.getAlarmManager("BTCUSDT", "idwook80") != null) {
			checkBalance();
		}
		//checkUserBalances();
		
	}
	public void checkBalance() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				int sleeptime = 10;
				while(true) {
					try {
					System.out.println("Check User Balances");
					Date date = new Date();
					System.out.println(TimeUtil.getDateFormat(date));
					int hour = date.getHours();
					if(7 <= hour && hour < 8) {
							if(checkUserBalances()) sleeptime = 60;
					}else if( 6 <= hour && hour < 7) sleeptime = 10;
					 else sleeptime = 60;
					
					Thread.sleep(1000*60*sleeptime);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
	public boolean checkUserBalances() {
		boolean ret = false;
		try {
		ArrayList<BybitUser> users = BybitDao.getInstace().selectUserList();
		String symbol = "USDT";
			for(BybitUser user : users) {
				Balance balance = BybitBalanceDao.getInstace().selectBalance(user.getId(), symbol, TimeUtil.getCurrentTime("yyyy-MM-dd"));
				System.out.println("DB Balance :  " + balance);
				if(balance == null && Integer.parseInt(user.getAlarm_model()) < 10)  {
					balance 		 =   WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
					balance.setId(user.getId());
					balance.setSymbol(symbol);
					balance.setReg_date(new Date());
					balance.setReg_datetime(new Date());
					ret = BybitBalanceDao.getInstace().insert(balance) > 0 ;
				}else {
					ret = true;
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}