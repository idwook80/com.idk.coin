package com.idk.coin.bybit;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class BybitUtil {
	  public static Logger LOG =   LoggerFactory.getLogger(BybitUtil.class.getName());

			  
	  public static Date getTimeNow(String time) {
	    	double ret = Double.valueOf(time).doubleValue() * 1000 + (1000 * 60 * 60 * 9)-1000;
	    	return new Date(Double.valueOf(ret).longValue());
	    }
	  public static void printJson(String str) {
         	JsonParser parser = new JsonParser();
            JsonElement el =  parser.parse(str);
             
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            LOG.info(gson.toJson(el));
             
	  }
}
