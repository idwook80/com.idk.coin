package com.idk.coin.upbit;

import java.math.BigDecimal;

import com.google.gson.JsonObject;

public class Ticker {
	String market;//	종목 구분 코드	String
	String trade_date;//		최근 거래 일자(UTC)	String
	String trade_time;//		최근 거래 시각(UTC)	String
	String trade_date_kst;//		최근 거래 일자(KST)	String
	String trade_time_kst;//		최근 거래 시각(KST)	String
	BigDecimal opening_price;//		시가	Double
	BigDecimal high_price;//		고가	Double
	BigDecimal low_price;//		저가	Double
	BigDecimal trade_price;//		종가	Double
	BigDecimal prev_closing_price;//		전일 종가	Double
	String change;//	EVEN : 보합 RISE : 상승FALL : 하락	String
	BigDecimal change_price;//		변화액의 절대값	Double
	BigDecimal change_rate;//		변화율의 절대값	Double
	BigDecimal signed_change_pric;//	e	부호가 있는 변화액	Double
	BigDecimal signed_change_rate;//		부호가 있는 변화율	Double
	BigDecimal trade_volume;//		가장 최근 거래량	Double
	BigDecimal acc_trade_price;//		누적 거래대금(UTC 0시 기준)	Double
	BigDecimal acc_trade_price_24h;//		24시간 누적 거래대금	Double
	BigDecimal acc_trade_volume;//		누적 거래량(UTC 0시 기준)	Double
	BigDecimal acc_trade_volume_24h;//		24시간 누적 거래량	Double
	BigDecimal highest_52_week_price;//		52주 신고가	Double
	String highest_52_week_date;//		52주 신고가 달성일	String
	BigDecimal lowest_52_week_price;//		52주 신저가	Double
	String lowest_52_week_date;//		52주 신저가 달성일	String
	long timestamp;//		타임스탬프	Long
	
	
	public Ticker() {
		
	}
	
	public void setJsonObject(JsonObject obj) {
		if(!obj.get("market").isJsonNull()) setMarket(obj.get("market").getAsString());
		if(!obj.get("trade_date").isJsonNull()) setTrade_date(obj.get("trade_date").getAsString());
		if(!obj.get("trade_time").isJsonNull()) setTrade_time(obj.get("trade_time").getAsString());
		if(!obj.get("trade_date_kst").isJsonNull()) setTrade_date_kst(obj.get("trade_date_kst").getAsString());
		if(!obj.get("trade_time_kst").isJsonNull()) setTrade_time_kst(obj.get("trade_time_kst").getAsString());
		
		if(!obj.get("opening_price").isJsonNull()) setOpening_price(obj.get("opening_price").getAsBigDecimal());
		if(!obj.get("low_price").isJsonNull()) setLow_price(obj.get("low_price").getAsBigDecimal());
		if(!obj.get("trade_price").isJsonNull()) setTrade_price(obj.get("trade_price").getAsBigDecimal());
		if(!obj.get("prev_closing_price").isJsonNull()) setPrev_closing_price(obj.get("prev_closing_price").getAsBigDecimal());
		
		if(!obj.get("change").isJsonNull()) setChange(obj.get("change").getAsString());
		
		if(!obj.get("change_price").isJsonNull()) setChange_price(obj.get("change_price").getAsBigDecimal());
		if(!obj.get("change_rate").isJsonNull()) setChange_rate(obj.get("change_rate").getAsBigDecimal());
		if(!obj.get("signed_change_pric").isJsonNull()) setSigned_change_pric(obj.get("signed_change_pric").getAsBigDecimal());
		if(!obj.get("signed_change_rate").isJsonNull()) setSigned_change_rate(obj.get("signed_change_rate").getAsBigDecimal());
		if(!obj.get("trade_volume").isJsonNull()) setTrade_volume(obj.get("trade_volume").getAsBigDecimal());
		if(!obj.get("acc_trade_price").isJsonNull()) setAcc_trade_price(obj.get("acc_trade_price").getAsBigDecimal());
		if(!obj.get("acc_trade_price_24h").isJsonNull()) setAcc_trade_price_24h(obj.get("acc_trade_price_24h").getAsBigDecimal());
		if(!obj.get("acc_trade_volume").isJsonNull()) setAcc_trade_volume(obj.get("acc_trade_volume").getAsBigDecimal());
		if(!obj.get("acc_trade_volume_24h").isJsonNull()) setAcc_trade_volume_24h(obj.get("acc_trade_volume_24h").getAsBigDecimal());
		if(!obj.get("highest_52_week_price").isJsonNull()) setHighest_52_week_price(obj.get("highest_52_week_price").getAsBigDecimal());
		
		if(!obj.get("highest_52_week_date").isJsonNull()) setHighest_52_week_date(obj.get("highest_52_week_date").getAsString());
		if(!obj.get("lowest_52_week_price").isJsonNull()) setLowest_52_week_price(obj.get("lowest_52_week_price").getAsBigDecimal());
		if(!obj.get("lowest_52_week_date").isJsonNull()) setLowest_52_week_date(obj.get("lowest_52_week_date").getAsString());
		if(!obj.get("timestamp").isJsonNull()) setTimestamp(obj.get("timestamp").getAsLong());
		
		
	}

	public String getMarket() {
		return market;
	}


	public void setMarket(String market) {
		this.market = market;
	}


	public String getTrade_date() {
		return trade_date;
	}


	public void setTrade_date(String trade_date) {
		this.trade_date = trade_date;
	}


	public String getTrade_time() {
		return trade_time;
	}


	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}


	public String getTrade_date_kst() {
		return trade_date_kst;
	}


	public void setTrade_date_kst(String trade_date_kst) {
		this.trade_date_kst = trade_date_kst;
	}


	public String getTrade_time_kst() {
		return trade_time_kst;
	}


	public void setTrade_time_kst(String trade_time_kst) {
		this.trade_time_kst = trade_time_kst;
	}


	public BigDecimal getOpening_price() {
		return opening_price;
	}


	public void setOpening_price(BigDecimal opening_price) {
		this.opening_price = opening_price;
	}


	public BigDecimal getHigh_price() {
		return high_price;
	}


	public void setHigh_price(BigDecimal high_price) {
		this.high_price = high_price;
	}


	public BigDecimal getLow_price() {
		return low_price;
	}


	public void setLow_price(BigDecimal low_price) {
		this.low_price = low_price;
	}


	public BigDecimal getTrade_price() {
		return trade_price;
	}


	public void setTrade_price(BigDecimal trade_price) {
		this.trade_price = trade_price;
	}


	public BigDecimal getPrev_closing_price() {
		return prev_closing_price;
	}


	public void setPrev_closing_price(BigDecimal prev_closing_price) {
		this.prev_closing_price = prev_closing_price;
	}


	public String getChange() {
		return change;
	}


	public void setChange(String change) {
		this.change = change;
	}


	public BigDecimal getChange_price() {
		return change_price;
	}


	public void setChange_price(BigDecimal change_price) {
		this.change_price = change_price;
	}


	public BigDecimal getChange_rate() {
		return change_rate;
	}


	public void setChange_rate(BigDecimal change_rate) {
		this.change_rate = change_rate;
	}


	public BigDecimal getSigned_change_pric() {
		return signed_change_pric;
	}


	public void setSigned_change_pric(BigDecimal signed_change_pric) {
		this.signed_change_pric = signed_change_pric;
	}


	public BigDecimal getSigned_change_rate() {
		return signed_change_rate;
	}


	public void setSigned_change_rate(BigDecimal signed_change_rate) {
		this.signed_change_rate = signed_change_rate;
	}


	public BigDecimal getTrade_volume() {
		return trade_volume;
	}


	public void setTrade_volume(BigDecimal trade_volume) {
		this.trade_volume = trade_volume;
	}


	public BigDecimal getAcc_trade_price() {
		return acc_trade_price;
	}


	public void setAcc_trade_price(BigDecimal acc_trade_price) {
		this.acc_trade_price = acc_trade_price;
	}


	public BigDecimal getAcc_trade_price_24h() {
		return acc_trade_price_24h;
	}


	public void setAcc_trade_price_24h(BigDecimal acc_trade_price_24h) {
		this.acc_trade_price_24h = acc_trade_price_24h;
	}


	public BigDecimal getAcc_trade_volume() {
		return acc_trade_volume;
	}


	public void setAcc_trade_volume(BigDecimal acc_trade_volume) {
		this.acc_trade_volume = acc_trade_volume;
	}


	public BigDecimal getAcc_trade_volume_24h() {
		return acc_trade_volume_24h;
	}


	public void setAcc_trade_volume_24h(BigDecimal acc_trade_volume_24h) {
		this.acc_trade_volume_24h = acc_trade_volume_24h;
	}


	public BigDecimal getHighest_52_week_price() {
		return highest_52_week_price;
	}


	public void setHighest_52_week_price(BigDecimal highest_52_week_price) {
		this.highest_52_week_price = highest_52_week_price;
	}


	public String getHighest_52_week_date() {
		return highest_52_week_date;
	}


	public void setHighest_52_week_date(String highest_52_week_date) {
		this.highest_52_week_date = highest_52_week_date;
	}


	public BigDecimal getLowest_52_week_price() {
		return lowest_52_week_price;
	}


	public void setLowest_52_week_price(BigDecimal lowest_52_week_price) {
		this.lowest_52_week_price = lowest_52_week_price;
	}


	public String getLowest_52_week_date() {
		return lowest_52_week_date;
	}


	public void setLowest_52_week_date(String lowest_52_week_date) {
		this.lowest_52_week_date = lowest_52_week_date;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
