package com.idk.coin.db.upbit;

import java.util.HashMap;

import com.idk.coin.db.CoinDBManager;
import com.idk.coin.upbit.BotConfig;

public class ConfigDao extends DaoModel {
	public volatile static ConfigDao instance;
	
	public synchronized  static ConfigDao getInstace(){
		if(instance == null){
			synchronized(OrderDao.class){
				instance = new ConfigDao();
			}
		}
		return instance;
	}
	public BotConfig select(String market) {
		BotConfig config = new BotConfig();
		CoinDBManager mgr = CoinDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT * from upbit.config A");

		if(market != null) {
			queryBuffer.append(" WHERE A.market = '"+ market +"'");
		}
		queryBuffer.append(" ORDER BY A.created_at DESC");
		
		try {
			HashMap  map = mgr.selectHashMap(queryBuffer.toString());
			if(map != null) {
				config.setHashMap(map);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		return config;
	}

}
