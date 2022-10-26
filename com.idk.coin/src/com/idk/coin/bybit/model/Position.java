package com.idk.coin.bybit.model;

import java.util.List;
import java.util.Map;

public class Position {
	String user_id 				= "";
	String symbol 				= "";
	String side					= "";
	double size					= 0;
	double position_value 		= 0;
	double entry_price 			= 0;
	double liq_price 			= 0;
	double bust_price 			= 0;
	double leverage 			= 0;
	double auto_add_margin 		= 0;
	boolean is_isolated 		= false;
	double position_margin 		= 0;
	double occ_closing_fee 		= 0;
	double realised_pnl 		= 0;
	double cum_realised_pnl 	= 0;
	double free_qty 			= 0;
	String tp_sl_mode 			= "";
	double unrealised_pnl 		= 0;
	double deleverage_indicator = 0;
	double risk_id 				= 9;
	double stop_loss 			= 0;
	double take_profit 			= 0;
	double trailing_stop		= 0;
	double position_idx			= 0;
	String mode 				= "";
	
	public Position() {
		
	}
	public Position(Map map) {
		setMap(map);
	}
	public void setMap(Map map) {
		map.get("user_id");
		user_id 			= map.get("user_id").toString();
		symbol 				= map.get("symbol").toString();
		side				= map.get("side").toString();
		size				= Double.valueOf(map.get("size").toString());
		position_value 		= Double.valueOf(map.get("position_value").toString());
		entry_price 		= Double.valueOf(map.get("entry_price").toString());
		liq_price 			= Double.valueOf( map.get("liq_price").toString());
		bust_price 			= Double.valueOf( map.get("bust_price").toString());
		leverage 			= Double.valueOf(map.get("leverage").toString());
		auto_add_margin 	= Double.valueOf(map.get("auto_add_margin").toString());
		is_isolated 		= Boolean.valueOf(map.get("is_isolated").toString());
		position_margin 	= Double.valueOf(map.get("position_margin").toString());
		occ_closing_fee 	= Double.valueOf(map.get("occ_closing_fee").toString());
		realised_pnl 		= Double.valueOf(map.get("realised_pnl").toString());
		cum_realised_pnl 	= Double.valueOf(map.get("cum_realised_pnl").toString());
		free_qty 			= Double.valueOf(map.get("free_qty").toString());
		tp_sl_mode 			= map.get("tp_sl_mode").toString();
		unrealised_pnl 		= Double.valueOf(map.get("unrealised_pnl").toString());
		deleverage_indicator = Double.valueOf(map.get("deleverage_indicator").toString());
		risk_id 			= Double.valueOf(map.get("risk_id").toString());
		stop_loss 			= Double.valueOf(map.get("stop_loss").toString());
		take_profit 		= Double.valueOf(map.get("take_profit").toString());
		trailing_stop		= Double.valueOf(map.get("trailing_stop").toString());
		position_idx		= Double.valueOf(map.get("position_idx").toString());
		mode 				= map.get("mode").toString();
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public double getPosition_value() {
		return position_value;
	}
	public void setPosition_value(double position_value) {
		this.position_value = position_value;
	}
	public double getEntry_price() {
		return entry_price;
	}
	public void setEntry_price(double entry_price) {
		this.entry_price = entry_price;
	}
	public double getLiq_price() {
		return liq_price;
	}
	public void setLiq_price(double liq_price) {
		this.liq_price = liq_price;
	}
	public double getBust_price() {
		return bust_price;
	}
	public void setBust_price(double bust_price) {
		this.bust_price = bust_price;
	}
	public double getLeverage() {
		return leverage;
	}
	public void setLeverage(double leverage) {
		this.leverage = leverage;
	}
	public double getAuto_add_margin() {
		return auto_add_margin;
	}
	public void setAuto_add_margin(double auto_add_margin) {
		this.auto_add_margin = auto_add_margin;
	}
	public boolean is_isolated() {
		return is_isolated;
	}
	public void setIs_isolated(boolean is_isolated) {
		this.is_isolated = is_isolated;
	}
	public double getPosition_margin() {
		return position_margin;
	}
	public void setPosition_margin(double position_margin) {
		this.position_margin = position_margin;
	}
	public double getOcc_closing_fee() {
		return occ_closing_fee;
	}
	public void setOcc_closing_fee(double occ_closing_fee) {
		this.occ_closing_fee = occ_closing_fee;
	}
	public double getRealised_pnl() {
		return realised_pnl;
	}
	public void setRealised_pnl(double realised_pnl) {
		this.realised_pnl = realised_pnl;
	}
	public double getCum_realised_pnl() {
		return cum_realised_pnl;
	}
	public void setCum_realised_pnl(double cum_realised_pnl) {
		this.cum_realised_pnl = cum_realised_pnl;
	}
	public double getFree_qty() {
		return free_qty;
	}
	public void setFree_qty(double free_qty) {
		this.free_qty = free_qty;
	}
	public String getTp_sl_mode() {
		return tp_sl_mode;
	}
	public void setTp_sl_mode(String tp_sl_mode) {
		this.tp_sl_mode = tp_sl_mode;
	}
	public double getUnrealised_pnl() {
		return unrealised_pnl;
	}
	public void setUnrealised_pnl(double unrealised_pnl) {
		this.unrealised_pnl = unrealised_pnl;
	}
	public double getDeleverage_indicator() {
		return deleverage_indicator;
	}
	public void setDeleverage_indicator(double deleverage_indicator) {
		this.deleverage_indicator = deleverage_indicator;
	}
	public double getRisk_id() {
		return risk_id;
	}
	public void setRisk_id(double risk_id) {
		this.risk_id = risk_id;
	}
	public double getStop_loss() {
		return stop_loss;
	}
	public void setStop_loss(double stop_loss) {
		this.stop_loss = stop_loss;
	}
	public double getTake_profit() {
		return take_profit;
	}
	public void setTake_profit(double take_profit) {
		this.take_profit = take_profit;
	}
	public double getTrailing_stop() {
		return trailing_stop;
	}
	public void setTrailing_stop(double trailing_stop) {
		this.trailing_stop = trailing_stop;
	}
	public double getPosition_idx() {
		return position_idx;
	}
	public void setPosition_idx(double position_idx) {
		this.position_idx = position_idx;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "Position [user_id=" + user_id + ", symbol=" + symbol + ", side=" + side + ", size=" + size
				+ ", position_value=" + position_value + ", entry_price=" + entry_price + ", liq_price=" + liq_price
				+ ", bust_price=" + bust_price + ", leverage=" + leverage + ", auto_add_margin=" + auto_add_margin
				+ ", is_isolated=" + is_isolated + ", position_margin=" + position_margin + ", occ_closing_fee="
				+ occ_closing_fee + ", realised_pnl=" + realised_pnl + ", cum_realised_pnl=" + cum_realised_pnl
				+ ", free_qty=" + free_qty + ", tp_sl_mode=" + tp_sl_mode + ", unrealised_pnl=" + unrealised_pnl
				+ ", deleverage_indicator=" + deleverage_indicator + ", risk_id=" + risk_id + ", stop_loss=" + stop_loss
				+ ", take_profit=" + take_profit + ", trailing_stop=" + trailing_stop + ", position_idx=" + position_idx
				+ ", mode=" + mode + "]";
	}
	
	public static Position getPosition(List<Position> positions, String symbol, String side) {
			Position[] ps = positions.toArray(new Position[0]);
			for(Position p : ps) {
				if(p.getSymbol().equals(symbol) && 
					p.getSide().equals(side)) return p;
			}
		return null;
	}
	
	
}
