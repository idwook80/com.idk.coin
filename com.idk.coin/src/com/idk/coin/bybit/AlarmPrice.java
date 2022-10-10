package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.AlarmSound;

public class AlarmPrice {
	public static Logger LOG =   LoggerFactory.getLogger(AlarmPrice.class.getName());
	
	public AlarmPriceManager manager;
	
	double price 		= 0;
	boolean is_over 	= false; //true <= over , false >= under
	BybitTrade tr 		= null;
	boolean is_reverse 	= false;
	AlarmPrice next		= null;
	
	public AlarmPrice(double price, boolean is_over,boolean is_reverse) {
		this.price 		= price;
		this.is_over 	= is_over;
		this.is_reverse = is_reverse;
	}
	public void setNextAlarm(AlarmPrice next) {
		this.next = next;
	}
	public boolean compare(double price) {
		if(is_over) {
			return this.price <= price;
		}else {
			return this.price >= price;
		}
	}
	public void action() {
		LOG.info("★★★★★★★★★★\t" + price + " is " + ( is_over ? "over price" : "under price" )  + "\t★★★★★★★★★★★★★");
		if(tr != null) {
			double ret_code = tr.executAction();
			
			if(ret_code == 0 && is_reverse) {
				AlarmSound.playAlert();
				LOG.info("Reverse Execute!");
					String position 	= tr.getPosition_idx();
					String side			= tr.getSide();
					AlarmPrice newAlarm = null;
					
					if(position.equals(BybitTrade.POSITION_IDX_LONG)) {		// Long
						if(side.equals(BybitTrade.SIDE_BUY)) {				// Open Long  --> Close Long
							newAlarm = manager.createCloseLong(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
						}else if(side.equals(BybitTrade.SIDE_SELL)) {		// Close Long  --> Open Long
							newAlarm = manager.createOpenLong(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
						}
					}else if(position.equals(BybitTrade.POSITION_IDX_SHORT))	{ // Short 
						if(side.equals(BybitTrade.SIDE_BUY)) {				  // Close Short --> Open Short
							newAlarm = manager.createOpenShort(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
						}else if(side.equals(BybitTrade.SIDE_SELL)) {		 // Open Short ---> Close Short
							newAlarm = manager.createCloseShort(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
						}
					}
				LOG.info("★★★★★★★★★★\t Reverse Action \t★★★★★★★★★★★★★");
				LOG.info(newAlarm.toString());
				
			}else {
				if(ret_code == 0) {
					AlarmSound.playAlert();
					if(next != null) manager.addAlarm(next);
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
	
	@Override
	public String toString() {
		/*return "AlarmPrice [" + price + ", " + (is_over ? "Over" : "Under") + ", " + (is_reverse ? "R&R" : "Once") + "]" 
				+ ", [" + tr.getActionString()+"] " + tr.toString()+" " +  (next != null ?  "--> "+next.toString() : "") ;*/
		return "Alarm : [$" + price + "] " + (is_over ? "Over" : "Under") + " [" + tr.getActionString() + "] " +tr.getPrice() + " ," + tr.getQty() +","+ (is_reverse ? "R&R" : "Once") 
				+ " " + (next != null ?  " --> " + next.toString() : "");
 		
	}
	
}
