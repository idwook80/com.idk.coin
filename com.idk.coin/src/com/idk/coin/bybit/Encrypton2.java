package com.idk.coin.bybit;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.AlarmSound;
import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.model.Position;


import io.contek.invoker.bybit.api.common._Order;
import io.contek.invoker.bybit.api.common._Position;


public class Encrypton2 {
	static String API_KEY = "xx";
    static String API_SECRET = "xx";
    
    final static String TIMESTAMP 	= Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    final static String RECV_WINDOW = "10000";
    
    public static Logger LOG =   LoggerFactory.getLogger(Encrypton2.class.getName());
    
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
    	
    	Encrypton2 encryptionTest = new Encrypton2();
    	
    	//encryptionTest.getServerTime();
    	encryptionTest.getActiveOrder();
    
    	//encryptionTest.getActiveMyPosition();
    	//encryptionTest.placeActiveOrder();
    	//encryptionTest.getActiveKline();
    	/*for(;;) {
    		encryptionTest.getActiveKline();
    		try {
				Thread.sleep(1000*10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}*/
    }
    public Encrypton2() {

    	CoinConfig.loadConfig();
    	
    	//API_KEY 	= System.getProperty(CoinConfig.BYBIT_KEY);
    	//API_SECRET 	= System.getProperty(CoinConfig.BYBIT_SECRET);
    	
    }
    public void getServerTime() throws NoSuchAlgorithmException, InvalidKeyException {
    	 
        String url = "https://api.bybit.com";
        url  	 += "/v2/public/time";
        String response = BybitClient.get(url);
        if(response != null) {
        	printJson(response);
        }
    }
    
    /**
     * POST: place an active linear perpetual order
     */
    private void placeActiveOrder() throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> map = new TreeMap(
            new Comparator<String>() {
                @Override
                // sort paramKey in A-Z
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
            }
        });
        map.put("api_key", API_KEY);
        map.put("timestamp", TIMESTAMP);
        map.put("side", "Buy");
        map.put("symbol", "BTCUSDT");
        map.put("order_type", "Limit");
        map.put("qty", "0.001");
        map.put("price", "18760");
        map.put("time_in_force", "GoodTillCancel");
        //map.put("take_profit", "20000");
        //map.put("stop_loss", "18000");
        map.put("reduce_only", false);
        map.put("close_on_trigger", false);
        map.put("recv_window", RECV_WINDOW);
        String signature = BybitClient.genSign(API_SECRET,map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com/private/linear/order/create";
        String response = BybitClient.post(url, map);
        if(response != null) {
        	System.out.println(response);
        }
    }
    

    /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public void getActiveMyPosition() throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        map.put("api_key", API_KEY);
        map.put("timestamp", TIMESTAMP);
        map.put("symbol", "BTCUSDT");
        //map.put("order_status", "Created,New,Filled,Cancelled");
        //map.put("order_status", "Created,New");
        String signature = BybitClient.genSign(API_SECRET,map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com";
        url  	 += "/private/linear/position/list";
        String response = BybitClient.get(url, map);
        if(response != null) {
        	parsingPosition(response);
        }
        
    }
    public void parsingPosition(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        JsonObject el2 =  el.getAsJsonObject();
        JsonArray obj2 = el2.get("result").getAsJsonArray();
        
       // System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       // LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(el, Map.class);
        ArrayList result = (ArrayList)map.get("result");
        Date time_now = getTimeNow((String)map.get("time_now"));
        
        for(int i=0; i<result.size(); i++) {
        	 LinkedTreeMap t1 = (LinkedTreeMap)result.get(i);
        	 Position p = new Position(t1);
        	 JsonObject o = (JsonObject)obj2.get(i);
        	 p.setUser_id(o.get("user_id").toString());
        	 System.out.println(p.getSide() + " & " + (p.getSide().equals("Buy") ? "Long" : "Short") + " \t: " 
        			 		  + p.getSize()  +  " , "
        	 				  +(p.getSide().equals("Sell") ? "-" : "") + String.format("%.2f", p.getSize()/1.5) + " , "
        	 				  + p.getLiq_price() + " , "
        	 				  + "");
        	 System.out.println(p);
        }
    }
    /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public void getActiveOrder() throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        map.put("api_key", API_KEY);
        map.put("timestamp", TIMESTAMP);
        map.put("symbol", "BTCUSDT");
        //map.put("order_status", "Created,New,Filled,Cancelled");
        map.put("order_status", "Created,New");
        String signature = BybitClient.genSign(API_SECRET,map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com/private/linear/order/list";
        String response = BybitClient.get(url, map);
        if(response != null) {
        	parsingOrder(response);
        }
        
    }
    public void parsingOrder(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
       // LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        Date time_now = getTimeNow((String)map.get("time_now"));
        System.out.println(result.get("current_page"));
        System.out.println(result.get("data"));
        ArrayList data = (ArrayList)result.get("data");
        for(int i=0; i<data.size(); i++) {
        	LinkedTreeMap order = (LinkedTreeMap)data.get(i);
        	_Order o = new _Order();
        	 LOG.info(gson.toJson(order));
        	o.order_type = order.get("order_type").toString();
        	o.price		 = Double.valueOf(order.get("price").toString());
        	double qty	 = Double.valueOf(order.get("qty").toString());
        	o.side		 = order.get("side").toString();
        	System.out.println(o.order_type + " , " +o.side + " , " +o.price + " , " + " , " + qty + " , " );
        }
    }
    /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public void getActiveAPIInfo() throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        map.put("api_key", API_KEY);
        map.put("timestamp", TIMESTAMP);
        String signature = BybitClient.genSign(API_SECRET,map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com/v2/private/account/api-key";
        String response = BybitClient.get(url, map);
        if(response != null) {
        	System.out.println(response);
        }
    }
    /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public void getActiveKline() throws NoSuchAlgorithmException, InvalidKeyException {
    	  Map<String, Object> map = new TreeMap<>(new Comparator<String>() {
              @Override
              public int compare(String s1, String s2) {
                  return s1.compareTo(s2);
              }
          });
    	  
    	String TIMESTAMP_SEC = Long.toString((ZonedDateTime.now().toInstant().toEpochMilli() / 1000) - (60*1*5));
        map.put("api_key", API_KEY);
        map.put("timestamp", TIMESTAMP);
        map.put("symbol", "BTCUSDT");
        map.put("interval", "5");
        map.put("from", TIMESTAMP_SEC);
        map.put("limit", "1");
        String signature = BybitClient.genSign(API_SECRET,map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com/public/linear/kline";
        String response = BybitClient.get(url, map);
        if(response != null) {
        	printJson(response);
        	lineParsing(response);
        	
        }
        
    }
    
    
    public static double last_close 			= 0;
    public static double last_volume 			= 0;
    public static boolean pre_beep 				= false;
    public static boolean is_alarm 				= false;
    public static double  volume_per_sec		= 	10;
    public static double  volume_max			= 	500;
    
    public static void lineParsing(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        //System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        ArrayList result = (ArrayList)map.get("result");
        Date time_now = getTimeNow((String)map.get("time_now"));
        
        for(int i=0; i<result.size(); i++) {
       	 LinkedTreeMap t1 = (LinkedTreeMap)result.get(i);
       	 double open  = Double.valueOf(t1.get("open").toString());
       	 double price = Double.valueOf(t1.get("close").toString());
       	 double volume = Double.valueOf(t1.get("volume").toString());
       	 Date start_at = getTimeNow(t1.get("start_at").toString());
       	 Date open_time = getTimeNow(t1.get("open_time").toString());
       	 
       	 if(last_volume == 0 || time_now.getSeconds() < 1 || time_now.getSeconds() >= 59) last_volume  = volume;
       	 double per_volume = volume - last_volume;
       	 last_volume = volume;
       	 LOG.info(i + " : " + time_now.toGMTString()
       			 + ",\t#" + open   
       			 + ",\t#" + price  
       			 + ",\t#" + (open < price ? "↑↑" : "↓↓") 
       			 + ",\t#" + String.format("%.2f",volume) 
       			 + ",\t#" + String.format("%.2f",per_volume) 
       			 + "," + (per_volume > 10 || volume > 500 ? (volume > 500 ? "◀◀◀◀◀" : "◁◁◁◁◁") : "")
       			 + ", \t# " + start_at.toGMTString() + " , " + open_time.toGMTString());
        // LOG.info(i + " : " + start_at.toGMTString() + " , " + open_time.toGMTString());
       	 
       	 if(volume > volume_max) {
       		 AlarmSound.alarm01();
       		 is_alarm = true;
       	 }else {
       		 if(per_volume > volume_per_sec) {
           		 if(pre_beep) AlarmSound.beep04();
           		 AlarmSound.beep01();
           		 pre_beep = true;
           	 }else pre_beep = false;
       	 }
       	 if(volume < volume_max || time_now.getSeconds() < 10 || time_now.getSeconds() > 58) is_alarm = false;
        }
    }
    
    public static Date getTimeNow(String time) {
    	double ret = Double.valueOf(time).doubleValue() * 1000 + (1000 * 60 * 60 * 9)-1000;
    	return new Date(Double.valueOf(ret).longValue());
    }
    public static void printJson(String str) {
             
         	JsonParser parser = new JsonParser();
             JsonElement el =  parser.parse(str);
             //System.out.println(el);
             
             Gson gson = new GsonBuilder().setPrettyPrinting().create();
             LOG.info(gson.toJson(el));
             
    }
    
}
