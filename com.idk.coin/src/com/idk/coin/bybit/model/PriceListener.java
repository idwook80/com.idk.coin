package com.idk.coin.bybit.model;

public interface PriceListener {
	public void checkAlarmExecution(OrderExecution execution);
	public void checkAlarmPrice(double price);
	public String toString();
}
