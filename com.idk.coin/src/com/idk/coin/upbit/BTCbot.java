package com.idk.coin.upbit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.idk.coin.db.upbit.ConfigDao;
import com.idk.coin.db.upbit.OrderDao;
import com.idk.coin.upbit.Upbit.EchoWebSocketListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class BTCbot extends WebSocketListener {
	public static final String accessKey 	 = "IXoeDiXBMiri90J7X7alXJKQqzLeW3U9SCVyCsx3";
	public static final String secretKey 	 = "Ako8ZExhxvIKRvdcSLKkXOX0lNbZr0qFtkDSIgYY";
	public static final String serverUrl 	 = "https://api.upbit.com";
	public static final String market	 	 = "KRW-BTC";
	public static final String currency  	 = "BTC";
	public static final String unit_currency = "KRW";
	Accounts 		accounts;
	Account 		account;
	Account 		krw_account;
	OrderManager 	orderManager;
	BigDecimal 		last_price;
	Date 			last_date;
	public int 		bid_count 			= 0;
	public int 		ask_count 			= 0;
	public int 		bid_cancel_count	= 0;
	public int		ask_cancel_count	= 0;
	
	public  double 	default_volume 		= 0.0003;
	public  double 	default_goal		= 0.0015;
	public  double 	buy_per	  			= -0.08;
	public  int 	buy_wait_timeout	= 5; 		// 5분
	public  int 	sell_wait_timeout   = 180;		// 180분
	public static final DecimalFormat df1 = new DecimalFormat("0.00");
	public static final DecimalFormat df2 = new DecimalFormat("0.0000");
	
	public boolean is_run = false;
	
	public CandleChecker candle_checker;
	OkHttpClient client;
	WebSocket ws;
	
	public BTCbot() {
		candle_checker = new CandleChecker();
		load();
		start();
	}
	public void load() {
		BotConfig config = ConfigDao.getInstace().select("KRW-BTC");
		
		BigDecimal volume  		= new BigDecimal(config.getString("volume"));
		BigDecimal goal  		= new BigDecimal(config.getString("goal"));
		BigDecimal buy  		= new BigDecimal(config.getString("buy"));
		BigDecimal buy_timeout  = new BigDecimal(config.getString("buy_wait_timeout"));
		BigDecimal sell_timeout  = new BigDecimal(config.getString("sell_wait_timeout"));
		
		default_volume = volume.doubleValue();
		default_goal = goal.doubleValue();
		buy_per = buy.doubleValue();
		buy_wait_timeout = buy_timeout.intValue();
		sell_wait_timeout = sell_timeout.intValue();
		
		accounts = new Accounts();
		
		krw_account = new Account(config.getString("unit_currency"), config.getString("unit_currency"));
		account = new Account(config.getString("currency"), config.getString("unit_currency"));
		accounts.addAccount(krw_account);
		accounts.addAccount(account);
		updateAccount();
		orderManager = new OrderManager(config.getString("market"));
		
		try {
			getOrders(market,null,"wait",null);
			Order order = getOrder("23fd2858-11a3-4568-a81b-affea605984b");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start() {
		is_run = true;
		String URL = "wss://api.upbit.com/websocket/v1";
		client = new OkHttpClient();
		
		Request request = new Request.Builder().url(URL).build();
	    ws = client.newWebSocket(request, this);
	 
        // Trigger shutdown of the dispatcher's executor so this process can
        // exit cleanly.
        client.dispatcher().executorService().shutdown();
      
	}
	public void stop() {
		 is_run = false;
		 
	}
	public void reconect() {
		 
	}
	 /**
	   * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
	   * messages.
	   */
	   
	  public void onOpen(WebSocket webSocket, Response response) {
		  System.out.println("Opened WebSocket!");
		  String msg = "[{\"ticket\":\"ticker\"},{\"type\":\"ticker\",\"codes\":[\""+market +"\"]}]";
		  //String msg = "[{\"ticket\":\"ticker\"},{\"type\":\"ticker\",\"codes\":[\"KRW-XRP\"]},{\"format\":\"SIMPLE\"}]";
		  account = accounts.getAccount(currency);
		  webSocket.send(msg);
	  }

	  /** Invoked when a text (type {@code 0x1}) message has been received. */
	  public void onMessage(WebSocket webSocket, String text) {
		  System.out.println(" onMessage Receiving: " + text);
	  }

	  /** Invoked when a binary (type {@code 0x2}) message has been received. */
	  public double min_per = 0.0;
	  public int checkCount = 100;
	  public void onMessage(WebSocket webSocket, ByteString bytes) {
		 
		   //System.out.println("Receiving: " + bytes.utf8());
		   JsonParser jp			= new JsonParser();
		   JsonElement je 			= jp.parse(bytes.utf8());
		   JsonObject obj 			= je.getAsJsonObject();
		   BigDecimal trade_price	= obj.get("trade_price").getAsBigDecimal();
		   Date trade_date 			= new Date(Long.parseLong(obj.get("trade_timestamp").toString()));
		  
		   account.setTrade_price(trade_price);
		   account.setTrade_date(trade_date);
		   
		   checkBuys();
		   checkSells();
		   
		   double profitPer =  account.getProfitPer();
		   
		   if(checkCount--  < 0) {
			   System.out.println("####### Bot Detail ######");
			   System.out.println(account);
			   account.getTotal_balance();
			   
			   is_run = ConfigDao.getInstace().select("KRW-BTC").get("stop").toString().toUpperCase().equals("Y");
			   
			   System.out.println(bytes.utf8());
			   System.out.println(df1.format(account.getProfit()) + " , " + df1.format(account.getProfitPer()));
			   System.out.println("buy : " + bid_count + " , sell : " + ask_count + ", buy cancel: " + bid_cancel_count + ", sell cancel: "+ask_cancel_count);
			   System.out.println("wait order : "+ orderManager.getSize() + "  , buy : " + orderManager.getBuySize() + " , sell : " + orderManager.getSellSize());
			   candle_checker.is_rise();
			   System.out.println("########## MIN[ "+ df2.format(min_per)+" ] ########### stop["+is_run+"]");
			   checkCount = 100;
		   }
		   
		   if(last_date == null) last_date = trade_date;
		   if(last_price == null) last_price = trade_price;
		   
		   double even = trade_price.doubleValue() - last_price.doubleValue();
		   double per = even / last_price.doubleValue() * 100 ;
		   min_per = per < min_per ? per : min_per;
		   if(even != 0.0) {
	    	   Date checkTime = new Date(last_date.getTime() + (1000*30*3));
	    	   if(trade_date.compareTo(checkTime) > 0 || per < buy_per) {
    			   if(per < buy_per && !is_run)  {
    				   if(candle_checker.is_rise()) buy(trade_price);
    			   }
	    		   last_date	= trade_date;
	    		   last_price 	= trade_price;
	    		   min_per 		= 100;
	    	   }
		   }
	  }
	  public void buy(BigDecimal price) {
		  try {
			  System.out.println("<<< SEND BUY PRICE >>> [ " + price  + " ] ");
			  Order order = postOrder(market, Order.SIDE_BID, String.valueOf(default_volume), price.toString(), Order.ORD_TYPE_LIMIT);
			  ZonedDateTime created_at	= ZonedDateTime.parse(order.getCreated_at());
			  ZonedDateTime cancel_at 	= created_at.plusMinutes(buy_wait_timeout);
			  order.setCancel_at(cancel_at.toString());
			  if(order !=null) {
				  orderManager.addBuys(order);
			  }
		  
		  } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  public void checkBuys() {
		 Order[] orders = orderManager.getBuyOrders();
		 if(orders.length <= 0 ) return;
		 for(int i=0; i<orders.length; i++) {
			 String uuid = orders[i].getUuid();
			 Order o;
			try {
				o = getOrder(uuid);
				if( o.getState().equals(Order.STATE_DONE)) {
					 double goal = (o.getPrice().doubleValue() * default_goal) + o.getPrice().doubleValue();
					 goal =  Math.round(goal/1000);
					 goal = goal*1000;
					 System.out.println("goal profit : " + (goal - o.getPrice().doubleValue()) * new BigDecimal(default_volume).doubleValue() );
					 orders[i].setGoal_price(new BigDecimal(goal));
					
					 
					 sell(orders[i],new BigDecimal(goal));
					 orderManager.removeBuys(orders[i]);
					 OrderDao.getInstace().insert(orders[i]);
					 
					 
					 bid_count++;
					 updateAccount();
					 
				 }else if(o.getState().equals(Order.STATE_CANCEL)) {
					 orders[i].setState(o.getState());
					 orderManager.removeBuys(orders[i]);
				 }else {
					 ZonedDateTime cancel_at = ZonedDateTime.parse(orders[i].getCancel_at());
					 if(ZonedDateTime.now().isAfter(cancel_at)) {
						 cancelOrder(o.getUuid());
						 orderManager.removeBuys(orders[i]);
					 }
				 }
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		 }
	  }
	  public void sell(Order buy,BigDecimal price) {
		  try {
			  System.out.println("send sell price! " +  price);
			  Order order = postOrder(market, Order.SIDE_ASK, String.valueOf(default_volume), price.toString(), Order.ORD_TYPE_LIMIT);
			  
			  ZonedDateTime created_at	= ZonedDateTime.parse(order.getCreated_at());
			  ZonedDateTime cancel_at 	= created_at.plusMinutes(sell_wait_timeout);
			  order.setCancel_at(cancel_at.toString());
			  order.setPuuid(buy.getUuid());
			  order.setGoal_price(buy.getPrice());
			  
			  OrderDao.getInstace().insert(order);
			  
			  if(order !=null) {
				  orderManager.addSells(order);
			  }
		  
		  } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  public void checkSells() {
		 Order[] orders = orderManager.getSellOrders();
		 if(orders.length <= 0 ) return;
		 for(int i=0; i<orders.length; i++) {
			 String uuid = orders[i].getUuid();
			 Order o;
			try {
				o = getOrder(uuid);
				if( o.getState().equals(Order.STATE_DONE)) {
					orderManager.removeSells(orders[i]);
					OrderDao.getInstace().update(o);
					ask_count++;
					updateAccount();
				 }else if(o.getState().equals(Order.STATE_CANCEL)) {
					orderManager.removeSells(orders[i]);
					OrderDao.getInstace().update(o);
				 }else {
					ZonedDateTime cancel_at = ZonedDateTime.parse(orders[i].getCancel_at());
					if(ZonedDateTime.now().isAfter(cancel_at)){
						cancelOrder(o.getUuid());
						orderManager.removeSells(orders[i]);
					}
				 }
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
	  }


	  /**
	   * Invoked when the remote peer has indicated that no more incoming messages will be
	   * transmitted.
	   * **/
	
	  public void onClosing(WebSocket webSocket, int code, String reason) {
		    System.out.println("Closing: " + code + " " + reason);
	  }

	  /**
	   * Invoked when both peers have indicated that no more messages will be transmitted and the
	   * connection has been successfully released. No further calls to this listener will be made.
	   */
	  public void onClosed(WebSocket webSocket, int code, String reason) {
		    System.out.println("Closed: " + code + " " + reason);
	  }

	  /**
	   * Invoked when a web socket has been closed due to an error reading from or writing to the
	   * network. Both outgoing and incoming messages may have been lost. No further calls to this
	   * listener will be made.
	   */
	  public void onFailure(WebSocket webSocket, Throwable t,  Response response) {
		  System.out.println("onFailure");
		  t.printStackTrace();
	  }
	public void updateAccount() {
		System.out.println("Upbit getAccount ");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            
            
            String jsonString = EntityUtils.toString(entity, "UTF-8");
            
            JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(jsonString);
    		JsonArray  objArray =  je.getAsJsonArray();
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			accounts.addAccount(jobj);
    		}
    		Account[] ac = accounts.getAccounts();
    		for(Account a : ac) {
    			System.out.println(a);
    		}
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void getOrders(String market,String[] uuids,String state,String[] states) throws NoSuchAlgorithmException, UnsupportedEncodingException {

	    HashMap<String, String> params = new HashMap<>();
	    if(market != null) 	params.put("market", market);
	    if(state != null) 	params.put("state", state);
	    

	  /*  String[] uuids = {
	        "9ca023a5-851b-4fec-9f0a-48cd83c2eaae"
	        // ...
	    };*/

	    ArrayList<String> queryElements = new ArrayList<>();
	    for(Map.Entry<String, String> entity : params.entrySet()) {
	        queryElements.add(entity.getKey() + "=" + entity.getValue());
	    }
	    if(uuids != null) {
			 for(String uuid : uuids) {
			        queryElements.add("uuids[]=" + uuid);
		    }
	    }
	    if(states != null) {
	    	for(String s : states) {
	    		queryElements.add("states[]="+s);
	    	}
	    }
	    String queryString = String.join("&", queryElements.toArray(new String[0]));

	    MessageDigest md = MessageDigest.getInstance("SHA-512");
	    md.update(queryString.getBytes("UTF-8"));

	    String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

	    Algorithm algorithm = Algorithm.HMAC256(secretKey);
	    String jwtToken = JWT.create()
	            .withClaim("access_key", accessKey)
	            .withClaim("nonce", UUID.randomUUID().toString())
	            .withClaim("query_hash", queryHash)
	            .withClaim("query_hash_alg", "SHA512")
	            .sign(algorithm);

	    String authenticationToken = "Bearer " + jwtToken;

	    try {
	        HttpClient client = HttpClientBuilder.create().build();
	        HttpGet request = new HttpGet(serverUrl + "/v1/orders?" + queryString);
	        request.setHeader("Content-Type", "application/json");
	        request.addHeader("Authorization", authenticationToken);

	        HttpResponse response = client.execute(request);
	        HttpEntity entity = response.getEntity();


	        String jsonString = EntityUtils.toString(entity, "UTF-8");
	        JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonString);
			JsonArray  objArray = je.isJsonArray() ? je.getAsJsonArray() : null;
			for(int i=0; i<objArray.size(); i++) {
				JsonObject jobj  = objArray.get(i).getAsJsonObject();
				 
			}
			
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public Order getOrder(String uuid) throws NoSuchAlgorithmException, UnsupportedEncodingException {

	    HashMap<String, String> params = new HashMap<>();
	    params.put("uuid", uuid);

	    ArrayList<String> queryElements = new ArrayList<>();
	    for(Map.Entry<String, String> entity : params.entrySet()) {
	        queryElements.add(entity.getKey() + "=" + entity.getValue());
	    }

	    String queryString = String.join("&", queryElements.toArray(new String[0]));

	    MessageDigest md = MessageDigest.getInstance("SHA-512");
	    md.update(queryString.getBytes("UTF-8"));

	    String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

	    Algorithm algorithm = Algorithm.HMAC256(secretKey);
	    String jwtToken = JWT.create()
	            .withClaim("access_key", accessKey)
	            .withClaim("nonce", UUID.randomUUID().toString())
	            .withClaim("query_hash", queryHash)
	            .withClaim("query_hash_alg", "SHA512")
	            .sign(algorithm);

	    String authenticationToken = "Bearer " + jwtToken;
	    Order order = null;
	    
	    try {
	        HttpClient client = HttpClientBuilder.create().build();
	        HttpGet request = new HttpGet(serverUrl + "/v1/order?" + queryString);
	        request.setHeader("Content-Type", "application/json");
	        request.addHeader("Authorization", authenticationToken);

	        HttpResponse response = client.execute(request);
	        HttpEntity entity = response.getEntity();

	       // System.out.println(EntityUtils.toString(entity, "UTF-8"));
	        
	        String jsonString = EntityUtils.toString(entity, "UTF-8");
	        JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonString);
			JsonObject obj = je.getAsJsonObject();
			String uid = obj.get("uuid").getAsString();
			
		    order= new Order(uid);
			order.setJsonObject(obj);
			
	    } catch (IOException e) {
	        e.printStackTrace();
	        order = null;
	    }
		return order;
	}
	public Order postOrder(String market,String side,String volume,String price, String ord_type) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //{"uuid":"23fd2858-11a3-4568-a81b-affea605984b","side":"bid","ord_type":"limit","price":"37823000.0","state":"wait","market":"KRW-BTC","created_at":"2022-05-22T00:52:55+09:00","volume":"0.0002","remaining_volume":"0.0002","reserved_fee":"3.7823","remaining_fee":"3.7823","paid_fee":"0.0","locked":"7568.3823","executed_volume":"0.0","trades_count":0}
        HashMap<String, String> params = new HashMap<>();
        params.put("market",  market);
        params.put("side", side);
        params.put("volume", volume);
        params.put("price", price);
        params.put("ord_type", ord_type);

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));
        
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));
        
        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;
        Order order = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(serverUrl + "/v1/orders");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);
            
            request.setEntity(new StringEntity(new Gson().toJson(params)));
        
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            /*System.out.println(EntityUtils.toString(entity, "UTF-8"));*/
            String jsonString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(jsonString);
	        JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonString);
			JsonObject obj = je.getAsJsonObject();
			String uid = obj.get("uuid").getAsString();
		    order= new Order(uid);
			order.setJsonObject(obj);
			
        } catch (IOException e) {
            e.printStackTrace();
        }
        return order;
    }
	
	public void cancelOrder(String uuid) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    HashMap<String, String> params = new HashMap<>();
	    params.put("uuid",uuid);

	    ArrayList<String> queryElements = new ArrayList<>();
	    for(Map.Entry<String, String> entity : params.entrySet()) {
	        queryElements.add(entity.getKey() + "=" + entity.getValue());
	    }

	    String queryString = String.join("&", queryElements.toArray(new String[0]));

	    MessageDigest md = MessageDigest.getInstance("SHA-512");
	    md.update(queryString.getBytes("UTF-8"));

	    String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

	    Algorithm algorithm = Algorithm.HMAC256(secretKey);
	    String jwtToken = JWT.create()
	            .withClaim("access_key", accessKey)
	            .withClaim("nonce", UUID.randomUUID().toString())
	            .withClaim("query_hash", queryHash)
	            .withClaim("query_hash_alg", "SHA512")
	            .sign(algorithm);

	    String authenticationToken = "Bearer " + jwtToken;

	    try {
	        HttpClient client = HttpClientBuilder.create().build();
	        HttpDelete request = new HttpDelete(serverUrl + "/v1/order?" + queryString);
	        request.setHeader("Content-Type", "application/json");
	        request.addHeader("Authorization", authenticationToken);

	        HttpResponse response = client.execute(request);
	        HttpEntity entity = response.getEntity();

	       // System.out.println(EntityUtils.toString(entity, "UTF-8"));
	        String jsonString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(jsonString);
	        JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonString);
			JsonObject obj = je.getAsJsonObject();
			String side = obj.get("side").getAsString();
			if(side.equals(Order.SIDE_BID)) bid_cancel_count++;
			else if(side.equals(Order.SIDE_ASK)) ask_cancel_count++;
			
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
