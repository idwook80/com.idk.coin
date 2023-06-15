package com.idk.coin.bybit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.AlarmSound;
import com.idk.coin.bybit.db.BybitAlarmDao;
import com.idk.coin.model.AlarmManager;

public class BybitAlarmPrice extends AlarmPrice {
	public static Logger LOG =   LoggerFactory.getLogger(AlarmPrice.class.getName());
 
	public BybitAlarmPrice(AlarmManager manager, double trigger, boolean is_over,int is_reverse) {
		super(manager, trigger, is_over, is_reverse);
	}
	public synchronized void action(String api_key, String api_secret, String symbol) throws Exception {
		//LOG.info("##\t 현재가 : [" + trigger + "] " + ( is_over ? "(↑)이상" : "(↓)이하" )  + " 알림이 확인되었습니다.\t## pid : " + this.getParent_order_id());
		if(tr != null) {
			com.idk.coin.bybit.model.Order order  =
					(com.idk.coin.bybit.model.Order) tr.executAction(api_key, api_secret, symbol);
			
			int last = getRepeat();
			String last_order =  toActionString();
			if(isReverse()) {
				//홀수 자기자신 짝수는 반대가 끝
				last_order = last%2 == 0 ? tr.getReverseActionString() :  toActionString();
			}
			
			descreseRepeat();
			
		
			if(order != null) {
				LOG.info("주문실행: " + tr.getPrice() + " , " + tr.getQty() + " "+tr.getActionString()+ " 주문 되었습니다.");
				this.setOrder_id(order.getOrder_id());
				//if(isReverse()) LOG.info("리버스 & 반복 알람예약을 생성합니다. !") ;
				//else LOG.info("반복&리버스 ("+getRepeat()+ ")회 남았습니다.");
				//LOG.info("리버스&반복 ("+getRepeat()+ ")회 남았습니다.");
			}
			if(order != null && isReverse() && next == null) {
				AlarmSound.playAlert();
				LOG.info("리버스 &반복:(새로운)알람을  생성합니다. !") ;
				String position 	= tr.getPosition_idx();
				String side			= tr.getSide();
				AlarmPrice newAlarm = null;
				
				if(position.equals(BybitTrade.POSITION_IDX_LONG)) {		// Long
					if(side.equals(BybitTrade.SIDE_BUY)) {				// Open Long  --> Close Long
						newAlarm = manager.addCloseLong(tr.getPrice(), !is_over, trigger, tr.getQty(),getRepeat());
					}else if(side.equals(BybitTrade.SIDE_SELL)) {		// Close Long  --> Open Long
						newAlarm = manager.addOpenLong(tr.getPrice(), !is_over, trigger, tr.getQty(),getRepeat());
					}
				}else if(position.equals(BybitTrade.POSITION_IDX_SHORT))	{ // Short 
					if(side.equals(BybitTrade.SIDE_BUY)) {				  // Close Short --> Open Short
						newAlarm = manager.addOpenShort(tr.getPrice(), !is_over, trigger, tr.getQty(),getRepeat());
					}else if(side.equals(BybitTrade.SIDE_SELL)) {		 // Open Short ---> Close Short
						newAlarm = manager.addCloseShort(tr.getPrice(), !is_over, trigger, tr.getQty(),getRepeat());
					}
				}
				newAlarm.setParent(this);
				newAlarm.setParent_alarm_id("0");
				newAlarm.setParent_order_id(getOrder_id());
				
				int id = insertDatabase(newAlarm);
				if(id > 0) newAlarm.setAlarm_id(String.valueOf(id));
				
				//홀수 자기자신 짝수는 반대가 끝
				last_order = newAlarm.getRepeat()%2 == 0 ? newAlarm.getParent().toActionString() : newAlarm.toActionString();
				//LOG.info(newAlarm.toString());
			}else {
				if(order != null) {
					AlarmSound.playAlert();
					if(next != null) {
						LOG.info("리버스 &반복 :(자식)알람을 가져옵니다. !") ;
						next.setParent(this);
						next.setParent_alarm_id("0");
						next.setParent_order_id(getOrder_id());
						//next.setRepeat(getRepeat());
						manager.addAlarm(next);
						updateDatabase(next);
						//LOG.info(next.toString());
						//홀수 자기자신 짝수는 반대가 끝
						last_order = next.getRepeat()%2 == 0 ? next.getParent().toActionString() : next.toActionString();
					}
				}
				else {
					AlarmSound.playDistress();
					LOG.error("주문중 에러가 있습니다.");
					LOG.error(tr.toString());
				}
			}
			
			this.setParent_alarm_id("-1");
			deleteDatabase(this);
			
			if(isReverse())LOG.info("리버스&반복 유지: ("+getRepeat()+ ")회 남았습니다. 마지막 주문 : "+last_order );
			else {
				if(next == null) LOG.info("리버스&반복 종료: ("+getRepeat()+ ")회로 현재주문이 체결되면 더이상 리버스&반복 하지 않습니다. 마지막주문" +last_order);
				else {
					LOG.info("리버스&반복 유지: ("+next.getRepeat()+ ")회 남았습니다. 마지막 주문 : "+last_order );
				}
			}
			
			LOG.info("##########################[알람처리 완료]###################################");
			LOG.info("");
		}
	}
	public void deleteDatabase(AlarmPrice alarm) {
		try {
			BybitAlarmDao.getInstace().delete(alarm);
		}catch(Exception e) {
			
		}
	}
	public int insertDatabase(AlarmPrice alarm) {
		try {
			return BybitAlarmDao.getInstace().insert(alarm);
		}catch(Exception e) {
			
		}
		return 0;
	}
	public void updateDatabase(AlarmPrice alarm	) {
		try {
			BybitAlarmDao.getInstace().update(alarm);
		}catch(Exception e) {
			
		}
	}
	
	public void setOpenLongAction(double price, double qty) {
		tr = new BybitTrade();
		tr.openLong(price, qty);
	}
	public void setOpenShortAction(double price, double qty) {
		tr = new BybitTrade();
		tr.openShort(price, qty);
	}
	public void setCloseLongAction(double price, double qty) {
		tr = new BybitTrade();
		tr.closeLong(price, qty);
	}
	public void setCloseShortAction(double price,double qty) {
		tr = new BybitTrade();
		tr.closeShort(price, qty);
	}
 
 
	@Override
	public String toAlarm() {
		return "["+trigger + ", " + (is_over ? " ↑이상" : " ↓이하") + "]";
	}
	@Override
	public String toActionString() {
		return "["+ tr.getPrice() + ", "+tr.qty+ "주][" + tr.getActionString()+"]";
	}
	
	@Override
	public String toString() {
		if(parent_order_id == null) {
			return "["+getAlarm_id()+"]["+ manager.getUser().getId() + ", "+manager.getSymbol()+"] 알람정보 : 가격 [" + trigger + "," + (is_over ? " ↑이상" : " ↓이하") +"]감시\t->["
					+ tr.getPrice() + " , " + tr.getQty()  + "주][" + tr.getActionString() + "]주문 실행합니다. "+"[반복&리버스 ("+ repeat + ")회]\t" 
					+ " " + (next != null ?  "다음 알림설정 -> " + next.toString() : "")
					+ " pid : " +parent_order_id;
		}else {
		
			return "["+getAlarm_id()+"]["+  manager.getUser().getId() + ", "+manager.getSymbol()+"] 알람정보 : 체결 " + parent.toActionString()+ "감시\t->["
					+ tr.getPrice() + " , " + tr.getQty()  + "주][" + tr.getActionString() + "]주문 실행합니다. "+"[반복&리버스 ("+ repeat + ")회]\t" 
					+ " " + (next != null ?  "다음 알림설정 -> " + next.toString() : "")
					+ " pid : " +parent_order_id;
		}

		/** "↑↑" : "↓↓"
		return "["+ manager.getUser().getId() + "] Alarm : [" + trigger + "," + (is_over ? "Over" : "Under") +"],["+ tr.getPrice() + " , " + tr.getQty()  + "],[" + tr.getActionString() + "] "+"[반복:"+ repeat + "]" 
				+ " " + (next != null ?  " Next Alarm -> " + next.toString() : "")
				+ " pid : " +parent_order_id; **/
 		
	}
	
}