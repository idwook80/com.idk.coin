package com.idk.coin.bybit.db;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.idk.coin.db.DaoModel;
import com.idk.coin.db.upbit.CoinDBManager;
import com.idk.coin.upbit.Order;
import com.idk.coin.upbit.Orders;

public class BybitDao extends DaoModel{
	DecimalFormat df  = new DecimalFormat("##.00");
	
	public volatile static BybitDao instance;
	
	public synchronized  static BybitDao getInstace(){
		if(instance == null){
			synchronized(BybitDao.class){
				instance = new BybitDao();
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
	public BybitUser selectUser(String id, String pw) throws Exception{
		StringBuffer queryBuffer = new StringBuffer("");
		queryBuffer.append("SELECT * FROM bybit.user A WHERE A.id = '"+ id+"' AND A.PASSWORD = '"+ pw+"'");
		
		System.out.println(queryBuffer.toString());
		List arr = BybitDBManager.getInstance().selectList(queryBuffer.toString());
		
		for(int i=0; i<arr.size(); i++) {
			HashMap map = (HashMap)arr.get(i);
			BybitUser user = new BybitUser(map);
			return user;
		}
		return null;
	}
	
	public ArrayList<BybitUser>  selectUserList() throws Exception{
		StringBuffer queryBuffer = new StringBuffer("");
		queryBuffer.append("SELECT * FROM bybit.user A");
		
		System.out.println(queryBuffer.toString());
		List arr = BybitDBManager.getInstance().selectList(queryBuffer.toString());
		
		ArrayList<BybitUser> users = new ArrayList<BybitUser>();
		for(int i=0; i<arr.size(); i++) {
			HashMap map = (HashMap)arr.get(i);
			BybitUser user = new BybitUser(map);
			users.add(user);
		}
		return users;
	}
	
	public Orders select(String... state) throws Exception{
		/**
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
