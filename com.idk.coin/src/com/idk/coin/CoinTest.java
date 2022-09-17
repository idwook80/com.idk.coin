package com.idk.coin;
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

import javax.swing.RepaintManager;

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
import com.idk.coin.upbit.Candle;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class CoinTest  {
	public static final String accessKey 	 = "IXoeDiXBMiri90J7X7alXJKQqzLeW3U9SCVyCsx3";
	public static final String secretKey 	 = "Ako8ZExhxvIKRvdcSLKkXOX0lNbZr0qFtkDSIgYY";
	public static final String serverUrl 	 = "https://api.upbit.com";
	public static final String market	 	 = "KRW-BTC";
	public static final String currency  	 = "BTC";
	public static final String unit_currency = "KRW";
	
	public boolean is_rise = true;
	public boolean rise_60 = true;
	public boolean rise_15 = true;
	public boolean rise_5  = true;
	
	
	public static void main(String[] args) {
		new CoinTest();
	}
	public CoinTest() {
		start();
	}
	public void start() {
		candle60();
		candle15();
		candle5();
		//candle1();
		is_rise = (rise_15 && rise_5 && rise_60);
		System.out.println("is_rise : " + is_rise);
		
		
		System.out.println(ZonedDateTime.now().toString() + " = 5 : " + rise_5 + " , 15 : " + rise_15 + " , 60 : " + rise_60);
		
	}
	
	
	
	public void candle60() {
		//60분 2개 저점 상승시
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/60?market=KRW-BTC&count=2")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			System.out.println(message);
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		System.out.println("################################[60분]#####################################");
    		rise_60  = true;
    		for(int i=0; i<candles.length-1; i++) {
    			System.out.println(candles[i]);
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			DecimalFormat df = new DecimalFormat("###,###");
    			String next_time = c.getCandle_date_time_kst();
    			String pre_time  = p.getCandle_date_time_kst();
    			double next_high = c.getHigh_price().doubleValue();
    			double next_low  = c.getLow_price().doubleValue();
    			double pre_high = p.getHigh_price().doubleValue();
    			double pre_low  = p.getLow_price().doubleValue();
    			double high_change = next_high - pre_high;
    			double low_change  = next_low - pre_low;
    			
    			System.out.println(pre_time + " ~ " + next_time);
    			System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    			System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			
    			if(low_change < 0) rise_60 = false;
    		
    		}
    		System.out.println("###################################### "+ rise_60 +" #############################\n");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void candle15() {
		//15분 2개가 저점 상승 & 전고점 상승
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/15?market=KRW-BTC&count=3")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			System.out.println(message);
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		System.out.println("##########################[15분]#########################################");
    		rise_15 = true;
    		for(int i=0; i<candles.length-1; i++) {
    			System.out.println(candles[i]);
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			DecimalFormat df = new DecimalFormat("###,###");
    			String next_time = c.getCandle_date_time_kst();
    			String pre_time  = p.getCandle_date_time_kst();
    			double next_high = c.getHigh_price().doubleValue();
    			double next_low  = c.getLow_price().doubleValue();
    			double pre_high = p.getHigh_price().doubleValue();
    			double pre_low  = p.getLow_price().doubleValue();
    			double high_change = next_high - pre_high;
    			double low_change  = next_low - pre_low;
    			
    			System.out.println(pre_time + " ~ " + next_time);
    			System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    			System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			if(i == 0 && low_change < 0 ) rise_15 = false;
    			if(i == 1 && high_change < 0) rise_15 = false;
    		}
    		System.out.println("########################################## "+ rise_15+" #########################\n");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void candle5() {
		// 이전 2개가 저점  모두 상승시
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/5?market=KRW-BTC&count=3")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			System.out.println(message);
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		System.out.println("###########################[5분]####################################");
    		rise_5 = true;
    		int rise_count = 0;
    		for(int i=0; i<candles.length-1; i++) {
    			System.out.println(candles[i]);
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			DecimalFormat df = new DecimalFormat("###,###");
    			String next_time = c.getCandle_date_time_kst();
    			String pre_time  = p.getCandle_date_time_kst();
    			double next_high = c.getHigh_price().doubleValue();
    			double next_low  = c.getLow_price().doubleValue();
    			double pre_high = p.getHigh_price().doubleValue();
    			double pre_low  = p.getLow_price().doubleValue();
    			double high_change = next_high - pre_high;
    			double low_change  = next_low - pre_low;
    			
    			System.out.println(pre_time + " ~ " + next_time);
    			System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    			System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    			//if(high_change < 0) rise_5 = false;
    			
    			if(low_change < 0) rise_5 = false;
    			if(low_change >= 0) rise_count++;
    			if(high_change >=0) rise_count++;
    		}
    		if(rise_count > 2) rise_5 = true;
    		System.out.println("########################################## "+rise_5+" #########################\n");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void candle1() {

		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.upbit.com/v1/candles/minutes/1?market=KRW-BTC&count=5")
			  .get()
			  .addHeader("Accept", "application/json")
			  .build();
			
			Response response = client.newCall(request).execute();
			
			String message = response.body().string();
			System.out.println(message);
			
			JsonParser jp = new JsonParser();
    		JsonElement je = jp.parse(message);
    		JsonArray  objArray =  je.getAsJsonArray();
    		
    		Candle[] candles = new Candle[objArray.size()];
    		
    		for(int i=0; i<objArray.size(); i++) {
    			JsonObject jobj  = objArray.get(i).getAsJsonObject();
    			candles[i] = new Candle(jobj);
    		}
    		
    		System.out.println("###########################[1분]####################################");
    		for(int i=0; i<candles.length-1; i++) {
    			System.out.println(candles[i]);
    			Candle c = candles[i];
    			Candle p = candles[i+1];
    			
    			DecimalFormat df = new DecimalFormat("###,###");
    			String next_time = c.getCandle_date_time_kst();
    			String pre_time  = p.getCandle_date_time_kst();
    			double next_high = c.getHigh_price().doubleValue();
    			double next_low  = c.getLow_price().doubleValue();
    			double pre_high = p.getHigh_price().doubleValue();
    			double pre_low  = p.getLow_price().doubleValue();
    			double high_change = next_high - pre_high;
    			double low_change  = next_low - pre_low;
    			
    			System.out.println(pre_time + " ~ " + next_time);
    			System.out.println(df.format(pre_high) + " vs " + df.format(next_high) + " = " + df.format(high_change) + " >>>> " + (high_change >= 0 ? "고점상승" : "고점하락"));
    			System.out.println(df.format(pre_low) + " vs " + df.format(next_low) + " = " + df.format(low_change) + "  >>>>  " + (low_change >= 0 ? "저점상승" : "저점하락"));
    		}
    		System.out.println("#######################################################################\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
