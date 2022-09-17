package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmPrice {
	public static Logger LOG =   LoggerFactory.getLogger(AlarmPrice.class.getName());
	
	public AlarmPriceManager manager;
	
	double price 		= 0;
	boolean is_over 	= false; //true <= over , false >= under
	BybitTrade tr 		= null;
	boolean is_reverse 	= false;
	
	public AlarmPrice(double price, boolean is_over,boolean is_reverse) {
		this.price 		= price;
		this.is_over 	= is_over;
		this.is_reverse = is_reverse;
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
				LOG.info("Reverse Execute!");
					String position = tr.getPosition_idx();
					String side		= tr.getSide();
					AlarmPrice newAlarm = null;
					if(position.equals(BybitTrade.POSITION_IDX_BUY)) {		// Long
						if(side.equals(BybitTrade.SIDE_BUY)) {				//openLong
							
							newAlarm = manager.createCloseLong(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
							
						}else if(side.equals(BybitTrade.SIDE_SELL)) {		//closeLong		
							
							newAlarm = manager.createOpenLong(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
						}
						
					}else if(position.equals(BybitTrade.POSITION_IDX_SELL))	{ // Short
						
						if(side.equals(BybitTrade.SIDE_BUY)) {				  // closeShort
							
							newAlarm = manager.createOpenShort(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
						
						}else if(side.equals(BybitTrade.SIDE_SELL)) {		 // openShort
							
							newAlarm = manager.createCloseShort(tr.getPrice(), !is_over, price, tr.getQty(),is_reverse);
						}
					}
					LOG.info("★★★★★★★★★★\t Reverse Action \t★★★★★★★★★★★★★");
					LOG.info(newAlarm.toString());
				
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
		return "AlarmPrice [price=" + price + ", is_over=" + is_over + ", tr=" + tr.toString() + "]";
	}
	
}
