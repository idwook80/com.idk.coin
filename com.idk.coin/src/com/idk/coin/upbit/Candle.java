package com.idk.coin.upbit;

import java.math.BigDecimal;

import com.google.gson.JsonObject;

public class Candle {
	String market; //	마켓명	String
	String candle_date_time_utc; //	캔들 기준 시각(UTC 기준)	String
	String candle_date_time_kst; //	캔들 기준 시각(KST 기준)	String
	BigDecimal opening_price; //	시가	Double
	BigDecimal high_price; //	고가	Double
	BigDecimal low_price;	//저가	Double
	BigDecimal trade_price; //	종가	Double
	long timestamp; //	마지막 틱이 저장된 시각	Long
	BigDecimal candle_acc_trade_price; //	누적 거래 금액	Double
	BigDecimal candle_acc_trade_volume; //	누적 거래량	Double
	BigDecimal prev_closing_price; //	전일 종가(UTC 0시 기준)	Double
	BigDecimal change_price; //	전일 종가 대비 변화 금액	Double
	BigDecimal change_rate; //	전일 종가 대비 변화량	Double
	BigDecimal converted_trade_price; //	종가 환산 화폐 단위로 환산된 가격(요청에 convertingPriceUnit 파라미터 없을 시 해당 필드 포함되지 않음.)	Double

	public Candle(JsonObject obj) {
		setJsonObject(obj);
	}
	
	public void setJsonObject(JsonObject obj) {
		if(!obj.get("market").isJsonNull()) setMarket(obj.get("market").getAsString());
		if(!obj.get("candle_date_time_utc").isJsonNull()) setCandle_date_time_utc(obj.get("candle_date_time_utc").getAsString());
		if(!obj.get("candle_date_time_kst").isJsonNull()) setCandle_date_time_kst(obj.get("candle_date_time_kst").getAsString());
		if(!obj.get("opening_price").isJsonNull()) setOpening_price(obj.get("opening_price").getAsBigDecimal());
		if(!obj.get("high_price").isJsonNull()) setHigh_price(obj.get("high_price").getAsBigDecimal());
		if(!obj.get("low_price").isJsonNull()) setLow_price(obj.get("low_price").getAsBigDecimal());
		if(!obj.get("trade_price").isJsonNull()) setTrade_price(obj.get("trade_price").getAsBigDecimal());
		if(!obj.get("timestamp").isJsonNull()) setTimestamp(obj.get("timestamp").getAsLong());
		if(!obj.get("candle_acc_trade_price").isJsonNull()) setCandle_acc_trade_price(obj.get("candle_acc_trade_price").getAsBigDecimal());
		if(!obj.get("candle_acc_trade_volume").isJsonNull()) setCandle_acc_trade_volume(obj.get("candle_acc_trade_volume").getAsBigDecimal());
		//if(!obj.get("prev_closing_price").isJsonNull()) setPrev_closing_price(obj.get("prev_closing_price").getAsBigDecimal());
		//if(!obj.get("change_price").isJsonNull()) setChange_price(obj.get("change_price").getAsBigDecimal());
		//if(!obj.get("change_rate").isJsonNull()) setChange_rate(obj.get("change_rate").getAsBigDecimal());
		//if(!obj.get("converted_trade_price").isJsonNull()) setConverted_trade_price(obj.get("converted_trade_price").getAsBigDecimal());
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getCandle_date_time_utc() {
		return candle_date_time_utc;
	}

	public void setCandle_date_time_utc(String candle_date_time_utc) {
		this.candle_date_time_utc = candle_date_time_utc;
	}

	public String getCandle_date_time_kst() {
		return candle_date_time_kst;
	}

	public void setCandle_date_time_kst(String candle_date_time_kst) {
		this.candle_date_time_kst = candle_date_time_kst;
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getCandle_acc_trade_price() {
		return candle_acc_trade_price;
	}

	public void setCandle_acc_trade_price(BigDecimal candle_acc_trade_price) {
		this.candle_acc_trade_price = candle_acc_trade_price;
	}

	public BigDecimal getCandle_acc_trade_volume() {
		return candle_acc_trade_volume;
	}

	public void setCandle_acc_trade_volume(BigDecimal candle_acc_trade_volume) {
		this.candle_acc_trade_volume = candle_acc_trade_volume;
	}

	public BigDecimal getPrev_closing_price() {
		return prev_closing_price;
	}

	public void setPrev_closing_price(BigDecimal prev_closing_price) {
		this.prev_closing_price = prev_closing_price;
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

	public BigDecimal getConverted_trade_price() {
		return converted_trade_price;
	}

	public void setConverted_trade_price(BigDecimal converted_trade_price) {
		this.converted_trade_price = converted_trade_price;
	}

	@Override
	public String toString() {
		return "Candle [market=" + market + ", candle_date_time_utc=" + candle_date_time_utc + ", candle_date_time_kst="
				+ candle_date_time_kst + ", opening_price=" + opening_price + ", high_price=" + high_price
				+ ", low_price=" + low_price + ", trade_price=" + trade_price + ", timestamp=" + timestamp
				+ ", candle_acc_trade_price=" + candle_acc_trade_price + ", candle_acc_trade_volume="
				+ candle_acc_trade_volume + ", prev_closing_price=" + prev_closing_price + ", change_price="
				+ change_price + ", change_rate=" + change_rate + ", converted_trade_price=" + converted_trade_price
				+ "]";
	}


}
