package com.idk.coin.upbit;

import java.math.BigDecimal;
import java.util.*;

import com.google.gson.JsonObject;

public class Order {
	public final static String STATE_CANCEL 	= "cancel";
	public final static String STATE_WAIT   	= "wait";
	public final static String STATE_WATCH		= "watch";
	public final static String STATE_DONE		= "done";
	
	public final static String SIDE_BID			= "bid";
	public final static String SIDE_ASK			= "ask";
	
	public final static String ORD_TYPE_LIMIT	= "limit";
	public final static String ORD_TYPE_MARKET  = "market";
	public final static String ORD_TYPE_PRICE 	= "price";
	
	String uuid; // 주문의 고유 아이디 String
	String side; // 주문 종류 String
	String ord_type; // 주문 방식 String
	BigDecimal price; // 주문 당시 화폐 가격 NumberString
	String state; // 주문 상태 String
	String market; // 마켓의 유일키 String
	String created_at; // 주문 생성 시간 DateString
	BigDecimal volume; // 사용자가 입력한 주문 양 NumberString
	BigDecimal remaining_volume; // 체결 후 남은 주문 양 NumberString
	BigDecimal reserved_fee; // 수수료로 예약된 비용 NumberString
	BigDecimal remaining_fee; // 남은 수수료 NumberString
	BigDecimal paid_fee; // 사용된 수수료 NumberString
	BigDecimal locked; // 거래에 사용중인 비용 NumberString
	BigDecimal executed_volume; // 체결된 양 NumberString
	BigDecimal trades_count; // 해당 주문에 걸린 체결 수 Integer
	ArrayList<Object> trades; // 체결 Array[Object]
	
	String cancel_at;
	BigDecimal goal_price;
	String puuid;
	
	public Order(String uuid) {
		setUuid(uuid);
	}
	public void setJsonObject(JsonObject obj) {
		if(!obj.get("side").isJsonNull()) setSide(obj.get("side").getAsString());
		if(!obj.get("ord_type").isJsonNull()) setOrd_type(obj.get("ord_type").getAsString());
		if(!obj.get("price").isJsonNull()) setPrice(obj.get("price").getAsBigDecimal());
		if(!obj.get("state").isJsonNull()) setState(obj.get("state").getAsString());
		if(!obj.get("market").isJsonNull()) setMarket(obj.get("market").getAsString());
		if(!obj.get("created_at").isJsonNull()) setCreated_at(obj.get("created_at").getAsString());
		if(!obj.get("volume").isJsonNull()) setVolume(obj.get("volume").getAsBigDecimal());
		if(!obj.get("remaining_fee").isJsonNull()) setRemaining_fee(obj.get("remaining_fee").getAsBigDecimal());
		if(!obj.get("remaining_volume").isJsonNull()) setRemaining_volume(obj.get("remaining_volume").getAsBigDecimal());
		if(!obj.get("paid_fee").isJsonNull()) setPaid_fee(obj.get("paid_fee").getAsBigDecimal());
		if(!obj.get("locked").isJsonNull()) setLocked(obj.get("locked").getAsBigDecimal());
		if(!obj.get("executed_volume").isJsonNull()) setExecuted_volume(obj.get("executed_volume").getAsBigDecimal());
		if(!obj.get("trades_count").isJsonNull()) setTrades_count(obj.get("trades_count").getAsBigDecimal());
		//setTrades( );
	}
	public void setHashMap(HashMap map) {
		if(map.get("side") != null) setSide(map.get("side").toString());
		if(map.get("ord_type") != null) setOrd_type(map.get("ord_type").toString());
		if(map.get("price") != null) setPrice(new BigDecimal(map.get("price").toString()));
		if(map.get("state") != null) setState(map.get("state").toString());
		if(map.get("market") != null) setMarket(map.get("market").toString());
		if(map.get("created_at") != null) setCreated_at(map.get("created_at").toString());
		if(map.get("volume") != null) setVolume(new BigDecimal(map.get("volume").toString()));
		if(map.get("remaining_fee") != null) setRemaining_fee(new BigDecimal(map.get("remaining_fee").toString()));
		if(map.get("remaining_volume") != null) setRemaining_volume(new BigDecimal(map.get("remaining_volume").toString()));
		if(map.get("paid_fee") != null) setPaid_fee(new BigDecimal(map.get("paid_fee").toString()));
		if(map.get("locked") != null) setLocked(new BigDecimal(map.get("locked").toString()));
		if(map.get("executed_volume") != null) setExecuted_volume(new BigDecimal(map.get("executed_volume").toString()));
		if(map.get("trades_count") != null) setTrades_count(new BigDecimal(map.get("trades_count").toString()));
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getOrd_type() {
		return ord_type;
	}
	public void setOrd_type(String ord_type) {
		this.ord_type = ord_type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getRemaining_volume() {
		return remaining_volume;
	}
	public void setRemaining_volume(BigDecimal remaining_volume) {
		this.remaining_volume = remaining_volume;
	}
	public BigDecimal getReserved_fee() {
		return reserved_fee;
	}
	public void setReserved_fee(BigDecimal reserved_fee) {
		this.reserved_fee = reserved_fee;
	}
	public BigDecimal getRemaining_fee() {
		return remaining_fee;
	}
	public void setRemaining_fee(BigDecimal remaining_fee) {
		this.remaining_fee = remaining_fee;
	}
	public BigDecimal getPaid_fee() {
		return paid_fee;
	}
	public void setPaid_fee(BigDecimal paid_fee) {
		this.paid_fee = paid_fee;
	}
	public BigDecimal getLocked() {
		return locked;
	}
	public void setLocked(BigDecimal locked) {
		this.locked = locked;
	}
	public BigDecimal getExecuted_volume() {
		return executed_volume;
	}
	public void setExecuted_volume(BigDecimal executed_volume) {
		this.executed_volume = executed_volume;
	}
	public BigDecimal getTrades_count() {
		return trades_count;
	}
	public void setTrades_count(BigDecimal trades_count) {
		this.trades_count = trades_count;
	}
	public ArrayList<Object> getTrades() {
		return trades;
	}
	public void setTrades(ArrayList<Object> trades) {
		this.trades = trades;
	}
	@Override
	public String toString() {
		return "Order [uuid=" + uuid + ", side=" + side + ", ord_type=" + ord_type + ", price=" + price + ", state="
				+ state + ", market=" + market + ", created_at=" + created_at + ", volume=" + volume
				+ ", remaining_volume=" + remaining_volume + ", reserved_fee=" + reserved_fee + ", remaining_fee="
				+ remaining_fee + ", paid_fee=" + paid_fee + ", locked=" + locked + ", executed_volume="
				+ executed_volume + ", trades_count=" + trades_count + ", trades=" + trades + ", cancel_at=" + cancel_at +"]";
	}
	
	
	public String getCancel_at() {
		return cancel_at;
	}
	public void setCancel_at(String cancel_at) {
		this.cancel_at = cancel_at;
	}
	public BigDecimal getGoal_price() {
		return goal_price;
	}
	public void setGoal_price(BigDecimal goal_price) {
		this.goal_price = goal_price;
	}
	public String getPuuid() {
		return puuid;
	}
	public void setPuuid(String puuid) {
		this.puuid = puuid;
	}
	
	
	
	
}
