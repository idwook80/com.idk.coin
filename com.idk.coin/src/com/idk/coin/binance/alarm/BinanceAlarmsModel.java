package com.idk.coin.binance.alarm;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.AlarmPrice;
import com.idk.coin.bybit.db.BybitDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.OrderExecution;
import com.idk.coin.model.AlarmManager;

abstract public class BinanceAlarmsModel extends AlarmManager{
	
	public BinanceAlarmsModel(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
		
	}
	public BinanceAlarmsModel(String symbol, String web_id,String web_pw) throws Exception{
		this(symbol , null);
		this.web_id = web_id;
		setDB(web_id, web_pw);
		
	}
	public BybitUser getUser() {
		return user;
	}
	public void setDB(String web_id, String web_pw) throws Exception{
		  try {
		    	user =  BybitDao.getInstace().selectUser(web_id, web_pw);
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
	}
 
	
	
	/* ###################    OPEN LONG   ##################### */
 
	public AlarmPrice addOpenLong(double trigger, boolean is_over, double price, double qty, boolean is_reverse)throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over, is_reverse);
		a.setOpenLongAction(price, qty);
		if(is_reverse && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < open_price [Long] for reverse"); 
			//addOpenLong(18910, OVER, 18930, QTY, RR); //18920 무한
		}
		addAlarm(a);
		return a;
	}
 
	public void makeOpenLong(double trigger, boolean is_over, double open_price, double qty, double close_price, boolean is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenLong(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new BinanceAlarmPrice(this, open_price, UNDER, is_repeat);
		closeAlarm.setCloseLongAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
	}
	
	
	
	/* ###################    CLOSE LONG   ##################### */
 
	public AlarmPrice addCloseLong(double trigger,boolean is_over,double price,double qty,boolean is_reverse)throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseLongAction(price, qty);
		if(is_reverse && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > close_price [Long] for reverse"); 
		}
		addAlarm(a);
		return a;
	}
	 
	public void makeCloseLong(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseLong(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new BinanceAlarmPrice(this, close_price, OVER, is_repeat); //trigger 
		openAlarm.setOpenLongAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
	}
	
	
	/* ###################    OPEN SHORT   ##################### */
	 
	public AlarmPrice addOpenShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse)throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over,is_reverse);
		a.setOpenShortAction(price, qty);
		if(is_reverse && is_over == UNDER) {
			if(trigger >= price) throw new Exception("[Warning] trigger > open_price [Short] for reverse");
		}
		addAlarm(a);
		return a;
	}
	 
	public void makeOpenShort(double trigger, boolean is_over, double open_price, double qty, double close_price,boolean is_repeat) throws Exception {
		AlarmPrice openAlarm = addOpenShort(trigger, is_over, open_price, qty, ONCE);
		AlarmPrice closeAlarm = new BinanceAlarmPrice(this, open_price, OVER, is_repeat);
		closeAlarm.setCloseShortAction(close_price, qty);
		openAlarm.setNextAlarm(closeAlarm);
	}
 
	
	
	/* ###################    CLOSE SHORT   ##################### */
	 
	public AlarmPrice addCloseShort(double trigger,boolean is_over,double price,double qty,boolean is_reverse) throws Exception {
		AlarmPrice a = new BinanceAlarmPrice(this, trigger, is_over,is_reverse);
		a.setCloseShortAction(price, qty);
		if(is_reverse && is_over == OVER) {
			if(trigger <= price) throw new Exception("[Warning] trigger < close_price [Short] for reverse");
			 //ex) addCloseShort(18910, OVER, 18930, QTY, RR); // 18920 무한 
		}
		addAlarm(a);
		return a;
	}
	public void makeCloseShort(double trigger, boolean is_over, double close_price, double qty, double open_price, boolean is_repeat) throws Exception {
		AlarmPrice closeAlarm = addCloseShort(trigger, is_over, close_price, qty, ONCE);
		AlarmPrice openAlarm = new BinanceAlarmPrice(this, close_price, UNDER, is_repeat);
		openAlarm.setOpenShortAction(open_price, qty);
		closeAlarm.setNextAlarm(openAlarm);
	}
	
	
	
}