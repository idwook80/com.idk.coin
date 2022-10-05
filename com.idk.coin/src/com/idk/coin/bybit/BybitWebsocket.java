package com.idk.coin.bybit;
import java.awt.Font;
import java.io.Console;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

@ClientEndpoint
public class BybitWebsocket {
	
	 @OnOpen
	    public void onOpen(Session session) {
	        System.out.println("Connected to endpoint: " + session.getBasicRemote());
	        try {
	            BybitWssClient.session=session;
	            System.out.println(BybitWssClient.session);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    @OnMessage
	    public void processMessage(String message) {
	        //System.out.println("Received message in client: " + message);
	    	lineParsing(message);
	    }

	    @OnError
	    public void processError(Throwable t) {
	        t.printStackTrace();
	    }
	    public final static String PLUSTICK 	= "PlusTick";
	    public final static String MINUSTIC		= "MinusTick";
	    public final static String ZERO_PLUS 	= "ZeroPlusTick";
	    public final static String ZERO_MINUS 	= "ZeroMinusTick";
	    static Queue<TradeValue> queue = new LinkedList<TradeValue>();
	    
	    public static void lineParsing(String str) {
	    	JsonParser parser = new JsonParser();
	        JsonElement el =  parser.parse(str);
	        //System.out.println(el);
	        
	        
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        //LOG.info(gson.toJson(el));
	        Map<String, Object> map  = gson.fromJson(str, Map.class);
	        String topic = (String)map.get("topic");
	        if(topic == null || !topic.equals("trade.BTCUSDT")) return;
	        
	        ArrayList data = (ArrayList)map.get("data");
	        
	        for(int i=0; i<data.size(); i++) {
	       	 LinkedTreeMap t1 = (LinkedTreeMap)data.get(i);
	       	 double price  	= Double.valueOf(t1.get("price").toString());
	       	 double size 	= Double.valueOf(t1.get("size").toString());
	       	 String side 	= t1.get("side").toString();
	       	 String tick_direction	= t1.get("tick_direction").toString();
	       	 String timestamp = t1.get("timestamp").toString();
	       	 
	       	addTrade( new TradeValue(price, size, side));
	       	double total = buyTotal + sellTotal;
	       	total = 100/total;
	       	// if(size > 1) {
		       	 System.out.print(price + " # ");
		       	System.out.print(String.format("%.3f",size) + " "
		       			 			+ side.charAt(0)  + "|"
		       			 			+ getDirectionTick(tick_direction) + "\t|"
		       			 			+ "["+buyCount+"] "+ String.format("%.2f",buyTotal) +"(" + String.format("%.2f",buyTotal * total) +"%) | " 
		       			 			+  ( buyTotal > sellTotal ? "▲▲" : "▽▽") + " "
		       			 			+ " ["+sellCount+"] "+ String.format("%.2f",sellTotal) +"("+ String.format("%.2f",sellTotal * total) +"%) | "
		       			 			+ (size > 2 ?  " ◁◁  " + getDirectionTick(tick_direction) + " " + String.format("%.3f",size) : "") 
		       			 		);
		       			 		System.out.print("\n");
	       	// }
	       	 
	        }
	    }
	    public static String getDirectionTick(String str) {
	    	if(str.equals(PLUSTICK)){
	    		return "▲▲";
	    	}else if(str.equals(ZERO_PLUS)){
	    		return "▲▲";
	    	}else if(str.equals(MINUSTIC)) {
	    		return "▽▽";
	    	}else if(str.equals(ZERO_MINUS)) {
	    		return "▽▽";
	    	}
	    	return "";
	    }
	    public static void addTrade(TradeValue t) {
	    	synchronized (queue) {
	    		if(queue.size() > 1000 ) queue.remove();
		    	queue.add(t);
			}
	    	setSideValue();
	    }
	    
	    static double buyTotal = 0.0;
	    static 	int   buyCount = 0;
	    static double sellTotal = 0.0;
	    static  int   sellCount = 0;
	    
	    public static void setSideValue() {
	    	buyCount = 0;
	    	buyTotal = 0.0;
	    	sellCount = 0;
	    	sellTotal = 0.0;
	    	synchronized (queue) {
	    		for(TradeValue t : queue) {
		    		if(t.side  == 1) {
		    			buyTotal += t.size;
		    			buyCount++;
		    		}else {
		    			sellTotal += t.size;
		    			sellCount++;
		    		}
		    	}
			}
	    	
	    }
	      
	   static class TradeValue {
	    	double price;
	    	double size;
	    	int side;		//1 Buy ,2 Sell
	        TradeValue(double price, double size,String side) {
	    		this.price = price;
	    		this.size = size;
	    		this.side = side.equals("Buy") ? 1 : 2;
	    	}
	    }
}
