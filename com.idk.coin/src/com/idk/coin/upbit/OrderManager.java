package com.idk.coin.upbit;

public class OrderManager {
	String market;
	
	Orders buys;
	Orders sells;
	
	
	public OrderManager(String market) {
		this.market = market;
		buys 	= new Orders();
		sells 	= new Orders();
	}
	
	public void addBuys(Order order) {
		buys.add(order);;
	}
	public void removeBuys(Order order) {
		buys.remove(order);
	}
	public Order[] getBuyOrders() {
		return buys.getOrders();
	}
	
	public void addSells(Order order) {
		sells.add(order);
	}
	public void removeSells(Order order) {
		sells.remove(order);
	}
	public Order[] getSellOrders() {
		return sells.getOrders();
	}
	
	public int getSize() {
		return getBuySize() + getSellSize();
	}
	public int getBuySize() {
		return buys.getSize();
	}
	public int getSellSize() {
		return sells.getSize();
	}
}
