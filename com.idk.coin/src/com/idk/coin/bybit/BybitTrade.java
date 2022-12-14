package com.idk.coin.bybit;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

public class BybitTrade {
	public static Logger LOG 			  =   LoggerFactory.getLogger(BybitTrade.class.getName());
	
	
	final static String API_KEY 		  = "F05n0T6G79ivD4UZKW";
    final static String API_SECRET 	 	  = "QhYN61Cn9tKSIrfHxSMo3me9C6cfZrFLHEmv";
    final static String RECV_WINDOW 	  = "10000";
    
    final static String SIDE_BUY		  = "Buy";			// LongOpen , ShortClose
    final static String SIDE_SELL		  = "Sell";			// LongClose, ShortOpen	
    
    final static String POSITION_IDX_BOTH = "0";
    final static String POSITION_IDX_BUY  = "1";			// Long
    final static String POSITION_IDX_SELL = "2";			// Short
    
    String side;
    String position_idx;
    double price;
    double qty;
    
    public static void main(String[] args) {
    	//BybitTrade tr = new BybitTrade();
    	//tr.closeShort("19310", "0.1");
    	//tr.executAction();
    	/*tr.openLong("19010","0.001");
    	tr.executAction();
    	tr.openShort("19310","0.001");
    	tr.executAction();
    	tr.closeLong("19320","0.001");
    	tr.executAction();
    	tr.closeShort("19020","0.001");
    	tr.executAction();*/
    }
    public BybitTrade() {}
    
	/**
     * POST: place an active linear perpetual order
     */
    public  double placeActiveOrder(String side,String position_idx,double price,double qty) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<String, Object> map = new TreeMap(
            new Comparator<String>() {
                @Override
                // sort paramKey in A-Z
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
            }
        });
        map.put("api_key", API_KEY);
        map.put("timestamp", getTimestamp());
        map.put("side", side);
        map.put("position_idx",position_idx);
        map.put("symbol", "BTCUSDT");
        map.put("order_type", "Limit");
        map.put("qty", qty);
        map.put("price", price);
        map.put("time_in_force", "GoodTillCancel");
        //map.put("take_profit", "20000");
        //map.put("stop_loss", "18000");
        map.put("reduce_only", false);
        map.put("close_on_trigger", false);
        map.put("recv_window", RECV_WINDOW);
        String signature = BybitClient.genSign(map);
        map.put("sign", signature);
        
        String url = "https://api.bybit.com";
        url		  += "/private/linear/order/create";
        
        String response = BybitClient.post(url, map);
        if(response != null) {
        	System.out.println(response);
        	return parsing(response);
        }
        return -1;
    }
    
    public double parsing(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        //System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        
        double ret_code = (Double)map.get("ret_code");
        
        return ret_code;
        
    }
    
    
    public String getTimestamp() {
    	return Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    }
    
    public double executAction() {
    	try {
    		double ret_code = placeActiveOrder(side,position_idx, price, qty);
    		 return ret_code;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return -1;
    }
    public void openLong(double price, double qty) {
    	this.side 		  = SIDE_BUY;
    	this.position_idx = POSITION_IDX_BUY;
    	this.price 		  = price;
    	this.qty 		  = qty;
    }
    public void openShort(double price, double qty){
    	this.side 			= SIDE_SELL;
    	this.position_idx 	= POSITION_IDX_SELL;
    	this.price 			= price;
    	this.qty 			= qty;
    }
    public void closeLong(double price, double qty) {
    	this.side 			= SIDE_SELL;
    	this.position_idx 	= POSITION_IDX_BUY;
    	this.price 			= price;
    	this.qty 			= qty;
    }
    public void closeShort(double price, double qty) {
    	this.side 			= SIDE_BUY;
    	this.position_idx 	= POSITION_IDX_SELL;
    	this.price 			= price;
    	this.qty 			= qty;
    }
	@Override
	public String toString() {
		return "BybitTrade [side=" + side + ", position_idx=" + position_idx + ", price=" + price + ", qty=" + qty
				+ "]";
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getPosition_idx() {
		return position_idx;
	}
	public void setPosition_idx(String position_idx) {
		this.position_idx = position_idx;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
    
	
	
	
	
    
}
