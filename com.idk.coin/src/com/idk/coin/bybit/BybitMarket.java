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
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.AlarmSound;

public class BybitMarket implements Runnable {
	final static String API_KEY 		= "F05n0T6G79ivD4UZKW";
    final static String API_SECRET 		= "QhYN61Cn9tKSIrfHxSMo3me9C6cfZrFLHEmv";
    final static String TIMESTAMP 		= Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    final static String RECV_WINDOW 	= "10000";
    
    public static Logger LOG =   LoggerFactory.getLogger(BybitMarket.class.getName());
    
    public AlarmPriceManager alarmPriceManager;
    Thread thread;
    public int alarm_total_size;
    
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
    	BybitMarket market = new BybitMarket(null);
    }
    
    
	public BybitMarket(AlarmPriceManager alarmPriceManager) {
		this.alarmPriceManager = alarmPriceManager;
		init();
		Thread thread  = new Thread(this);
		thread.start();
	}
	public void init() {
		
	}
	public void run()   {
		if(alarmPriceManager != null) {
			alarm_total_size = alarmPriceManager.getSize();
		}
		
		for(;;) {
			try {
    			getActiveKline();
			}catch(Exception e) {
				
			}
    		try {
				Thread.sleep(timing);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
    	  
    	String TIMESTAMP_SEC = Long.toString((ZonedDateTime.now().toInstant().toEpochMilli() / 1000) - ( 60*1 ));
        map.put("api_key", API_KEY);
        map.put("timestamp", TIMESTAMP);
        map.put("symbol", "BTCUSDT");
        map.put("interval", "1");
        map.put("from", TIMESTAMP_SEC);
        map.put("limit", "1");
        String signature = BybitClient.genSign(map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com/public/linear/kline";
        String response = BybitClient.get(url, map);
        if(response != null) {
        	lineParsing(response);;
        }
        
    }
    
	
	public double last_close 			= 0;
    public double last_volume 			= 0;
    public boolean pre_beep 			= false;
    public double volume_per_sec		= 10/2;
    public double volume_max			= 500;
    public double last_price			= 0;
    public double time_sec				= 60/2; 	 	// sec
    public long timing					= 1000/2;   	//ms
    public boolean last_dir				= false; // true ?????? , false ??????
    
    public void lineParsing(String str) {
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
       	 
       	 if(last_volume == 0 || time_now.getSeconds() < 1 || time_now.getSeconds() >= 59 || last_volume > volume) last_volume  = volume;
       	 double per_volume = volume - last_volume;
       	 last_volume = volume;
       	 last_dir 	= (last_price == price ? last_dir : last_price < price ? true : false) ;
       	// if(per_volume > volume_per_sec || volume > volume_max || time_now.getSeconds() < 1) {
	       	 //LOG.info(i + " : " + time_now.toGMTString() 
	       	LOG.info(  ",[" + alarm_total_size + "]:" + alarmPriceManager.getSize()
	       			 + ",\t# " + open    + "# " +  (open < price ? "??????" : "??????") 
	       			 + ",\t# " + price  +  "# " +  (last_dir ? "??????" : "??????")
	       			 + ",\t# " + String.format("%.2f",volume) 
	       			 + ",\t# " + String.format("%.2f",per_volume) 
	       			 +  " # " + (last_dir ? "??????" : "??????")
	       			 + "," + (per_volume > volume_per_sec || volume > volume_max ? (per_volume > volume_per_sec * 5 || volume > volume_max ? "???????????????" : "???????????????") : "")
	       			 +  (per_volume > volume_per_sec * 10  ?  (last_dir ? "???????????????" : "???????????????") : ""));
	       			 //+ ", \t# " + start_at.toGMTString() + " , " + open_time.toGMTString());
	        // LOG.info(i + " : " + start_at.toGMTString() + " , " + open_time.toGMTString());
       	 //}
       	 
       	last_price = price;
       	 if(volume > volume_max) {
       		 AlarmSound.alarm01();
       	 }else {
       		 if(per_volume > volume_per_sec) {
           		 if(pre_beep) AlarmSound.beep04();
           		 AlarmSound.beep01();
           		 pre_beep = true;
           	 }else pre_beep = false;
       	 }
       	 
       	 if(alarmPriceManager != null) alarmPriceManager.checkAlarm(last_price);
        }
    }
    public static Date getTimeNow(String time) {
    	double ret = Double.valueOf(time).doubleValue() * 1000 + (1000 * 60 * 60 * 9)-1000;
    	return new Date(Double.valueOf(ret).longValue());
    }
}
