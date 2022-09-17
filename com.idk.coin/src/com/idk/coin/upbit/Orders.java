package com.idk.coin.upbit;

import java.util.ArrayList;

import com.google.gson.JsonObject;

public class Orders {
	ArrayList<Order> orders;
	public Orders() {
		orders = new ArrayList<>();
	}
	public void addOrder(JsonObject obj) {
		String uuid = obj.get("uuid").getAsString();
		Order order = getOrder(uuid);
		if(order == null) {
			order = new Order(uuid);
			add(order);
		}
		order.setJsonObject(obj);
	}
	public void add(Order order) {
		synchronized (orders) {
			orders.add(order);
		}
	}
	public void remove(Order order) {
		synchronized (orders) {
			orders.remove(order);
		}
	}
	public Order getOrder(String uuid) {
		synchronized(orders) {
			for(Order o : orders) {
				if(o.getUuid().equals(uuid)) {
					return o;
				}
			}
		}
		return null;
	}
	public Order[] getOrders() {
		return orders.toArray(new Order[0]);
	}
	public boolean isEmpty() {
		return orders.isEmpty();
	}
	public int getSize() {
		return orders.size();
	}
}
