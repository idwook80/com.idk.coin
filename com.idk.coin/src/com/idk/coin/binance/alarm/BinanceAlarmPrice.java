package com.idk.coin.binance.alarm;

import com.idk.coin.AlarmSound;
import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.BybitTrade;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.model.AlarmManager;
import com.idk.coin.model.TradeModel;

public class BinanceAlarmPrice extends AlarmPrice {
	public BinanceAlarmPrice(AlarmManager manager, double trigger, boolean is_over,boolean is_reverse) {
		super(manager, trigger, is_over, is_reverse);
	}
	public void action(BybitUser user, String symbol) throws Exception {
		action(user.getApi_key(), user.getApi_secret(), symbol);
	}
	public void action(String api_key, String api_secret, String symbol) throws Exception {
		LOG.info("★★★★★★★★★★\t trigger : " + trigger + " is " + ( is_over ? "over price" : "under price" )  + "\t★★★★★★★★★★★★★ pid : " + this.getParent_order_id());
		if(tr != null) {
			System.out.println(tr);
			com.binance.client.model.trade.Order order  =
					(com.binance.client.model.trade.Order) tr.executAction(api_key, api_secret, symbol);
			if(order != null && !is_reverse) {
				this.setOrder_id(order.getClientOrderId());
				if(is_reverse) LOG.info("Reverse & Repeat Order Action!");
				else LOG.info("Once Order Done!");
			}
			if(order != null && is_reverse && next == null) {
				AlarmSound.playAlert();
				LOG.info("★★★★★★★★★★\t Reverse & Repeat Setting   \t★★★★★★★★★★★★★");
				String position 	= tr.getPosition_idx();
				String side			= tr.getSide();
				AlarmPrice newAlarm = null;
				
				if(position.equals(TradeModel.POSITION_IDX_LONG)) {		// Long
					if(side.equals(BybitTrade.SIDE_BUY)) {				// Open Long  --> Close Long
						newAlarm = manager.addCloseLong(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}else if(side.equals(TradeModel.SIDE_SELL)) {		// Close Long  --> Open Long
						newAlarm = manager.addOpenLong(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}
				}else if(position.equals(TradeModel.POSITION_IDX_SHORT))	{ // Short 
					if(side.equals(BybitTrade.SIDE_BUY)) {				  // Close Short --> Open Short
						newAlarm = manager.addOpenShort(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}else if(side.equals(TradeModel.SIDE_SELL)) {		 // Open Short ---> Close Short
						newAlarm = manager.addCloseShort(tr.getPrice(), !is_over, trigger, tr.getQty(),is_reverse);
					}
				}
				newAlarm.setParent_order_id(getOrder_id());
				LOG.info(newAlarm.toString());
			}else {
				if(order != null) {
					AlarmSound.playAlert();
					if(next != null) {
						next.setParent_order_id(getOrder_id());
						manager.addAlarm(next);
						LOG.info(next.toString());
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
		tr = new BinanceTrade();
		tr.openLong(price, qty);
	}
	public void setOpenShortAction(double price, double qty) {
		tr = new BinanceTrade();
		tr.openShort(price, qty);
	}
	public void setCloseLongAction(double price, double qty) {
		tr = new BinanceTrade();
		tr.closeLong(price, qty);
	}
	public void setCloseShortAction(double price,double qty) {
		tr = new BinanceTrade();
		tr.closeShort(price, qty);
	}
}
