package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.model.AlarmManager;
import com.idk.coin.model.TradeModel;

abstract public class AlarmPrice {
	public static Logger LOG =   LoggerFactory.getLogger(AlarmPrice.class.getName());
	
	
	public String alarm_id			= "0";
	public String parent_alarm_id	= "0";
	public String alarm_kind		= "P"; //price , trade /P:price T: trade
	public String order_id			= null;
	public String parent_order_id	= null;
	public boolean is_executed		= false;
	public double trigger 			= 0;
	public boolean is_over 			= false; //true <= over , false >= under
	public TradeModel tr 			= null;
	public int repeat 				= 0;
	public boolean is_active		= true;
	
	public AlarmPrice parent		= null;
	public AlarmPrice next			= null;
	public AlarmManager manager;
	
	public AlarmPrice(AlarmManager manager, double trigger, boolean is_over,int repeat) {
		this.manager 	= manager;
		this.trigger 	= trigger;
		this.is_over 	= is_over;
		this.repeat 	= repeat;
		this.alarm_kind = "P";
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
	abstract public void setOpenLongAction(double price, double qty);
	abstract public void setOpenShortAction(double price, double qty);  
	abstract public void setCloseLongAction(double price, double qty); 
	abstract public void setCloseShortAction(double price,double qty);
 
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
		this.setAlarm_kind("T");
	}
	public AlarmPrice getParent() {
		return parent;
	}
	public void setParent(AlarmPrice parent) {
		this.parent = parent;
	}
	public String getParent_order_id() {
		return parent_order_id;
	}
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	public void descreseRepeat() {
		this.repeat--;
	}
	public boolean isReverse() {
		return repeat > 0;
	}
	
	public String toAlarm() {
		return "["+trigger + "," + (is_over ? "Over" : "Under") + "]";
	}
	public String toActionString() {
		return "["+ tr.getPrice() + ","+tr.qty+ "," + tr.getActionString()+"]";
	}
	@Override
	public String toString() {
		/*return "AlarmPrice [" + price + ", " + (is_over ? "Over" : "Under") + ", " + (is_reverse ? "R&R" : "Once") + "]" 
				+ ", [" + tr.getActionString()+"] " + tr.toString()+" " +  (next != null ?  "--> "+next.toString() : "") ;*/
		return "["+getAlarm_id()+"]["+ manager.getUser().getId() + "] Alarm : [" + trigger + "," + (is_over ? "Over" : "Under") +"],["+ tr.getPrice() + " , " + tr.getQty()  + "],[" + tr.getActionString() + "] "+"[R:"+ repeat +"]"
				+ " " + (next != null ?  "Next Alarm -> " + next.toString() : "")
				+ " pid : " +parent_order_id;
 		
	}
	public String getAlarm_id() {
		return alarm_id;
	}
	public void setAlarm_id(String alarm_id) {
		this.alarm_id = alarm_id;
	}
	public String getAlarm_kind() {
		return alarm_kind;
	}
	public void setAlarm_kind(String alarm_kind) {
		this.alarm_kind = alarm_kind;
	}
	public double getTrigger() {
		return trigger;
	}
	public void setTrigger(double trigger) {
		this.trigger = trigger;
	}
	public boolean isIs_over() {
		return is_over;
	}
	public void setIs_over(boolean is_active) {
		this.is_over = is_active;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public TradeModel getTr() {
		return tr;
	}
	public void setTr(TradeModel tr) {
		this.tr = tr;
	}
	public AlarmPrice getNext() {
		return next;
	}
	public void setNext(AlarmPrice next) {
		this.next = next;
	}
	public AlarmManager getManager() {
		return manager;
	}
	public void setManager(AlarmManager manager) {
		this.manager = manager;
	}
	public boolean isIs_executed() {
		return is_executed;
	}
	public String getParent_alarm_id() {
		return parent_alarm_id;
	}
	public void setParent_alarm_id(String parent_alarm_id) {
		this.parent_alarm_id = parent_alarm_id;
	}
	
	 
	
	
}
