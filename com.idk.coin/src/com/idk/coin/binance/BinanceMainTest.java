package com.idk.coin.binance;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.trade.AccountInformation;
import com.idk.coin.CoinConfig;
import com.idk.coin.binance.alarm.BinanceAlarmManager;
import com.idk.coin.binance.model.BinanceMarketModel;
import com.idk.coin.bybit.BybitMarketManager;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.alram.BybitAlarmManager;
import com.idk.coin.bybit.db.BybitBalanceDao;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.ExecutionModel;
import com.idk.coin.bybit.model.MarketModel;
import com.idk.coin.model.AlarmManager;
import com.idk.coin.util.TimeUtil;

public class BinanceMainTest {
	public static Logger LOG =   LoggerFactory.getLogger(BinanceMainTest.class.getName());
	
	public static void main(String[] args) {
		new BinanceMainTest();
		
	}
	
	BinanceMarketModel      marketModel;
	//BybitMarketManager  	marketManager;
	//BybitExecutionManager 	executionManager;
	BybitAlarmManager   	alarmManager;
	
	BybitUser root;
	public static boolean is_main_account =  false;
	public BinanceMainTest() {
		load();
		init();
		start();
		LOG.info("Start Binance Alarm System!!");
	}
	public void load() {
		loadConfig();
		loadDB();
	}
	public void loadDB() {
		try {
			root = 	BybitDao.getInstace().selectUser("binance01", "1122");
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
		//marketManager = new BybitMarketManager();
		//MarketModel btcMarket = marketManager.createMarket("BTCUSDT", root.getApi_key(), root.getApi_secret());
		marketModel = new BinanceMarketModel("BTCUSDT", root.getApi_key(), root.getApi_secret());
	
	}
	public void initAlarms() {
		alarmManager = new BybitAlarmManager();
		//executionManager = new BybitExecutionManager();
		try {
			ArrayList<BybitUser> users = BybitDao.getInstace().selectUserList();
			for(BybitUser user : users) {
				System.out.println(user);
				String symbol = "BTCUSDT";
				AlarmManager am = null;
				if(user.getAlarm_model().equals("10")) 		am = new BinanceAlarmManager(symbol, user);
				if(am != null) setAlarmManager(am);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setAlarmManager(AlarmManager am) {
		alarmManager.addAlarmManager(am);
		BinanceMarketModel m = getMarketModel(am.symbol);
	    m.addPriceListener(am);
		//ExecutionModel em = getExecutionModel(am.user);
		//em.addPriceListener(am);
	}
	public BinanceMarketModel getMarketModel(String symbol) {
		/*MarketModel m = marketManager.getMarketModel(symbol);
		if(m == null) {
			m  = marketManager.createMarket(symbol, root.getApi_key(), root.getApi_secret());
			m.setDebug(true);
		}
		return m;*/
		return marketModel;
	}
	public ExecutionModel getExecutionModel(BybitUser user) {
		//ExecutionModel em = executionManager.getExecutionModel(user.getId());
		//if(em == null) em = executionManager.createExecution(user);
		return null;
	}
	
	public void start() {
	  //  marketManager.startAllMarkets();
		//executionManager.startAllExecutions();
		marketModel.startMarket();
		alarmManager.startAllAlarms();
		if(alarmManager.getAlarmManager("BTCUSDT", "binance01") != null) {
			checkBalance();
		}
		
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
				if(user.getAlarm_model().equals("10")){
					Balance balance = BybitBalanceDao.getInstace().selectBalance(user.getId(), symbol, TimeUtil.getCurrentTime("yyyy-MM-dd"));
					System.out.println("DB Balance :  " + balance);
					if(balance == null) {
						balance = new Balance();
						balance.setId(user.getId());
						balance.setSymbol("USDT");
						balance.setEquity(getBalance(user.getApi_key(), user.getApi_secret()));
						balance.setReg_date(new Date());
						balance.setReg_datetime(new Date());
						
						ret = BybitBalanceDao.getInstace().insert(balance) > 0 ;
					}else {
						ret = true;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	public double getBalance(String api_key, String api_secret) {
		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(api_key, api_secret, options);
		AccountInformation ai = syncRequestClient.getAccountInformation();
		return ai.getTotalMarginBalance().doubleValue();
	}
}