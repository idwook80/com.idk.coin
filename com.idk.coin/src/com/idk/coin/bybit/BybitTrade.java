package com.idk.coin.bybit;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.bybit.model.Order;
import com.idk.coin.bybit.model.OrderExecution;
import com.idk.coin.model.TradeModel;

public class BybitTrade extends TradeModel{
    
    public BybitTrade() {
    	//CoinConfig.loadConfig();
    	//API_KEY = System.getProperty(CoinConfig.BYBIT_KEY);
    	//API_SECRET = System.getProperty(CoinConfig.BYBIT_SECRET);
    }
    
    /**
     * POST: place an active linear perpetual order
     */
    public  Order placeActiveOrder(String api_key, String api_secret,String symbol, String side,String position_idx,double price,double qty) throws NoSuchAlgorithmException, InvalidKeyException {
        
    	return placeActiveOrderV3(api_key, api_secret, symbol, side, position_idx, price, qty);
    	/*
    	Map<String, Object> map = new TreeMap(
            new Comparator<String>() {
                @Override
                // sort paramKey in A-Z
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
            }
        });
        map.put("api_key", api_key);
        map.put("timestamp", getTimestamp());
        map.put("side", side);
        map.put("position_idx",position_idx);
        map.put("symbol", symbol);
        //map.put("order_type", "Limit");
        map.put("orderType", "Limit");
        map.put("qty", qty);
        map.put("price", price);
        //map.put("time_in_force", "GoodTillCancel");
        map.put("timeInForce", "GoodTillCancel");
        //map.put("take_profit", "20000");
        //map.put("stop_loss", "18000");
        map.put("reduce_only", false);
        map.put("close_on_trigger", false);
        map.put("recv_window", RECV_WINDOW);
        String signature = BybitClient.genSign(api_secret,map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com";
        url		  += "/private/linear/order/create";
        
        String response = BybitClient.post(url, map);
        if(response != null) {
        	System.out.println(response);
        	return parsingOrder(response);
        }
        return null;*/
    }
    
    public double parsing(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        //System.out.println(el);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       // LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        OrderExecution order = new OrderExecution(result);
        
        double ret_code = (Double)map.get("ret_code");
        
        return ret_code;
    }
    public Order parsingOrder(String str) {
    	JsonParser parser = new JsonParser();
    	//System.out.println(str);
        JsonElement el =  parser.parse(str);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        Order order = new Order(result);
        
        double ret_code = (Double)map.get("ret_code");
        
        if(ret_code == 0) return order;
        return null;
    }
    
   
    public Order executAction(String api_key, String api_secret,String symbol) {
    	try {
    		 Order order = placeActiveOrder(api_key, api_secret, symbol, side,position_idx, price, qty);
    		return order;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * POST: place an active linear perpetual order
     */
    public  Order placeActiveOrderV3(String api_key, String api_secret,String symbol, String side,String position_idx,double price,double qty) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> map = new TreeMap(
            new Comparator<String>() {
                @Override
                // sort paramKey in A-Z
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
            }
        });
        
        map.put("category", "linear");
        map.put("symbol", symbol);
        map.put("side", side);
        map.put("orderType", "Limit");
        map.put("qty", String.valueOf(qty));
        map.put("price", String.valueOf(price));
        map.put("positionIdx", position_idx);
        map.put("timeInForce", "GoodTillCancel");
        
        
        String url = "https://api.bybit.com";
        url		  += "/contract/v3/private/order/create";
        
        String response = BybitClientV3.post(url, map, api_key, api_secret, getTimestamp(), RECV_WINDOW);
        if(response != null) {
        	System.out.println(response);
        	return parsingOrderV3(response,map.get("symbol").toString()
        								  ,map.get("side").toString()
        								  ,map.get("price").toString()
        								  ,map.get("qty").toString());
        }
        return null;
    }
    public Order parsingOrderV3(String str,String symbol, String side, String price, String qty) {
    	JsonParser parser = new JsonParser();
    	//System.out.println(str);
        JsonElement el =  parser.parse(str);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        
        String order_id 	= result.get("orderId").toString();
        result.put("order_id", order_id);
        result.put("symbol", symbol);
        result.put("side", side);
        result.put("price", price);
        result.put("qty", qty);
        
        Order order = new Order(result);
	 
        double ret_code = (Double)map.get("retCode");
        
        if(ret_code == 0) return order;
        return null;
    }
    
}
