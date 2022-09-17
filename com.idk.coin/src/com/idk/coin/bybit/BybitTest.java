package com.idk.coin.bybit;

import java.time.ZonedDateTime;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BybitTest {
	String API_KEY		= "LLQl52O8OFhotOL1P4";
	String SECRET_KEY   = "11AKm6SzKjOnTuCFNbZJ34LYzo7WzsGnhOG8";
	
	public static void main(String... args) {
		new BybitTest();
	}
	public BybitTest() {
		getTime();
		testnet();
		getPosition();
	}
	public void init() {
		
	}
	public long getTimeMillis() {
		long ms = ((System.currentTimeMillis())/1000)-1000;
		return ms;
	}
	public boolean testnet() {
		 
	    try {
	    	String domain_url = "https://api-testnet.bybit.com";
	    	String url_kline 	= "/public/linear/kline";
	        String url_recent_trading_records = "/public/linear/recent-trading-records?symbol=BTCUSDT&limit=500";
	    	
	    	String sub_url = url_kline;
	    	String params = "?symbol=BTCUSDT&interval=1&limit=2&from="+getTimeMillis();
	    	
	    	String url = domain_url + sub_url + params;
	    	
	        // OkHttp 클라이언트 객체 생성
	        OkHttpClient client = new OkHttpClient();
	 
	        // GET 요청 객체 생성
	        System.out.println("QUERY: " + url);
	        Request.Builder builder = new Request.Builder().url(url).get();
	        //builder.addHeader("password", "BlahBlah");
	        Request request = builder.build();
	 
	        // OkHttp 클라이언트로 GET 요청 객체 전송
	        Response response = client.newCall(request).execute();
	        if (response.isSuccessful()) {
	            // 응답 받아서 처리
	            ResponseBody body = response.body();
	            if (body != null) {
	                //System.out.println("Response:" + body.string());
	            	String str = body.string();
	                
	            	JsonParser parser = new JsonParser();
	                JsonElement el =  parser.parse(str);
	                System.out.println(el);
	                
	                Gson gson = new GsonBuilder().setPrettyPrinting().create();
	                System.out.println(gson.toJson(el));
	            }
	        }
	        else
	            System.err.println("Error Occurred");
	 
	        return true;
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	    return false;
	}
	
	public boolean getTime() {
		   try {
		    	String domain_url = "https://api-testnet.bybit.com";
		    	String sub_url 	  = "/v2/public/time";
		    	String params     = "";
		    	
		    	String url = domain_url + sub_url + params;
		    	
		        // OkHttp 클라이언트 객체 생성
		        OkHttpClient client = new OkHttpClient();
		 
		        // GET 요청 객체 생성
		        System.out.println("QUERY: " + url);
		        Request.Builder builder = new Request.Builder().url(url).get();
		        //builder.addHeader("password", "BlahBlah");
		        Request request = builder.build();
		 
		        // OkHttp 클라이언트로 GET 요청 객체 전송
		        Response response = client.newCall(request).execute();
		        if (response.isSuccessful()) {
		            // 응답 받아서 처리
		            ResponseBody body = response.body();
		            if (body != null) {
		                //System.out.println("Response:" + body.string());
		            	String str = body.string();
		                
		            	JsonParser parser = new JsonParser();
		                JsonElement el =  parser.parse(str);
		                System.out.println(el);
		                
		                Gson gson = new GsonBuilder().setPrettyPrinting().create();
		                System.out.println(gson.toJson(el));
		            }
		        }
		        else
		            System.err.println("Error Occurred");
		 
		        return true;
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		    
		    return false;
	}
	public boolean getPosition() {
			 try {
			    	String domain_url = "https://api-testnet.bybit.com";
			    	String sub_url 	  = "/private/linear/position/list";
			    	
			    	
			        // OkHttp 클라이언트 객체 생성
			    	TreeMap<String, String> map  = new TreeMap<String, String>();
			    	map.put("api_key",API_KEY);
			    	map.put("symbol", "BTCUSDT");
			    	map.put("timestamp", ZonedDateTime.now().toInstant().toEpochMilli()+"");
			    	
			    	String params     = Encryption.genQueryString(map, SECRET_KEY);
			    	
					String url = domain_url + sub_url +"?"+ params;
			    	
			    	
			        OkHttpClient client = new OkHttpClient();
			 
			        // GET 요청 객체 생성
			        System.out.println("QUERY: " + url);
			        Request.Builder builder = new Request.Builder().url(url).get();
			        //builder.addHeader("password", "BlahBlah");
			        Request request = builder.build();
			 
			        // OkHttp 클라이언트로 GET 요청 객체 전송
			        Response response = client.newCall(request).execute();
			        if (response.isSuccessful()) {
			            // 응답 받아서 처리
			            ResponseBody body = response.body();
			            if (body != null) {
			                //System.out.println("Response:" + body.string());
			            	String str = body.string();
			                
			            	JsonParser parser = new JsonParser();
			                JsonElement el =  parser.parse(str);
			                System.out.println(el);
			                
			                Gson gson = new GsonBuilder().setPrettyPrinting().create();
			                System.out.println(gson.toJson(el));
			            }
			        }
			        else
			            System.err.println("Error Occurred");
			 
			        return true;
			    } catch(Exception e) {
			        e.printStackTrace();
			    }
			    
			    return false;
	}
	 
}
