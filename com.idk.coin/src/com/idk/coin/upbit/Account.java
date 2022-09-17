package com.idk.coin.upbit;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import com.google.gson.JsonObject;

public class Account {
	String 	currency;									//	화폐를 의미하는 영문 대문자 코드	String
	BigDecimal 	balance = new BigDecimal(0);			//	주문가능 금액/수량	NumberString
	BigDecimal 	locked  = new BigDecimal(0);			//	주문 중 묶여있는 금액/수량	NumberString
	BigDecimal 	avg_buy_price = new BigDecimal(0);		//	매수평균가	NumberString
	boolean avg_buy_price_modified 	= true;				//	매수평균가 수정 여부	Boolean
	String 	unit_currency;								//  평단가 기준 화폐	String
	
	BigDecimal trade_price;
	Date 		trade_date;
	
	
	public Account(String currency,String unit_currency) {
		setCurrency(currency);
		setUnit_currency(unit_currency);
	}
	public void setJsonObject(JsonObject obj){
		BigDecimal balance = obj.get("balance").getAsBigDecimal();
		BigDecimal locked = obj.get("locked").getAsBigDecimal();
		BigDecimal avg_buy_price = obj.get("avg_buy_price").getAsBigDecimal();
		boolean avg_buy_price_modified = obj.get("avg_buy_price_modified").getAsBoolean();
		setAccountValue(balance, locked, avg_buy_price, avg_buy_price_modified);
		
	}
	public void setAccountValue(BigDecimal balance, BigDecimal locked, BigDecimal avg_buy_price, boolean avg_buy_price_modified) {
		setBalance(balance);
		setLocked(locked);
		setAvg_buy_price(avg_buy_price);
		setAvg_buy_price_modified(avg_buy_price_modified);
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getLocked() {
		return locked;
	}
	public void setLocked(BigDecimal locked) {
		this.locked = locked;
	}
	public BigDecimal getAvg_buy_price() {
		return avg_buy_price;
	}
	public void setAvg_buy_price(BigDecimal avg_buy_price) {
		this.avg_buy_price =  avg_buy_price;
	}
	public boolean isAvg_buy_price_modified() {
		return avg_buy_price_modified;
	}
	public void setAvg_buy_price_modified(boolean avg_buy_price_modified) {
		this.avg_buy_price_modified = avg_buy_price_modified;
	}
	public String getUnit_currency() {
		return unit_currency;
	}
	public void setUnit_currency(String unit_currency) {
		this.unit_currency = unit_currency;
	
	}
	public BigDecimal getTotal_balance() {
		return avg_buy_price.multiply(balance.add(locked));
	}
	public BigDecimal getTrade_balance() {
		if(trade_price == null) return new BigDecimal(0);
		return trade_price.multiply(balance.add(locked));
	}
	
	public BigDecimal getTrade_price() {
		return trade_price;
	}
	public void setTrade_price(BigDecimal trade_price) {
		this.trade_price = trade_price;
	}
	public double getProfit(){
			return   getTrade_balance().subtract(getTotal_balance()).doubleValue();
	}
	public double getProfitPer() {
		   return  getProfit() / getTrade_balance().doubleValue() * 100;
	}
	
	public Date getTrade_date() {
		return trade_date;
	}
	public void setTrade_date(Date trade_date) {
		this.trade_date = trade_date;
	}
	@Override
	public String toString() {
		return "Account [currency=" + currency + ", balance=" + balance + ", locked=" + locked + ", avg_buy_price="
				+ avg_buy_price + ", avg_buy_price_modified=" + avg_buy_price_modified + ", unit_currency="
				+ unit_currency + ", trade_price=" + trade_price + ", trade_date=" + trade_date + "]"
				+ "[total_balance=" + getTotal_balance() + ", trande_balance=" + getTrade_balance()+", trade_price="+getTrade_price()
				+ ", profit="+getProfit() + ", profit percetage="+getProfitPer()+"]";
	}
	
	
}
