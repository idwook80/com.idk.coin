package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.AlarmSound;
import com.idk.coin.bybit.model.Order;

public class AlarmPrice {
	public static Logger LOG =   LoggerFactory.getLogger(AlarmPrice.class.getName());
	
	public AlarmPriceManager manager;
	
	String order_id			= null;
	String parent_order_id	= null;
	boolean is_executed		= false;
	double trigger 		= 0;
	boolean is_over 	= false; //true <= over , false >= under
	BybitTrade tr 		= null;
	boolean is_reverse 	= false;
	AlarmPrice next		= null;
	
	public AlarmPrice(double trigger, boolean is_over,boolean is_reverse) {
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
	public void action() {
		LOG.info("★★★★★★★★★★\t trigger : " + trigger + " is " + ( is_over ? "over price" : "under price" )  + "\t★★★★★★★★★★★★★ pid : " + this.getParent_order_id());
		if(tr != null) {
			Order order  = tr.executAction();
			//ret_code = tr.executAction();
			if(order != null) {
				this.setOrder_id(order.getOrder_id());
				if(is_reverse) LOG.info("Reverse & Repeat Order Done!");
				else LOG.info("Once Order Done!");
			}
			if(order != null && is_reverse) {
				AlarmSound.playAlert();
				LOG.info("★★★★★★★★★★\t Reverse & Repeat Setting   \t★★★★★★★★★★★★★");
				String position 	= tr.getPosition_idx();
				String side			= tr.getSide();
				AlarmPrice newAlarm = null;
				
				if(position.equals(BybitTrade.POSITION_IDX_LONG)) {		// Long
					if(side.equals(BybitTrade.SIDE_BUY)) {				// Open Long  --> Close Long
						newAlarm = manager.createCloseLong(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}else if(side.equals(BybitTrade.SIDE_SELL)) {		// Close Long  --> Open Long
						newAlarm = manager.createOpenLong(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}
				}else if(position.equals(BybitTrade.POSITION_IDX_SHORT))	{ // Short 
					if(side.equals(BybitTrade.SIDE_BUY)) {				  // Close Short --> Open Short
						newAlarm = manager.createOpenShort(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}else if(side.equals(BybitTrade.SIDE_SELL)) {		 // Open Short ---> Close Short
						newAlarm = manager.createCloseShort(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}
					newAlarm.setParent_order_id(getOrder_id());
				}
				LOG.info(newAlarm.toString());
			}else {
				if(order != null) {
					AlarmSound.playAlert();
					if(next != null) {
						next.setParent_order_id(getOrder_id());
						manager.addAlarm(next);
					}
				}
				else {
					AlarmSound.playDistress();
					LOG.error("[ORDER ACTION ERROR]");
					LOG.error(tr.toString());
				}
			}
		
			
		}
	}

	
	
	public void setOpenLongAction(double price, double qty) {
		tr = new BybitTrade();
		tr.openLong(price, qty);
	}
	public void setOpenShortAction(double price, double qty) {
		tr = new BybitTrade();
		tr.openShort(price, qty);
	}
	public void setCloseLongAction(double price, double qty) {
		tr = new BybitTrade();
		tr.closeLong(price, qty);
	}
	public void setCloseShortAction(double price,double qty) {
		tr = new BybitTrade();
		tr.closeShort(price, qty);
	}
	public void setManager(AlarmPriceManager manager) {
		this.manager = manager;
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
		return "Alarm : [" + trigger + "," + (is_over ? "Over" : "Under") +"],["+ tr.getPrice() + " , " + tr.getQty()  + "],[" + tr.getActionString() + "] "+" "+ (is_reverse ? "R&R" : "Once") 
				+ " " + (next != null ?  " Waiting ---> " + next.toString() : "")
				+ " pid : " +parent_order_id;
 		
	}
	
}
