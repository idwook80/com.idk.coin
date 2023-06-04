package com.idk.coin.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.bybit.BybitTrade;
import com.idk.coin.bybit.model.Order;
import com.idk.coin.bybit.model.OrderExecution;

public abstract class TradeModel {
	public static Logger LOG 			  =   LoggerFactory.getLogger(BybitTrade.class.getName());
	public String API_KEY 		  		  = "";
    public String API_SECRET 	 	  	  = "";
    public final static String RECV_WINDOW 	  = "10000";
    
    public final static String SIDE_BUY		  = "Buy";			// LongOpen , ShortClose
    public final static String SIDE_SELL		  = "Sell";			// LongClose, ShortOpen	
    
    public final static String POSITION_IDX_BOTH = "0";
    public final static String POSITION_IDX_LONG  = "1";			// Long
    public final static String POSITION_IDX_SHORT = "2";			// Short
    
    public final static String POSITION_BOTH	= "BOTH";
    public final static String POSITION_LONG	= "LONG";
    public final static String POSITION_SHORT	= "SHORT";
    
    public String side;
    public String position_idx;
    public String position;
    public double price;
    public double qty;
    
    public TradeModel() {
    }
    
    /**
     * POST: place an active linear perpetual order
     */
    abstract public Object placeActiveOrder(String api_key, String api_secret, String symbol, String side,String position_idx,double price,double qty) 
    						throws NoSuchAlgorithmException, InvalidKeyException;   
    abstract public Object executAction(String api_key, String api_secret, String symbol); 
    
    
    public double parsing(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        //System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        OrderExecution order = new OrderExecution(result);
        
        double ret_code = (Double)map.get("ret_code");
        
        return ret_code;
    }
    public Object parsingOrder(String str) {
    	JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(str);
        //System.out.println(el);
        
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LOG.info(gson.toJson(el));
        Map<String, Object> map  = gson.fromJson(str, Map.class);
        LinkedTreeMap result = (LinkedTreeMap)map.get("result");
        Order order = new Order(result);
        
        double ret_code = (Double)map.get("ret_code");
        
        if(ret_code == 0) return order;
        return null;
    }
    
    public String getTimestamp() {
    	return Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
    }
    
    /*public OrderExecution executAction() {
    	try {
    		 OrderExecution order = placeActiveOrder(side,position_idx, price, qty);
    		double ret_code = placeActiveOrder(side,position_idx, price, qty);
    		return ret_code;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return -1;
    	
    }*/
    public void openLong(double price, double qty) {
    	this.side 		  = SIDE_BUY;
    	this.position_idx = POSITION_IDX_LONG;
    	this.position	  = "LONG";
    	this.price 		  = price;
    	this.qty 		  = qty;
    }
    public void openShort(double price, double qty){
    	this.side 			= SIDE_SELL;
    	this.position_idx 	= POSITION_IDX_SHORT;
    	this.position	    = "SHORT";
    	this.price 			= price;
    	this.qty 			= qty;
    }
    public void closeLong(double price, double qty) {
    	this.side 			= SIDE_SELL;
    	this.position_idx 	= POSITION_IDX_LONG;
    	this.position	  = "LONG";
    	this.price 			= price;
    	this.qty 			= qty;
    }
    public void closeShort(double price, double qty) {
    	this.side 			= SIDE_BUY;
    	this.position_idx 	= POSITION_IDX_SHORT;
    	this.position	    = "SHORT";
    	this.price 			= price;
    	this.qty 			= qty;
    }
    public String getActionString() {
    	if(this.side.equals(SIDE_BUY) && this.position_idx.equals(POSITION_IDX_LONG)) return "Open Long(Buy)";
    	if(this.side.equals(SIDE_SELL) && this.position_idx.equals(POSITION_IDX_LONG)) return "Close Long(Sell)";
    	if(this.side.equals(SIDE_SELL) && this.position_idx.equals(POSITION_IDX_SHORT)) return "Open Short(Sell)";
    	if(this.side.equals(SIDE_BUY) && this.position_idx.equals(POSITION_IDX_SHORT)) return "Close Short(Buy)";
    	return "UNKNOWN";
    }
    public String getReverseActionString() {
    	if(this.side.equals(SIDE_BUY) && this.position_idx.equals(POSITION_IDX_LONG)) return "Close Long(Sell)"; 
    	if(this.side.equals(SIDE_SELL) && this.position_idx.equals(POSITION_IDX_LONG)) return "Open Long(Buy)";
    	if(this.side.equals(SIDE_SELL) && this.position_idx.equals(POSITION_IDX_SHORT)) return "Close Short(Buy)";
    	if(this.side.equals(SIDE_BUY) && this.position_idx.equals(POSITION_IDX_SHORT)) return "Open Short(Sell)";
    	return "UNKNOWN";
    }
	@Override
	public String toString() {
		return "Order [side=" + side+ ", position=" + position_idx + ", price=" + price + ", qty=" + qty
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
	public String getPosition_name() {
		if(position_idx.equals("0")) return "One Wary";
		if(position_idx.equals("1")) return "Long";
		if(position_idx.equals("2")) return "Short";
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
