package com.idk.coin.binance.alarm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.enums.WorkingType;
import com.binance.client.model.trade.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.bybit.model.OrderExecution;
import com.idk.coin.model.TradeModel;

public class BinanceTrade extends TradeModel{
	
	public BinanceTrade() {
		super();
	}
	public  Object placeActiveOrder(String api_key, String api_secret,String symbol, String side,String position_idx,double price,double qty) throws NoSuchAlgorithmException, InvalidKeyException {
       this.API_KEY = api_key;
       this.API_SECRET = api_secret;
		if(side.toUpperCase().equals(SIDE_BUY.toUpperCase()) && position_idx.equals(POSITION_IDX_LONG)) 
				return openLongOrder(symbol, String.valueOf(price), String.valueOf(qty));
		if(side.toUpperCase().equals(SIDE_SELL.toUpperCase()) && position_idx.equals(POSITION_IDX_SHORT)) 
				return openShortOrder(symbol, String.valueOf(price), String.valueOf(qty)	);
		if(side.toUpperCase().equals(SIDE_SELL.toUpperCase()) && position_idx.equals(POSITION_IDX_LONG)) 
				return closeLongOrder(symbol, String.valueOf(price), String.valueOf(qty)	);
		if(side.toUpperCase().equals(SIDE_BUY.toUpperCase()) && position_idx.equals(POSITION_IDX_SHORT)) 
				return closeShortOrder(symbol, String.valueOf(price), String.valueOf(qty)	);
		return null;
    }
	public Object postOrder(String symbol,OrderSide side,PositionSide positionSide, String price, String qty){
		OrderType 		orderType 			= OrderType.LIMIT;
        TimeInForce 	timeInForce 		= TimeInForce.GTC;
        String 			reduceOnly 			= null;
        String 			newClientOrderId 	= null;
        String 			stopPrice 			= null;
        WorkingType 	workingType 		= null;
        NewOrderRespType newOrderRespType 	= NewOrderRespType.RESULT;
    	RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(API_KEY, API_SECRET, options);
        Order order = syncRequestClient.postOrder(symbol, side, positionSide, orderType, timeInForce,
                qty, price, reduceOnly, newClientOrderId, stopPrice, workingType, NewOrderRespType.RESULT);
        return order;
	}
	public Object openLongOrder(String symbol, String price, String qty) {
		return postOrder(symbol,OrderSide.BUY, PositionSide.LONG, price, qty);
	}
	public Object openShortOrder(String symbol, String price, String qty) {
		return postOrder(symbol,OrderSide.SELL, PositionSide.SHORT, price, qty);
	}
	public Object closeLongOrder(String symbol, String price, String qty) {
		return postOrder(symbol, OrderSide.SELL, PositionSide.LONG, price, qty);
	}
	public Object closeShortOrder(String symbol, String price, String qty) {
		return postOrder(symbol, OrderSide.BUY, PositionSide.SHORT, price, qty);
	}
    public double parsing(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        //System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        OrderExecution order = new OrderExecution(result);
        
        double ret_code = (Double)map.get("ret_code");
        
        return ret_code;
    }
    public Object parsingOrder(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        //System.out.println(el);
        
        
     /*   Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        Order order = new Order(result);
        
        double ret_code = (Double)map.get("ret_code");
        
        if(ret_code == 0) return order;*/
        return null;
    }
    
   
    public Object executAction(String api_key, String api_secret, String symbol) {
    	try {
    		return placeActiveOrder(api_key, api_secret,symbol, side,position_idx, price, qty);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
}
