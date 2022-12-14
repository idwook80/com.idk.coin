package com.idk.coin.db.upbit;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.idk.coin.db.CoinDBManager;
import com.idk.coin.upbit.Order;
import com.idk.coin.upbit.Orders;

public class OrderDao extends DaoModel{
	DecimalFormat df  = new DecimalFormat("##.00");
	
	public volatile static OrderDao instance;
	
	public synchronized  static OrderDao getInstace(){
		if(instance == null){
			synchronized(OrderDao.class){
				instance = new OrderDao();
			}
		}
		return instance;
	}
	 
	public int insert(Order order) throws Exception {
		CoinDBManager mgr = CoinDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		
		HashMap<String,Object> map =new HashMap<>();
		map.put("uuid", order.getUuid());
		map.put("side", order.getSide());
		map.put("ord_type", order.getOrd_type());
		map.put("price", df.format(order.getPrice().doubleValue()));
		map.put("state", order.getState());
		map.put("market", order.getMarket());
		map.put("volume", order.getVolume());
		map.put("created_at", 	Timestamp.valueOf(ZonedDateTime.parse(order.getCreated_at()).toLocalDateTime()));
		map.put("remaining_volume", order.getRemaining_volume());
		if(order.getReserved_fee() != null) map.put("reserved_fee", order.getReserved_fee());
		if(order.getRemaining_fee() != null) map.put("remaining_fee", order.getRemaining_fee());
		if(order.getPaid_fee() != null) map.put("paid_fee", order.getPaid_fee());
		if(order.getLocked() != null) map.put("locked", order.getLocked());
		if(order.getExecuted_volume() != null) map.put("executed_volume", order.getExecuted_volume());
		if(order.getTrades_count() != null) map.put("trades_count", order.getTrades_count());
		if(order.getPuuid() != null) map.put("puuid", order.getPuuid());
		if(order.getCancel_at() != null) map.put("cancel_at",Timestamp.valueOf(ZonedDateTime.parse(order.getCancel_at()).toLocalDateTime()));
		if(order.getGoal_price() != null) map.put("goal_price",df.format(order.getGoal_price().doubleValue()));
			
		queryBuffer.append("INSERT INTO upbit.order ");
		queryBuffer.append(getInsertQuery(map));
		
		return mgr.executeUpdate(queryBuffer.toString());
		
	}
	public int update(Order order) throws Exception{
		CoinDBManager mgr = CoinDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		
		HashMap<String,Object> map =new HashMap<>();
		//map.put("uuid", order.getUuid());
		//map.put("side", order.getSide());
		//map.put("ord_type", order.getOrd_type());
		//map.put("price", df.format(order.getPrice().doubleValue()));
		map.put("state", order.getState());
		//map.put("market", order.getMarket());
		map.put("volume", order.getVolume());
		//map.put("created_at", 	Timestamp.valueOf(ZonedDateTime.parse(order.getCreated_at()).toLocalDateTime()));
		map.put("remaining_volume", order.getRemaining_volume());
		if(order.getReserved_fee() != null) map.put("reserved_fee", order.getReserved_fee());
		if(order.getRemaining_fee() != null) map.put("remaining_fee", order.getRemaining_fee());
		if(order.getPaid_fee() != null) map.put("paid_fee", order.getPaid_fee());
		if(order.getLocked() != null) map.put("locked", order.getLocked());
		if(order.getExecuted_volume() != null) map.put("executed_volume", order.getExecuted_volume());
		if(order.getTrades_count() != null) map.put("trades_count", order.getTrades_count());
		if(order.getPuuid() != null) map.put("puuid", order.getPuuid());
		if(order.getCancel_at() != null) map.put("cancel_at",Timestamp.valueOf(ZonedDateTime.parse(order.getCancel_at()).toLocalDateTime()));
		//if(order.getGoal_price() != null) map.put("goal_price",df.format(order.getGoal_price().doubleValue()));
		
		queryBuffer.append("UPDATE upbit.order SET ");
		queryBuffer.append(getUpdateQuery(map));
		queryBuffer.append(" WHERE `uuid` = '"+order.getUuid() + "'");
		
		
		return mgr.executeUpdate(queryBuffer.toString());
	}
	
	public int delete(Order order) throws Exception {
		CoinDBManager mgr = CoinDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		
		queryBuffer.append("DELETE FROM upbit.order ");
		queryBuffer.append(" WHERE `uuid` = '"+order.getUuid() + "'");
		
		return mgr.executeUpdate(queryBuffer.toString());
	}
	
	public Orders select(String... state) throws Exception{
		/**
		String uuid; // ????????? ?????? ????????? String
		String side; // ?????? ?????? String
		String ord_type; // ?????? ?????? String
		BigDecimal price; // ?????? ?????? ?????? ?????? NumberString
		String state; // ?????? ?????? String
		String market; // ????????? ????????? String
		String created_at; // ?????? ?????? ?????? DateString
		BigDecimal volume; // ???????????? ????????? ?????? ??? NumberString
		BigDecimal remaining_volume; // ?????? ??? ?????? ?????? ??? NumberString
		BigDecimal reserved_fee; // ???????????? ????????? ?????? NumberString
		BigDecimal remaining_fee; // ?????? ????????? NumberString
		BigDecimal paid_fee; // ????????? ????????? NumberString
		BigDecimal locked; // ????????? ???????????? ?????? NumberString
		BigDecimal executed_volume; // ????????? ??? NumberString
		BigDecimal trades_count; // ?????? ????????? ?????? ?????? ??? Integer
		**/
		CoinDBManager mgr = CoinDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer("");
		queryBuffer.append("SELECT * FROM upbit.order A");
		if(state != null) {
			queryBuffer.append(" WHERE A.state = '"+state[0]+"'");
			if(state.length > 1) {
				for(int i=1; i<state.length; i++) {
					queryBuffer.append(" AND A.state = '"+state[i] +"'");
				}
			}
		}
		queryBuffer.append(" ORDER BY A.created_at DESC");
		
		System.out.println(queryBuffer.toString());
		List arr = mgr.selectList(queryBuffer.toString());
		
		Orders orders = new Orders();
		for(int i=0; i<arr.size(); i++) {
			HashMap map = (HashMap)arr.get(i);
			String uuid = (String) map.get("uuid");
			Order order = new Order(uuid);
			order.setHashMap(map);
			orders.add(order);
		}
		
		return orders;
	}
}
