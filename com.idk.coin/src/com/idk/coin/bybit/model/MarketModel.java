package com.idk.coin.bybit.model;

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
import com.idk.coin.bybit.BybitClient;

public class MarketModel implements Runnable {
	String symbol 				= "BTCUSDT";
	String api_key 				= "";
	String api_secret 			= "";
	ArrayList<PriceListener> listeners;
    Thread thread;
    public boolean is_run;
    public boolean debug				= false;
    public int debug_count				= 0;
    public int max_debug_count			= 10;
	
    final static String TIMESTAMP 		= Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    final static String RECV_WINDOW 	= "10000";
    
    public static Logger LOG =   LoggerFactory.getLogger(MarketModel.class.getName());
	public MarketModel(String symbol, String api_key, String api_secret, boolean debug) {
		listeners = new ArrayList();
		this.symbol = symbol;
		this.api_key  = api_key;
		this.api_secret = api_secret;
		this.debug = debug;
		init();
	}
	public MarketModel(String symbol, String api_key, String api_secret) {
		this(symbol, api_key, api_secret, false);
    }
	public void init() {
		if(symbol.equals("BTCUSDT")) {
			volume_max			= 500;
			volume_per_sec		= 10/2;
		}else if(symbol.equals("XRPUSDT")) {
			volume_max			= 800*1000;
			volume_per_sec		= 50*1000;
		}
	}
	public void startMarket() {
		is_run = true;
		thread  = new Thread(this);
		thread.start();
	}
	public void stopMarket() {
		is_run = false;
		if(thread != null) {
			try {
				thread.interrupt();
			}catch(Exception e) {
				e.printStackTrace();
			}
			thread = null;
		}
	}
	public void run()   {
		while(is_run) {
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
	public void addPriceListener(PriceListener l) {
		synchronized (listeners) {
			listeners.add(l);
		}
	}
	public void removePriceListener(PriceListener l) {
		synchronized (listeners) {
			listeners.remove(l);
		}
	}
	public synchronized void notifyPrice(double price) {
		PriceListener[] ls = listeners.toArray(new PriceListener[0]);
		for(PriceListener l : ls) {
			l.checkAlarmPrice(price);
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
        map.put("api_key", api_key);
        map.put("timestamp", TIMESTAMP);
        map.put("symbol", symbol);
        map.put("interval", "1");
        map.put("from", TIMESTAMP_SEC);
        map.put("limit", "1");
        String signature = BybitClient.genSign(api_secret,map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com/public/linear/kline";
        String response = BybitClient.get(url, map);
        if(response != null) {
        	lineParsing(response);;
        }
        
    }
    
	
	public double 	last_close 			= 0;
    public double 	last_volume 		= 0;
    public boolean 	pre_beep 			= false;
    public double 	volume_per_sec		= 10/2;
    public double 	volume_max			= 500;
    public double 	last_price			= 0;
    public double 	time_sec				= 60/2; 	 	// sec
    public long 	timing					= 1000/2;   	//ms
    public boolean 	last_dir				= false; // true ↑↑ , false ↓↓
   // public int balancePollingCounter	= 100;
    
    public synchronized void lineParsing(String str) {
    	JsonParser parser 	= new JsonParser();
        JsonElement el 		=  parser.parse(str);
        //System.out.println(el);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //LOG.info(gson.toJson(el));
        Map<String, Object> map 	= gson.fromJson(str, Map.class);
        ArrayList result 			= (ArrayList)map.get("result");
        Date time_now 				= getTimeNow((String)map.get("time_now"));
        
        for(int i=0; i<result.size(); i++) {
	       	 LinkedTreeMap t1 	= (LinkedTreeMap)result.get(i);
	       	 double open  		= Double.valueOf(t1.get("open").toString());
	       	 double price 		= Double.valueOf(t1.get("close").toString());
	       	 double volume 		= Double.valueOf(t1.get("volume").toString());
	       	 Date start_at 		= getTimeNow(t1.get("start_at").toString());
	       	 Date open_time 	= getTimeNow(t1.get("open_time").toString());
	     
	       	 
	       	 if(last_volume == 0 || time_now.getSeconds() < 1 
	       		|| time_now.getSeconds() >= 59 || last_volume > volume) 
	       		 	last_volume  = volume;
	       	 
	       	 double per_volume 	= volume - last_volume;
	       	 last_volume 		= volume;
	       	 last_dir 			= (last_price == price ? last_dir : last_price < price ? true : false) ;
	       	 
	       	//debug_count--;
	       	last_price = price;
	       	 if(volume > volume_max) {
	       		 AlarmSound.alarm01();
	       		debug_count = -1;
	       	 }else {
	       		 if(per_volume > volume_per_sec) {
	           		 if(pre_beep) AlarmSound.beep04();
	           		 AlarmSound.beep01();
	           		debug_count = -1;
	           		 pre_beep = true;
	           	 }else pre_beep = false;
	       	 }
	       	 notifyPrice(last_price);

	       	 if(!debug || 0 > --debug_count) {
	       		 /**
		       	LOG.info( "["+symbol + "][" + listeners.toString() + "]" + "["+debug_count+"]"
		       			 //+ ",\t# " + open    + "# " +  (open < price ? "↑↑" : "↓↓") 
		       			 + ", # [" + price  +  "] # " +  (last_dir ? "↑↑" : "↓↓")
		       			 + ",  # " + String.format("%.2f",volume) 
		       			 + ",  # " + String.format("%.2f",per_volume) 
		       			 +  " # " + (last_dir ? "↑↑" : "↓↓")
		       			 + "," + (per_volume > volume_per_sec || volume > volume_max ? (per_volume > volume_per_sec * 5 || volume > volume_max ? "◀◀" : "◁◁") : "")
		       			 +  (per_volume > volume_per_sec * 10  ?  (last_dir ? "▲▲" : "▼▼") : "") );**/
		        
		    	LOG.info( "["+symbol + "][" + listeners.toString() + "]" + "["+debug_count+"]"
		       			 //+ ",\t# " + open    + "# " +  (open < price ? "↑↑" : "↓↓") 
		       			 + ", 현재가 [" + price  +  "] # " +  (last_dir ? "↑↑" : "↓↓")
		       			 + ",  거래량 " + String.format("%.2f",volume) 
		       			 + ",  순거래량 " + String.format("%.2f",per_volume) 
		       			 +  " # " + (last_dir ? "↑↑" : "↓↓")
		       			 + "," + (per_volume > volume_per_sec || volume > volume_max ? (per_volume > volume_per_sec * 5 || volume > volume_max ? "◀◀" : "◁◁") : "")
		       			 +  (per_volume > volume_per_sec * 10  ?  (last_dir ? "▲▲" : "▼▼") : "") );
		    	//LOG.info("챠트 사이즈 : "+result.size());
		    	//LOG.info(gson.toJson(el));
		       //if(-3 > debug_count)	debug_count = max_debug_count;
		    	debug_count = max_debug_count;
	       	 }
        }
    }
    public double getLastPrice() {
    	return last_price;
    }
    public static Date getTimeNow(String time) {
    	double ret = Double.valueOf(time).doubleValue() * 1000 + (1000 * 60 * 60 * 9)-1000;
    	return new Date(Double.valueOf(ret).longValue());
    }
    public String getSymbol() {
    	return symbol;
    }
    public void setDebug(boolean b) {
    	this.debug = b;
    }
}
