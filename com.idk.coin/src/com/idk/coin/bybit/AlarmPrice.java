package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.AlarmSound;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Order;
import com.idk.coin.model.AlarmManager;
import com.idk.coin.model.TradeModel;

abstract public class AlarmPrice {
	public static Logger LOG =   LoggerFactory.getLogger(AlarmPrice.class.getName());
	
	public String order_id			= null;
	public String parent_order_id	= null;
	public boolean is_executed		= false;
	public double trigger 		= 0;
	public boolean is_over 	= false; //true <= over , false >= under
	public TradeModel tr 		= null;
	public boolean is_reverse 	= false;
	public AlarmPrice next		= null;
	public AlarmManager manager;
	
	public AlarmPrice(AlarmManager manager, double trigger, boolean is_over,boolean is_reverse) {
		this.manager = manager;
		this.trigger 		= trigger;
		this.is_over 	= is_over;
		this.is_reverse = is_reverse;
	}
	public void setNextAlarm(AlarmPrice next) {
		this.next = next;
	}
	public boolean compare(double price) {
		if(is_over) {
			return this.trigger <= price;
		}else {
			return this.trigger >= price;
		}
	}
	public void action(BybitUser user, String symbol) throws Exception {
		action(user.getApi_key(), user.getApi_secret(), symbol);
	}
	abstract public void action(String api_key, String api_secret, String symbol) throws Exception;  
	
	public void setOpenLongAction(double price, double qty) {
		//tr = new BybitTrade();
		//tr.openLong(price, qty);
	}
	public void setOpenShortAction(double price, double qty) {
		//tr = new BybitTrade();
		//tr.openShort(price, qty);
	}
	public void setCloseLongAction(double price, double qty) {
		//tr = new BybitTrade();
		//tr.closeLong(price, qty);
	}
	public void setCloseShortAction(double price,double qty) {
		//tr = new BybitTrade();
		//tr.closeShort(price, qty);
	}
 
	public boolean is_executed() {
		return is_executed;
	}
	public void setIs_executed(boolean b) {
		this.is_executed = b;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
		this.is_executed = true;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setParent_order_id(String order_id) {
		this.parent_order_id = order_id;
	}
	public String getParent_order_id() {
		return parent_order_id;
	}
	
	@Override
	public String toString() {
		/*return "AlarmPrice [" + price + ", " + (is_over ? "Over" : "Under") + ", " + (is_reverse ? "R&R" : "Once") + "]" 
				+ ", [" + tr.getActionString()+"] " + tr.toString()+" " +  (next != null ?  "--> "+next.toString() : "") ;*/
		return "["+ manager.getUser().getId() + "] Alarm : [" + trigger + "," + (is_over ? "Over" : "Under") +"],["+ tr.getPrice() + " , " + tr.getQty()  + "],[" + tr.getActionString() + "] "+" "+ (is_reverse ? "R&R" : "Once") 
				+ " " + (next != null ?  " Waiting ---> " + next.toString() : "")
				+ " pid : " +parent_order_id;
 		
	}
	
}
