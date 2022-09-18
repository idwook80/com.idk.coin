package com.idk.coin.bybit;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Properties;

public class ByBitConfig {
	public static String API_KEY 		= "BYBIT_API_KEY";
	public static String API_SECRET 	= "BYBIT_API_SECRET";
	 
   public static void loadConfig() {
    	try {
	    	File file = new File(System.getProperty("user.dir")+"/file/config.properties");
	    	FileInputStream fis = new FileInputStream(file);
	    	
	    	Properties props = new Properties();
	    	props.load(fis);
	    	System.setProperties(props);
	    	fis.close();
	    	file = null;
	    	
	    	String key 	= (String)System.getProperty(API_KEY);
	    	String secret 	= (String)System.getProperty(API_SECRET);
    	System.out.println(key + "," + secret);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally {
    		
    	}
    	
    }
}
