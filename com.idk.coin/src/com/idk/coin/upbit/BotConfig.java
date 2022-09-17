package com.idk.coin.upbit;

import java.util.HashMap;

public class BotConfig {
	public static final double 		default_volume 		= 0.0003;
	public static final double 		default_goal		= 0.0015;
	public static final double 		buy_per	  			= -0.08;
	public static final int 		buy_wait_timeout	= 5; // 5분
	public static final int 		sell_wait_timeout   = 180; // 180분
	
	public static final String 		market	 	 		= "KRW-BTC";
	public static final String 		currency  	 		= "BTC";
	public static final String 		unit_currency 		= "KRW";
	
	HashMap<String,Object> map;
	
	public BotConfig() {
		map = new HashMap<>();
	}
	public void setHashMap(HashMap d) {
	this.map = (HashMap) d.clone();
	
	}
	
	public void put(String key,Object value) {
		map.put(key, value);
	}
	public Object get(String key) {
		return map.get(key);
	}
	public String getString(String key) {
		return (String)map.get(key).toString();
	}
	public double getDouble(String key) {
		return Double.valueOf(map.get(key).toString());
	}
}
