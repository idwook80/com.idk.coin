package com.idk.coin;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

public class CoinConfig {
   public static String BYBIT_KEY 		= "BYBIT_API_KEY";
   public static String BYBIT_SECRET    = "BYBIT_API_SECRET";
   public static String BINANCE_KEY		= "BINANCE_API_KEY";
   public static String BINANCE_SECRET	= "BINANCE_API_SECRET";
   public static boolean is_loaded = false;
   
   
   public static void loadConfig() {
	   if(is_loaded) return;
	   
    	try {
	    	File file = new File(System.getProperty("user.dir")+"/file/config.properties");
	    	FileInputStream fis = new FileInputStream(file);
	    	
	    	Properties props = new Properties();
	    	props.load(fis);
	    	
	    	Enumeration<Object> enu = props.keys();
	    	while(enu.hasMoreElements()) {
	    		String key = enu.nextElement().toString();
	    		String value = (String)props.get(key);
	    		System.setProperty(key, value);
	    		
	    	}
	    	fis.close();
	    	file = null;
	    	is_loaded= true;
	    	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally {
    		
    	}
    	
    }
}
