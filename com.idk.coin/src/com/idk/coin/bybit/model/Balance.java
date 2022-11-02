package com.idk.coin.bybit.model;

import java.util.Date;
import java.util.HashMap;

import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.util.TimeUtil;

public class Balance {
	String id;
	String symbol;
	double equity;
	double available_balance;
	double cum_realised_pnl;
	double given_cash;
	double occ_closing_fee;
	double occ_funding_fee;
	double order_margin;
	double position_margin;
	double realised_pnl;
	double service_cash;
	double unrealised_pnl;
	double used_margin;
	double wallet_balance;
	Date reg_date;
	Date reg_datetime;
	public Balance() {}
	public Balance(HashMap map) {
		setId((String)map.get("id"));
		setSymbol((String)map.get("symbol"));
		setEquity(Double.valueOf(map.get("equity").toString()));
		setAvailable_balance(Double.valueOf(map.get("available_balance").toString()));
		setCum_realised_pnl(Double.valueOf(map.get("cum_realised_pnl").toString()));
		setGiven_cash(Double.valueOf(map.get("given_cash").toString()));
		setOcc_closing_fee(Double.valueOf(map.get("occ_closing_fee").toString()));
		setOcc_funding_fee(Double.valueOf(map.get("occ_funding_fee").toString()));
		setOrder_margin(Double.valueOf(map.get("order_margin").toString()));
		setPosition_margin(Double.valueOf(map.get("position_margin").toString()));
		setRealised_pnl(Double.valueOf(map.get("realised_pnl").toString()));
		setService_cash(Double.valueOf(map.get("service_cash").toString()));
		setUnrealised_pnl(Double.valueOf(map.get("unrealised_pnl").toString()));
		setUsed_margin(Double.valueOf(map.get("used_margin").toString()));
		setWallet_balance(Double.valueOf(map.get("wallet_balance").toString()));
		try {
			setReg_date(TimeUtil.getDefaultDate((String)map.get("reg_date")+" 09:00:00"));
			setReg_datetime(TimeUtil.getDefaultDate((String)map.get("reg_datetime")));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Balance(LinkedTreeMap<String, Object> map) {
		setEquity((double)map.get("equity"));
		setAvailable_balance((double)map.get("available_balance"));
		setCum_realised_pnl((double)map.get("cum_realised_pnl"));
		setGiven_cash((double)map.get("given_cash"));
		setOcc_closing_fee((double)map.get("occ_closing_fee"));
		setOcc_funding_fee((double)map.get("occ_funding_fee"));
		setOrder_margin((double)map.get("order_margin"));
		setPosition_margin((double)map.get("position_margin"));
		setRealised_pnl((double)map.get("realised_pnl"));
		setService_cash((double)map.get("service_cash"));
		setUnrealised_pnl((double)map.get("unrealised_pnl"));
		setUsed_margin((double)map.get("used_margin"));
		setWallet_balance((double)map.get("wallet_balance"));
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getEquity() {
		return equity;
	}
	public void setEquity(double equity) {
		this.equity = equity;
	}
	public double getAvailable_balance() {
		return available_balance;
	}
	public void setAvailable_balance(double available_balance) {
		this.available_balance = available_balance;
	}
	public double getCum_realised_pnl() {
		return cum_realised_pnl;
	}
	public void setCum_realised_pnl(double cum_realised_pnl) {
		this.cum_realised_pnl = cum_realised_pnl;
	}
	public double getGiven_cash() {
		return given_cash;
	}
	public void setGiven_cash(double given_cash) {
		this.given_cash = given_cash;
	}
	public double getOcc_closing_fee() {
		return occ_closing_fee;
	}
	public void setOcc_closing_fee(double occ_closing_fee) {
		this.occ_closing_fee = occ_closing_fee;
	}
	public double getOcc_funding_fee() {
		return occ_funding_fee;
	}
	public void setOcc_funding_fee(double occ_funding_fee) {
		this.occ_funding_fee = occ_funding_fee;
	}
	public double getOrder_margin() {
		return order_margin;
	}
	public void setOrder_margin(double order_margin) {
		this.order_margin = order_margin;
	}
	public double getPosition_margin() {
		return position_margin;
	}
	public void setPosition_margin(double position_margin) {
		this.position_margin = position_margin;
	}
	public double getRealised_pnl() {
		return realised_pnl;
	}
	public void setRealised_pnl(double realised_pnl) {
		this.realised_pnl = realised_pnl;
	}
	public double getService_cash() {
		return service_cash;
	}
	public void setService_cash(double service_cash) {
		this.service_cash = service_cash;
	}
	public double getUnrealised_pnl() {
		return unrealised_pnl;
	}
	public void setUnrealised_pnl(double unrealised_pnl) {
		this.unrealised_pnl = unrealised_pnl;
	}
	public double getUsed_margin() {
		return used_margin;
	}
	public void setUsed_margin(double used_margin) {
		this.used_margin = used_margin;
	}
	public double getWallet_balance() {
		return wallet_balance;
	}
	public void setWallet_balance(double wallet_balance) {
		this.wallet_balance = wallet_balance;
	}
	public Date getReg_date() {
		return reg_date;
	}
	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}
	public Date getReg_datetime() {
		return reg_datetime;
	}
	public void setReg_datetime(Date reg_datetime) {
		this.reg_datetime = reg_datetime;
	}
	
	@Override
	public String toString() {
		return "Balance [id=" + id + ", symbol=" + symbol + ", equity=" + equity + ", available_balance="
				+ available_balance + ", cum_realised_pnl=" + cum_realised_pnl + ", given_cash=" + given_cash
				+ ", occ_closing_fee=" + occ_closing_fee + ", occ_funding_fee=" + occ_funding_fee + ", order_margin="
				+ order_margin + ", position_margin=" + position_margin + ", realised_pnl=" + realised_pnl
				+ ", service_cash=" + service_cash + ", unrealised_pnl=" + unrealised_pnl + ", used_margin="
				+ used_margin + ", wallet_balance=" + wallet_balance + ", reg_date=" + TimeUtil.getDateFormat("yyyy-MM-dd",reg_date) 
				+ ", reg_datetime=" + TimeUtil.getDateFormat("yyyy-MM-dd HH:mm:ss",reg_datetime) 
				+ "]";
	}
}
