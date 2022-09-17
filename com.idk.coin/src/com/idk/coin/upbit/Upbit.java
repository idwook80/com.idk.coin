package com.idk.coin.upbit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class Upbit {
	public static final String accessKey = "IXoeDiXBMiri90J7X7alXJKQqzLeW3U9SCVyCsx3";
	public static final String secretKey = "Ako8ZExhxvIKRvdcSLKkXOX0lNbZr0qFtkDSIgYY";
	public static final String serverUrl =  "https://api.upbit.com";
	
	Date 	 	 last_date = null;
	BigDecimal	 last_price = null;
	
	Accounts accounts;
	Orders orders;
	
	public Upbit() {
		accounts =new Accounts();
		orders = new Orders();
		start();
	}
	
	public void start() {
		//authenticationToken();
		getAccount();
		try {
			//postOrder();
			//getOrders("KRW-BTC","wait");
			getOrdersDoneCancel("KRW-XRP");
			
			getOrder("23fd2858-11a3-4568-a81b-affea605984b");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentPrice();
	}
	public void currentPrice() {
	
	}
	public void checkWaitOrder() {
		
	}
	
	synchronized public void checkBalance()	{
				getAccount();
				try {
					//postOrder();
					getOrders("KRW-BTC","wait",null);
					getOrder("23fd2858-11a3-4568-a81b-affea605984b");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public void notifyCurrentPrice(Date date, BigDecimal price) {
		//checkBalance();
	}
	public int comparePrice() {
		return 0;
	}
	class EchoWebSocketListener extends WebSocketListener {
		
		 /**
		   * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
		   * messages.
		   */
		   Account act;
		   
		  public void onOpen(WebSocket webSocket, Response response) {
			  System.out.println("On Open WebSocket!");
			  String COIN = "BTC";
			  String msg = "[{\"ticket\":\"ticker\"},{\"type\":\"ticker\",\"codes\":[\"KRW-"+COIN +"\"]}]";
			  //String msg = "[{\"ticket\":\"ticker\"},{\"type\":\"ticker\",\"codes\":[\"KRW-XRP\"]},{\"format\":\"SIMPLE\"}]";
			  act = accounts.getAccount(COIN);
			  System.out.println("SND : 	" + msg);
			  webSocket.send(msg);
			  
		  }

		  /** Invoked when a text (type {@code 0x1}) message has been received. */
		  public void onMessage(WebSocket webSocket, String text) {
			  System.out.println("Receiving: " + text);
		  }

		  /** Invoked when a binary (type {@code 0x2}) message has been received. */
		  public void onMessage(WebSocket webSocket, ByteString bytes) {
			  DecimalFormat df1 = new DecimalFormat("0.00");
			   //System.out.println("Receiving: " + bytes.utf8());
			   JsonParser jp = new JsonParser();
	    	   JsonElement je = jp.parse(bytes.utf8());
	    	   JsonObject obj = je.getAsJsonObject();
	    	  // System.out.println(obj);
	    	   BigDecimal trade_price =  obj.get("trade_price").getAsBigDecimal();
	    	   
	    	   Date trade_date = new Date(Long.parseLong(obj.get("trade_timestamp").toString()));
	    	  
	    	   act.setTrade_price(trade_price);
	    	   act.setTrade_date(trade_date);
	    	   
	    	   double profitPer =  act.getProfitPer();
	    	   if(profitPer > 0.5) System.out.println("매도 : " + df1.format(profitPer) + "," + act.getProfit());
	    	   
	    	   if(last_date == null) last_date = trade_date;
	    	   if(last_price == null) last_price = trade_price;
	    	   
	    	   double even = trade_price.doubleValue() - last_price.doubleValue();
    		   double per = even / last_price.doubleValue() * 100 ;
    		 
    		   if(even != 0.0) {
		    	   Date checkTime = new Date(last_date.getTime() + (1000*60));
		    	   DecimalFormat df = new DecimalFormat("0.000");
		    	   
		    	 /*  System.out.println(trade_date.toLocaleString() + " : " 
		    			   +  df1.format(trade_price) +"," + df.format(last_price) + "," + even
		    			   + ",PER : "+ df.format(per) + "%");*/
		    	   
		    	   
		    	   if(trade_date.compareTo(checkTime) > 0 || per > 0.1|| per < -0.1) {
		    		  /* if(even == 0) {
		    			  System.out.println("보합 EVEN");
		    		   }else */if(even < 0) {
		    			   System.out.println("하락 FALL");
		    			   if(per < -0.1)  System.out.println("매수 전송 가격 : " + trade_price );
		    		   }else {
		    			   System.out.println("상승 RISE");
		    		   }
		    		   if(trade_date.compareTo(checkTime) < 0) {
		    			   System.out.println("########### changed ###########");
			    		   System.out.println(even + " , " + per);
			    		   System.out.println(last_date.toLocaleString() + " : " + last_price);
			    		   System.out.println(trade_date.toLocaleString() + " : " + trade_price);
			    		   //System.out.println(act);
			    		   System.out.println(df1.format(act.getProfitPer()) + "%" +"," + act.getProfit());
			    		   System.out.println("##############################");
		    		   } 
		    	
		    		   last_date = trade_date;
		    		   last_price = trade_price;
		    		   
		    	   }
		    	   
		    	   notifyCurrentPrice(trade_date, trade_price);
    		   }
		  }

		  /**
		   * Invoked when the remote peer has indicated that no more incoming messages will be
		   * transmitted.
		
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
			  t.printStackTrace();
		  }
		  
	}
	public String authenticationToken() {
		System.out.println("Upbit Auth Api Test");

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String jwtToken = JWT.create()
                             .withClaim("access_key", accessKey)
                             .withClaim("nonce", UUID.randomUUID().toString())
                             .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;
        System.out.println(authenticationToken);
        return authenticationToken;
	}
	public void getAccount() {
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
            
            Gson  gson = new Gson();
            String json = gson.toJson(jsonString);
            //System.out.println(json);
            JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(jsonString);
    		String prettyJsonString = gson.toJson(je);
    		//System.out.println(prettyJsonString);
    		JsonArray  objArray = je.isJsonArray() ? je.getAsJsonArray() : null;
    		//JsonObject  obj = objArray != null ? objArray.get(0).getAsJsonObject() :  je.getAsJsonObject();
    		//System.out.println(obj.get("currency"));
    		//ObjectMapper objectMapper = new ObjectMapper();
    		//Account[] acts = objectMapper.readValue(jsonString, Account[].class);
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

	
public void buyOrder(String market,String volume, String price, String ord_type) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	String side = "bid";
	postOrder(market, side, volume, price, ord_type);
}
public void sellOrder(String market,String volume,String price, String ord_type) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	String side = "ask";
	postOrder(market, side, volume, price, ord_type);
}

public void postOrder(String market,String side,String volume,String price, String ord_type) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //{"uuid":"23fd2858-11a3-4568-a81b-affea605984b","side":"bid","ord_type":"limit","price":"37823000.0","state":"wait","market":"KRW-BTC","created_at":"2022-05-22T00:52:55+09:00","volume":"0.0002","remaining_volume":"0.0002","reserved_fee":"3.7823","remaining_fee":"3.7823","paid_fee":"0.0","locked":"7568.3823","executed_volume":"0.0","trades_count":0}

        HashMap<String, String> params = new HashMap<>();
        params.put("market", "KRW-BTC");
        params.put("side", "bid");
        params.put("volume", "0.0002");
        params.put("price", "37823000");
        params.put("ord_type", "limit");

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
            HttpPost request = new HttpPost(serverUrl + "/v1/orders");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);
            
            request.setEntity(new StringEntity(new Gson().toJson(params)));
        
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        System.out.println(EntityUtils.toString(entity, "UTF-8"));
    } catch (IOException e) {
        e.printStackTrace();
    }
}



public void getOrdersDoneCancel(String market) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	String[] states = new String[] {"done","cancel"};
	getOrders(market, null, states);
}
public void getOrdersWaitWatch(String market) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	String[] states = new String[] {"wait","watch"};
	getOrders(market, null, states);
}
public void getOrders(String market,String state) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	getOrders(market, state, null);
}
public void getOrders(String market,String state,String[] states) throws NoSuchAlgorithmException, UnsupportedEncodingException {

    HashMap<String, String> params = new HashMap<>();
    if(state != null) params.put("state", "wait");
    

  /*  String[] uuids = {
        "9ca023a5-851b-4fec-9f0a-48cd83c2eaae"
        // ...
    };*/

    ArrayList<String> queryElements = new ArrayList<>();
    for(Map.Entry<String, String> entity : params.entrySet()) {
        queryElements.add(entity.getKey() + "=" + entity.getValue());
    }
  /*  for(String uuid : uuids) {
        queryElements.add("uuids[]=" + uuid);
    }
*/
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
			orders.addOrder(jobj);
		}
		Order[] od = orders.getOrders();
		for(Order o : od) {
			if(market.equals(o.getMarket())) {
				System.out.println(o);
			}
			
		}
		
    } catch (IOException e) {
        e.printStackTrace();
    }
}


public void getOrder(String uuid) throws NoSuchAlgorithmException, UnsupportedEncodingException {

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

    try {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(serverUrl + "/v1/order?" + queryString);
        request.setHeader("Content-Type", "application/json");
        request.addHeader("Authorization", authenticationToken);

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        System.out.println(EntityUtils.toString(entity, "UTF-8"));
    } catch (IOException e) {
        e.printStackTrace();
    }
}
	
}
