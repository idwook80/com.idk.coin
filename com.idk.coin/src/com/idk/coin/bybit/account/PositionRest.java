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

public class PositionRest {
	 public static final String bybit_url  ="https://api.bybit.com";

    /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static ArrayList<Position> getActiveMyPosition(String symbol,String api_key) throws NoSuchAlgorithmException, InvalidKeyException {
    	String url = bybit_url + "/private/linear/position/list";
        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        String tt = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
        map.put("api_key", api_key);
        map.put("timestamp", tt);
        map.put("symbol", "BTCUSDT");
        //map.put("order_status", "Created,New,Filled,Cancelled");
        //map.put("order_status", "Created,New");
        String signature = BybitClient.genSign(map);
        map.put("sign", signature);
        
        String response = BybitClient.get(url, map);
        if(response != null) {
        	return parsingPosition(response);
        }
        return null;
        
    }
    private static ArrayList<Position> parsingPosition(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        JsonObject el2 =  el.getAsJsonObject();
        JsonArray obj2 = el2.get("result").getAsJsonArray();
        
       // System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       // LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(el, Map.class);
        ArrayList result = (ArrayList)map.get("result");
        Date time_now = BybitUtil.getTimeNow((String)map.get("time_now"));
        
        ArrayList<Position> positions = new ArrayList<Position>();
        for(int i=0; i<result.size(); i++) {
        	 LinkedTreeMap t1 = (LinkedTreeMap)result.get(i);
        	 Position p = new Position(t1);
        	 positions.add(p);
        	 
        	 /*JsonObject o = (JsonObject)obj2.get(i);
        	 p.setUser_id(o.get("user_id").toString());
        	 System.out.println(p.getSide() + " & " + (p.getSide().equals("Buy") ? "Long" : "Short") + " \t: " 
        			 		  + p.getSize()  +  " , "
        	 				  +(p.getSide().equals("Sell") ? "-" : "") + String.format("%.2f", p.getSize()/1.5) + " , "
        	 				  + p.getLiq_price() + " , "
        	 				  + "");
        	 System.out.println(p);*/
        }
        return positions;
    }
}
