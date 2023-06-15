package com.idk.coin.bybit.db;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.db.DaoModel;
import com.idk.coin.db.upbit.CoinDBManager;
import com.idk.coin.model.AlarmManager;
import com.idk.coin.model.TradeModel;
import com.idk.coin.upbit.Order;
import com.idk.coin.upbit.Orders;

public class BybitAlarmDao extends DaoModel{
	DecimalFormat df  = new DecimalFormat("##.00");
	
	public volatile static BybitAlarmDao instance;
	
	public synchronized  static BybitAlarmDao getInstace(){
		if(instance == null){
			synchronized(BybitAlarmDao.class){
				instance = new BybitAlarmDao();
			}
		}
		return instance;
	}
	 
	public int insert(AlarmPrice alarm) throws Exception {
		BybitDBManager mgr =BybitDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		AlarmManager amg 	= alarm.getManager();
		TradeModel 	tr 		= 	alarm.getTr();
		BybitUser 	user 		= amg.getUser();
		
		
		HashMap<String,Object> map =new HashMap<>();
		
		map.put("user_id",user.getUser_id());
		map.put("symbol", amg.getSymbol());
		map.put("trigger", alarm.getTrigger());
		map.put("is_over", alarm.is_over ? "Y" : "N");
		map.put("position", tr.getPosition_idx() );
		map.put("side", tr.setSide_idx());
		map.put("price", tr.getPrice());
		map.put("qty", tr.getQty());
		map.put("repeat", alarm.getRepeat());
		map.put("alarm_kind", alarm.getAlarm_kind());
		map.put("parent_alarm_id", alarm.getParent_alarm_id());
		map.put("msg", alarm.toActionString());
		
		if(alarm.getParent_order_id() != null) map.put("parent_order_id", alarm.getParent_order_id());
		if(alarm.getOrder_id() != null) map.put("order_id", alarm.getOrder_id());
		if(alarm.getNext() != null) map.put("next_id", alarm.getNext().getAlarm_id());
		
		
		queryBuffer.append("INSERT INTO bybit.alarm ");
		queryBuffer.append(getInsertQuery(map));
		return mgr.insertAndIndex(queryBuffer.toString());
		
	}
	public int update(AlarmPrice alarm) throws Exception{
		BybitDBManager mgr =BybitDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		AlarmManager amg 	= alarm.getManager();
		TradeModel 	tr 		= 	alarm.getTr();
		BybitUser 	user 		= amg.getUser();
		
		
		HashMap<String,Object> map =new HashMap<>();
		
		//map.put("user_id",user.getUser_id());
		//map.put("symbol", amg.getSymbol());
		map.put("trigger", alarm.getTrigger());
		map.put("is_over", alarm.is_over ? "Y" : "N");
		map.put("position", tr.getPosition_idx() );
		map.put("side", tr.setSide_idx());
		map.put("price", tr.getPrice());
		map.put("qty", tr.getQty());
		map.put("repeat", alarm.getRepeat());
		map.put("alarm_kind", alarm.getAlarm_kind());
		map.put("parent_alarm_id", alarm.getParent_alarm_id());
		
		map.put("msg", alarm.toActionString());
		 
		
		if(alarm.getParent_order_id() != null) map.put("parent_order_id", alarm.getParent_order_id());
		if(alarm.getOrder_id() != null) map.put("order_id", alarm.getOrder_id());
		if(alarm.getNext() != null) map.put("next_id", alarm.getNext().getAlarm_id());
		
		
		queryBuffer.append("UPDATE bybit.alarm SET ");
		queryBuffer.append(getUpdateQuery(map));
		queryBuffer.append(" WHERE `alarm_id` = '"+alarm.getAlarm_id() + "'");
		
		
		return mgr.executeUpdate(queryBuffer.toString());
	}
	
	public int delete(AlarmPrice alarm) throws Exception {
		BybitDBManager mgr =BybitDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		
		queryBuffer.append("DELETE FROM bybit.alarm ");
		queryBuffer.append(" WHERE `alarm_id` = '"+ alarm.getAlarm_id() + "'");
		
		return mgr.executeUpdate(queryBuffer.toString());
	}
	public int deleteAll(String user_id, String symbol)  throws Exception {
		BybitDBManager mgr =BybitDBManager.getInstance();
		StringBuffer queryBuffer = new StringBuffer();
		
		queryBuffer.append("DELETE FROM bybit.alarm ");
		queryBuffer.append(" WHERE user_id = '"+user_id + "' AND symbol = '" +  symbol + "'");
		
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
