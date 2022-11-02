package com.idk.coin.bybit.account;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.bybit.BybitClient;
import com.idk.coin.bybit.BybitUtil;
import com.idk.coin.bybit.model.Position;

public class OrderRest {
	 public static final String bybit_url  ="https://api.bybit.com";

	    /**
	     * POST: get the active order list
	     * @throws NoSuchAlgorithmException
	     * @throws InvalidKeyException
	     */
	    public static void cancelAllOrder (String api_key,String api_secret,String symbol) throws NoSuchAlgorithmException, InvalidKeyException {
	    	String url = bybit_url + "/private/linear/order/cancel-all";
	        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
	            @Override
	            public int compare(String s1, String s2) {
	                return s1.compareTo(s2);
	            }
	        });
	        String tt = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
	        map.put("api_key", api_key);
	        map.put("timestamp", tt);
	        map.put("symbol", symbol);
	        String signature = BybitClient.genSign(api_secret,map);
	        map.put("sign", signature);
	        
	        String response = BybitClient.post(url, map);
	        if(response != null) {
	        	parsing(response);
	        }
	    }
	    public static void cancelOrder (String api_key,String api_secret,String symbol,String order_id) throws NoSuchAlgorithmException, InvalidKeyException {
	    	String url = bybit_url + "/private/linear/order/cancel-all";
	        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
	            @Override
	            public int compare(String s1, String s2) {
	                return s1.compareTo(s2);
	            }
	        });
	        String tt = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
	        map.put("api_key", api_key);
	        map.put("timestamp", tt);
	        map.put("order_id", order_id);
	        map.put("symbol", symbol);
	        String signature = BybitClient.genSign(api_secret,map);
	        map.put("sign", signature);
	        
	        String response = BybitClient.post(url, map);
	        if(response != null) {
	        	parsing(response);
	        }
	    }
	    public static double parsing(String str) {
	    	JsonParser parser = new JsonParser();
	        JsonElement el =  parser.parse(str);
	        //System.out.println(el);
	        
	        
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        System.out.println(gson.toJson(el));
	        Map<String, Object> map  = gson.fromJson(str, Map.class);
	        //LinkedTreeMap result = (LinkedTreeMap)map.get("result");
	        
	        double ret_code = (Double)map.get("ret_code");
	        
	        return ret_code;
	        
	    }
	}