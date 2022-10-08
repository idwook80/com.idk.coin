package com.idk.coin.bybit.account;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.bybit.BybitClient;

public class WalletRest {

	 public static final String bybit_url  ="https://api.bybit.com";
	
	  /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public  static double getWalletBalance(String api_key,String secret,String coin) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        
    	String url = bybit_url + "/v2/private/wallet/balance";
    	
        String tt = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
        map.put("api_key", api_key);
        map.put("timestamp", tt);
        map.put("coin", coin);
        String signature = BybitClient.genSign(secret,map);
        map.put("sign", signature);
        
        String response = BybitClient.get(url, map);
        if(response != null) {
        	return parsing(response);
        }
        return 0;
        
    }
    public static double parsing(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
      //  System.out.println(gson.toJson(el));
        
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        double ret_code = (Double)map.get("ret_code");
       
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        
        LinkedTreeMap usdt  = (LinkedTreeMap) result.get("USDT");
        double equity = (double)usdt.get("equity");
        return equity;
        
    }
}
