package com.idk.coin.upbit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.idk.coin.db.upbit.ConfigDao;
import com.idk.coin.db.upbit.OrderDao;
import com.idk.coin.indicator.RSI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CandleChecker {
	public static final String accessKey 	 = "IXoeDiXBMiri90J7X7alXJKQqzLeW3U9SCVyCsx3";
	public static final String secretKey 	 = "Ako8ZExhxvIKRvdcSLKkXOX0lNbZr0qFtkDSIgYY";
	public static final String serverUrl 	 = "https://api.upbit.com";
	
	public static final String currency  	 = "BTC";
	public static final String unit_currency = "KRW";
	public static final String market	 	 = unit_currency+"-"+currency;
	
	public boolean is_rise = true;
	public boolean rise_60 = true;
	public boolean rise_15 = true;
	public boolean rise_5  = true;
	
	boolean DEBUG 			= true;
	DecimalFormat df 		= new DecimalFormat("###,###");
	
	public static void main(String[] args) {
		CandleChecker checker = new CandleChecker();
		//checker.is_rise();
		
	}
	public CandleChecker() {
		//test();
		
	}
	public void test() {
		try {
			//Order order = getOrder("23fd2858-11a3-4568-a81b-affea605984b");
			//order.setCancel_at(ZonedDateTime.now().toString());
			//order.setGoal_price(new BigDecimal(order.getPrice().doubleValue()+ 50000));
			//order.setPuuid(order.getUuid());
			//OrderDao.getInstace().insert(order);
			//OrderDao.getInstace().update(order);
			/**Orders orders = OrderDao.getInstace().select(Order.STATE_WAIT);
			Order[] arr = orders.getOrders();
			for(int i=0; i<arr.length; i++) {
				System.out.println(arr[i]);
			}
			BotConfig config = ConfigDao.getInstace().select("KRW-BTC");
			System.out.println(new BigDecimal(config.getString("volume")));
			System.out.println(config.getString("buy"));
			**/
			candle30();
			RsiCandles();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean is_rise() {
		candle60();
		candle15();
		candle5();
		//candle1();
		is_rise = (rise_15 && rise_5 && rise_60);
		System.out.println(ZonedDateTime.now().toLocalDateTime() + " = rise: "+ is_rise + " ,  5 : " + rise_5 + " , 15 : " + rise_15 + " , 60 : " + rise_60);
		return is_rise;
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
	
	
	
	public void candle60() {
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/60?market=" + market + "&count=2")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		if(DEBUG) System.out.println("################################ [60분] #####################################");
    		rise_60  = true;
    		double[] high_changes = new double[candles.length-1];
    		double[] low_changes = new double[candles.length-1];
    		
    		
    		for(int i=0; i<candles.length-1; i++) {
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			String next_time 	= c.getCandle_date_time_kst();
    			String pre_time 	= p.getCandle_date_time_kst();
    			double next_high 	= c.getHigh_price().doubleValue();
    			double next_low  	= c.getLow_price().doubleValue();
    			double pre_high 	= p.getHigh_price().doubleValue();
    			double pre_low  	= p.getLow_price().doubleValue();
    			double high_change 	= next_high - pre_high;
    			double low_change  	= next_low - pre_low;
    			
    			if(DEBUG) {
    				System.out.println(pre_time + " ~ " + next_time);
    				System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    				System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			}
    			
    			/*if(low_change < 0) rise_60 = false;*/
    			
    			high_changes[i] = high_change;
    			low_changes[i] = low_change;
    		
    		}
    		if(DEBUG) System.out.println("###################################### "+ rise_60 +" #############################\n");
    		//60분챠트 분석
    		//현재 저점 상승 
    		//현재  고점 상승
    		if(low_changes[0] >= 0 || high_changes[0] >= 0) rise_60 = true;
    		else rise_60 = false;
    		
    		if(DEBUG) {
	    		System.out.println(Arrays.toString(high_changes));
	    		System.out.println(Arrays.toString(low_changes));
	    		System.out.println(rise_60);
    		}
    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void candle15() {
		
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/15?market=" + market + "&count=3")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		if(DEBUG) System.out.println("##########################[15분]#########################################");
    		rise_15 = true;
    		double[] high_changes = new double[candles.length-1];
    		double[] low_changes = new double[candles.length-1];
    		
    		
    		for(int i=0; i<candles.length-1; i++) {
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			String next_time = c.getCandle_date_time_kst();
    			String pre_time  = p.getCandle_date_time_kst();
    			double next_high = c.getHigh_price().doubleValue();
    			double next_low  = c.getLow_price().doubleValue();
    			double pre_high = p.getHigh_price().doubleValue();
    			double pre_low  = p.getLow_price().doubleValue();
    			double high_change = next_high - pre_high;
    			double low_change  = next_low - pre_low;
    			
    			if(DEBUG) {
    			System.out.println(pre_time + " ~ " + next_time);
    			System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    			System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			}
    			
    			/*if(i == 0 && low_change < 0 ) rise_15 = false;
    			if(i == 1 && high_change < 0) rise_15 = false;*/
    			
    			high_changes[i] = high_change;
    			low_changes[i] = low_change;
    		}
    		if(DEBUG) System.out.println("########################################## "+ rise_15+" #########################\n");
    		//15분챠트 분석
    		//현재 포함 2개 챠트 저점 상승
    		if(low_changes[0] >=0 && low_changes[1] >= 0) rise_15 = true;
    		else if(low_changes[0] >=0 && high_changes[1] >=0 ) rise_15 = true;
    		else rise_15 = false;
    		
    		if(DEBUG) {
	    		System.out.println(Arrays.toString(high_changes));
	    		System.out.println(Arrays.toString(low_changes));
	    		System.out.println(rise_15);
    		}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void candle5() {
		
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/5?market=" + market + "&count=4")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		if(DEBUG) System.out.println("###########################[5분]####################################");
    		rise_5 = true;
    		int rise_count = 0;
    		double[] high_changes = new double[candles.length-1];
    		double[] low_changes = new double[candles.length-1];
    		
    		
    		for(int i=0; i<candles.length-1; i++) {
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			String next_time = c.getCandle_date_time_kst();
    			String pre_time  = p.getCandle_date_time_kst();
    			double next_high = c.getHigh_price().doubleValue();
    			double next_low  = c.getLow_price().doubleValue();
    			double pre_high = p.getHigh_price().doubleValue();
    			double pre_low  = p.getLow_price().doubleValue();
    			double high_change = next_high - pre_high;
    			double low_change  = next_low - pre_low;
    			
    			if(DEBUG) {
    			System.out.println(pre_time + " ~ " + next_time);
    			System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    			System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			//if(high_change < 0) rise_5 = false;
    			}
    			
    			/*if(low_change < 0) rise_5 = false;
    			if(low_change >= 0) rise_count++;
    			if(high_change >=0) rise_count++;*/
    			
    			high_changes[i] = high_change;
    			low_changes[i] = low_change;
    		}
    		if(rise_count > 2) rise_5 = true;
    		if(DEBUG) System.out.println("########################################## "+ rise_5 +" #########################\n");
    		// 현재 포함 2개 챠트 저점 상승 + 현재 이전 고점 상승
    		// 현재 포함 3개 챠트가 고점,저점중 4개이상이 상승 + 기본 현재 저점 상승
    		
    		int change_count = 0;
    		for(int i=0; i<low_changes.length; i++) {
    			if(low_changes[i]>=0) change_count++;
    			if(high_changes[i]>=0) change_count++;
    		}
    		
    		if(low_changes[0] >=0 && low_changes[0]>= 0 && high_changes[1]>=0) rise_5 = true;
    		else if(low_changes[0]>=0 && change_count >=4) rise_5 = true;
    		else rise_5 = false;
    		
    		if(DEBUG) {
	    		System.out.println(Arrays.toString(high_changes));
	    		System.out.println(Arrays.toString(low_changes));
	    		System.out.println(rise_5);
    		}
    		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void candle30() {
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/30?market=" + market + "&count=5")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		if(DEBUG) System.out.println("###########################[30분]####################################");
    		for(int i=0; i<candles.length-1; i++) {
    			System.out.println(candles[i]);
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			String next_time 	= c.getCandle_date_time_kst();
    			String pre_time  	= p.getCandle_date_time_kst();
    			double next_high 	= c.getHigh_price().doubleValue();
    			double next_low  	= c.getLow_price().doubleValue();
    			double pre_high 	= p.getHigh_price().doubleValue();
    			double pre_low  	= p.getLow_price().doubleValue();
    			double high_change 	= next_high - pre_high;
    			double low_change  	= next_low - pre_low;
    			if(DEBUG) {
    				ZonedDateTime zd =  ZonedDateTime.parse(next_time+"+09:00");
    				
    				ZonedDateTime zd2 = zd.plusMinutes(30);
    				
    				System.out.println(zd);
    				System.out.println(zd2);
    				System.out.println(zd.isAfter(zd));
    				
    				System.out.println("현재 고점 : "+ df.format(next_high));
    				System.out.println("현재 저점 : "+ df.format(next_low));
    				
    				System.out.println(pre_time + " ~ " + next_time);
    				System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    				System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			}
    		}
    		if(DEBUG) System.out.println("#######################################################################\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void candle1() {

		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/1?market=" + market + "&count=5")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		if(DEBUG) System.out.println("###########################[1분]####################################");
    		for(int i=0; i<candles.length-1; i++) {
    			System.out.println(candles[i]);
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			String next_time 	= c.getCandle_date_time_kst();
    			String pre_time  	= p.getCandle_date_time_kst();
    			double next_high 	= c.getHigh_price().doubleValue();
    			double next_low  	= c.getLow_price().doubleValue();
    			double pre_high 	= p.getHigh_price().doubleValue();
    			double pre_low  	= p.getLow_price().doubleValue();
    			double high_change 	= next_high - pre_high;
    			double low_change  	= next_low - pre_low;
    			if(DEBUG) {
    				System.out.println(pre_time + " ~ " + next_time);
    				System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    				System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			}
    		}
    		if(DEBUG) System.out.println("#######################################################################\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void RsiCandles() {
		
		
		try {
			for(;;) {
					OkHttpClient client = new OkHttpClient();
					int time =5;
					int count = 14;
		
					Request request = new Request.Builder()
					  .url("https://api.upbit.com/v1/candles/minutes/"+time+"?market=" + market + "&count="+count)
					  .get()
					  .addHeader("Accept", "application/json")
					  .build();
					
					Response response = client.newCall(request).execute();
					
					String message = response.body().string();
					
					JsonParser jp = new JsonParser();
		    		JsonElement je = jp.parse(message);
		    		JsonArray  objArray =  je.getAsJsonArray();
		    		
		    		Candle[] candles = new Candle[objArray.size()];
		    		ArrayList<Candle> data = new ArrayList();
		    		for(int i=0; i<objArray.size(); i++) {
		    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
		    			Candle candle = new Candle(jobj);
		    			data.add(candle);
		    		}
		    		//
		    		calculate(data);
		    		/*calculate2(data);
		    		RSI rsi = new RSI();
		    		System.out.println(rsi.calculate(data));*/
		    		Collections.reverse(data);
		    		DecimalFormat dff = new DecimalFormat("###");
		    		/*for(int i=0; i<data.size(); i++) {
		    			Candle c = data.get(i);
		    			System.out.println(dff.format(c.getTrade_price()));
		    		}*/
		    		
		    		Thread.sleep(1000*10);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void calculate(List<Candle> data) throws Exception {
		int periodLength = 14;
		int lastBar = data.size() - 1;
		int firstBar = lastBar - periodLength + 1;
		if (firstBar < 0) {
			String msg = "Quote history length " + data.size() + " is insufficient to calculate the indicator.";
			throw new Exception(msg);
		}
		double aveGain = 0, aveLoss = 0;
		for (int bar = firstBar + 1; bar <= lastBar; bar++) {
			double change =  data.get(bar-1).getTrade_price().doubleValue() -
					data.get(bar).getTrade_price().doubleValue() ;
			String start_time = data.get(bar).getCandle_date_time_kst();
			String end_time   = data.get(bar-1).getCandle_date_time_kst();
			
			//System.out.println(bar+ " : "+ start_time +" ~ "+ end_time+ " : "+change +   "  "+ df.format(data.get(bar-1).getTrade_price()));
			if (change >= 0) {
				aveGain += change;
				
			} else {
				aveLoss += change;
			}
		}
		aveLoss = aveLoss / (periodLength);
		aveGain = aveGain / (periodLength);
		//System.out.println(aveGain + " , "+aveLoss);
		
		double rs = aveGain / Math.abs(aveLoss);
		double rsi = 100 - 100 / (1 + rs);
		double first_change = data.get(0).getTrade_price().doubleValue() - data.get(1).getTrade_price().doubleValue();
		System.out.println(data.get(0).getCandle_date_time_kst() + " == "+ df.format(data.get(0).getTrade_price()) + "["+ df.format(first_change)+"] rs: " + rs + ", RSI: " + rsi);
		
		/**
		for(int bar=firstBar; bar > 0; bar--) {
			double change =  data.get(bar - 1).getTrade_price().doubleValue() -
					data.get(bar).getTrade_price().doubleValue() ;
			
			if (change >= 0) {
				aveGain = ((aveGain * (periodLength-1)) + change ) / periodLength;
				
			} else {
				aveLoss =   (aveLoss* (periodLength-1) +change ) / periodLength;
			}
			
			 rs = aveGain / Math.abs(aveLoss);
			 rsi = 100 - 100 / (1 + rs);
			System.out.println(bar+ " : "+data.get(bar-1).getCandle_date_time_kst() + " : "+change +   "  "+ data.get(bar-1).getTrade_price()
					+ "rs: " + rs + ", RSI: " + rsi);
			
		}
		

		  rs = aveGain / Math.abs(aveLoss);
		  rsi = 100 - 100 / (1 + rs);
		  System.out.println(aveGain + " , "+aveLoss);
		  System.out.println("rs: " + rs + ", RSI: " + rsi);  **/
	}
	public void calculate2(List<Candle> data) throws Exception {
		int periodLength = 14;
		int lastBar = data.size() - 1;
		int firstBar = lastBar - periodLength + 1;
		if (firstBar < 0) {
			String msg = "Quote history length " + data.size() + " is insufficient to calculate the indicator.";
			throw new Exception(msg);
		}
		
		System.out.println(data.get(firstBar).getCandle_date_time_kst());
		System.out.println(data.get(firstBar+1).getCandle_date_time_kst());
		double aveGain = 0, aveLoss = 0;
		double au = 0, ad = 0;
		for (int bar = firstBar + 1; bar <= lastBar; bar++) {
			double change = data.get(bar).getTrade_price().doubleValue() - data.get(bar - 1).getTrade_price().doubleValue();
			aveGain += Math.max(0, change); 
			aveLoss += Math.max(0, -change); 
		}
		aveGain = aveGain/periodLength;
		aveLoss = aveLoss/periodLength;
		
		for (int bar = firstBar + 2; bar <= lastBar; bar++) {
			double change = data.get(bar).getTrade_price().doubleValue() - data.get(bar - 1).getTrade_price().doubleValue();
			au += Math.max(0, change) + (aveGain*13);
			ad += Math.max(0, -change)  + (aveLoss*13); 
		}
		au = au/14;
		ad = ad/14;
	 
			
			double rs = au/ad;
			double rsi = rs / (1 + rs);

		System.out.println("AVG2: " + aveGain + ", RSI: " + rsi);
	}
}
