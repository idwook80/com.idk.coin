package com.idk.coin.bybit.mocel;

import java.util.Map;

public class OrderExecution {
	String symbol;
	String side;
	String order_id;
	String exec_id;
	String order_link_id;
	double price;
	double order_qty;
	String exec_type;
	double exec_qty;
	double exec_fee;
	double leaves_qty;
	boolean is_maker;
	String trade_time;
	
	public OrderExecution() {}
	public OrderExecution(Map map) {
		symbol 		= map.get("symbol").toString();
		side 		= map.get("side").toString();
		order_id 	= map.get("order_id").toString();
		exec_id 	= map.get("exec_id").toString();
		order_link_id = map.get("order_link_id").toString();
		price 		= Double.valueOf(map.get("price").toString());
		order_qty 	= Double.valueOf(map.get("order_qty").toString());
		exec_type 	= map.get("exec_type").toString();
		exec_qty 	= Double.valueOf(map.get("exec_qty").toString());
		exec_fee 	= Double.valueOf(map.get("exec_fee").toString());
		leaves_qty 	= Double.valueOf(map.get("leaves_qty").toString());
		is_maker 	= Boolean.valueOf(map.get("is_maker").toString());
		trade_time 	= map.get("trade_time").toString();
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
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getExec_id() {
		return exec_id;
	}
	public void setExec_id(String exec_id) {
		this.exec_id = exec_id;
	}
	public String getOrder_link_id() {
		return order_link_id;
	}
	public void setOrder_link_id(String order_link_id) {
		this.order_link_id = order_link_id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getOrder_qty() {
		return order_qty;
	}
	public void setOrder_qty(double order_qty) {
		this.order_qty = order_qty;
	}
	public String getExec_type() {
		return exec_type;
	}
	public void setExec_type(String exec_type) {
		this.exec_type = exec_type;
	}
	public double getExec_qty() {
		return exec_qty;
	}
	public void setExec_qty(double exec_qty) {
		this.exec_qty = exec_qty;
	}
	public double getExec_fee() {
		return exec_fee;
	}
	public void setExec_fee(double exec_fee) {
		this.exec_fee = exec_fee;
	}
	public double getLeaves_qty() {
		return leaves_qty;
	}
	public void setLeaves_qty(double leaves_qty) {
		this.leaves_qty = leaves_qty;
	}
	public boolean is_maker() {
		return is_maker;
	}
	public void setIs_maker(boolean is_maker) {
		this.is_maker = is_maker;
	}
	public String getTrade_time() {
		return trade_time;
	}
	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}
	@Override
	public String toString() {
		return "OrderExecution [symbol=" + symbol + ", side=" + side + ", order_id=" + order_id + ", exec_id=" + exec_id
				+ ", order_link_id=" + order_link_id + ", price=" + price + ", order_qty=" + order_qty + ", exec_type="
				+ exec_type + ", exec_qty=" + exec_qty + ", exec_fee=" + exec_fee + ", leaves_qty=" + leaves_qty
				+ ", is_maker=" + is_maker + ", trade_time=" + trade_time + "]";
	}
	
	
}	
