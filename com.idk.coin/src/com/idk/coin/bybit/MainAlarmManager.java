package com.idk.coin.bybit;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.AlarmSound;
import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.account.OrderRest;
import com.idk.coin.bybit.model.AlarmManagerModel;

public class MainAlarmManager extends AlarmManagerModel {
	public static Logger LOG =   LoggerFactory.getLogger(MainAlarmManager.class.getName());
	 
	public MainAlarmManager(BybitMain main){
		super(main);
		LOG.info("Start Bybit Alarm System!!");
		LOG.info("Main Alarm Manager!!");
		init();
		start();
	}
	public void init() {
		userSet();
		setAlarm();
	}
	public void userSet() {
		DEFAULT_QTY     = new BigDecimal("0.15");
	    QTY				= DEFAULT_QTY.doubleValue();
	    QTY1			= new BigDecimal("0.2").doubleValue();
		QTY2  			= DEFAULT_QTY.multiply(new BigDecimal("2")).doubleValue();
		QTY3			= DEFAULT_QTY.multiply(new BigDecimal("3")).doubleValue();
		QTY4			= DEFAULT_QTY.multiply(new BigDecimal("4")).doubleValue();
		QTY5			= DEFAULT_QTY.multiply(new BigDecimal("5")).doubleValue();
		LOSS_TRIGGER_QTY	= 0.001;
	}
	
	public void start() {}
	public void setAlarm() {
		setShortStopLoss();
		setShort();
		setShortTakeProfit();
		setLongTakeProfit();
		setLong();
		setLongStopLoss();
	}

	public void setShortStopLoss() {
		shortStopLoss(23000, QTY2);
		createOpenShort(22710, OVER, 22910, QTY1, 22810, RR);
		createOpenShort(22510, OVER, 22710, QTY1, 22610, RR);
		createOpenShort(22310, OVER, 22510, QTY1, 22410, RR);
		createOpenShort(22110, OVER, 22310, QTY1, 22210, RR);
		createOpenShort(21910, OVER, 22110, QTY1, 22010, RR);
		shortStopLoss(22750, QTY2);
		shortStopLoss(22500, QTY2);
		shortStopLoss(22250, QTY2);
		shortStopLoss(22000, QTY2);
		
		createOpenShort(21710, OVER, 21910, QTY1, 21810, RR);
		createOpenShort(21510, OVER, 21710, QTY1, 21610, RR);
		createOpenShort(21310, OVER, 21510, QTY1, 21410, RR);
		createOpenShort(21110, OVER, 21310, QTY1, 21210, RR);
		createOpenShort(20910, OVER, 21110, QTY1, 21010, RR);
		shortStopLoss(21750, QTY2);
		shortStopLoss(21500, QTY2);
		shortStopLoss(21250, QTY2);
		shortStopLoss(21000, QTY2);
		
		createOpenShort(20710, OVER, 20910, QTY1, 20810, RR);
		createOpenShort(20510, OVER, 20710, QTY1, 20610, RR);
		createOpenShort(20310, OVER, 20510, QTY1, 20410, RR);
		createOpenShort(20110, OVER, 20310, QTY1, 20210, RR);
		createOpenShort(19910, OVER, 20110, QTY1, 20010, RR);
		shortStopLoss(20750, QTY2);
		shortStopLoss(20500, QTY2);
		shortStopLoss(20250, QTY2);
		
		createOpenShort(19810, OVER, 20010, QTY1, 19910, RR);
		createOpenShort(19710, OVER, 19910, QTY1, 19810, RR);
		createOpenShort(19610, OVER, 19810, QTY, 19710, RR);
		createOpenShort(19510, OVER, 19710, QTY, 19660, RR);
		createOpenShort(19510, OVER, 19660, QTY, 19610, RR);
	}
	
	public static boolean is_main_account = true;
	
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<<<<<<----------------------------
/**	############################################### **/
	public void setShort() {
		
		addCloseShort(19610, OVER, 19560, QTY, RR);
		addCloseShort(19560, OVER, 19510, QTY, RR);
		
		addCloseShort(19510, OVER, 19460, QTY, RR);
		addCloseShort(19460, OVER, 19410, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		addOpenShort(19310, UNDER, 19410, QTY, RR);
		addOpenShort(19110, UNDER, 19210, QTY, RR);
		
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void setShortTakeProfit() {
		
		createCloseShort(19210, UNDER, 19010, QTY, 19110, RR);
		
		addOpenShort(18960, UNDER, 18960, QTY);
		createCloseShort(19010, UNDER, 18910, QTY, 19010, RR);
		createCloseShort(18910, UNDER, 18710, QTY, 18910, RR);
		createCloseShort(18710, UNDER, 18510, QTY, 18710, RR);
		createCloseShort(18510, UNDER, 18310, QTY, 18510, RR);
		createCloseShort(18310, UNDER, 18110, QTY, 18310, RR);
		
		createCloseShort(18110, UNDER, 17910, QTY, 18110, RR);
		createCloseShort(17910, UNDER, 17710, QTY, 17910, RR);
		createCloseShort(17710, UNDER, 17510, QTY, 17710, RR);
		createCloseShort(17510, UNDER, 17310, QTY, 17510, RR);
		createCloseShort(17310, UNDER, 17110, QTY, 17310, RR);
		
	}
	
/**   ############### LONG SETTINGS ####################  **/
	public void setLongTakeProfit() {
		createCloseLong(22810, OVER, 23010, QTY, 22910, RR);
		createCloseLong(22610, OVER, 22810, QTY, 22710, RR);
		createCloseLong(22410, OVER, 22610, QTY, 22510, RR);
		createCloseLong(22210, OVER, 22410, QTY, 22310, RR);
		createCloseLong(22010, OVER, 22210, QTY, 22110, RR);
		
		createCloseLong(21810, OVER, 22010, QTY, 21910, RR);
		createCloseLong(21610, OVER, 21810, QTY, 21710, RR);
		createCloseLong(21410, OVER, 21610, QTY, 21510, RR);
		createCloseLong(21210, OVER, 21410, QTY, 21310, RR);
		createCloseLong(21010, OVER, 21210, QTY, 21110, RR);
		
		createCloseLong(20810, OVER, 21010, QTY, 20910, RR);
		createCloseLong(20610, OVER, 20810, QTY, 20710, RR);
		createCloseLong(20410, OVER, 20610, QTY, 20510, RR);
		createCloseLong(20210, OVER, 20410, QTY, 20310, RR);
		createCloseLong(20010, OVER, 20210, QTY, 20110, RR);
		
		createCloseLong(19810, OVER, 20010, QTY, 19910, RR);
		createCloseLong(19710, OVER, 19910, QTY, 19810, RR);
		createCloseLong(19610, OVER, 19810, QTY, 19710, RR);
		createCloseLong(19510, OVER, 19710, QTY, 19610, RR);
		
		
		
	}
	
/**	############################################### **/
/** ######### Long Order exists  Start ############ **///<<<<<<----------------------------
/**	############################################### **/
	public void setLong() {
		
		addOpenLong(19610, OVER, 19510, QTY, RR);
		addOpenLong(19510, OVER, 19410, QTY, RR);
		addOpenLong(19460, OVER, 19410, QTY, RR);
		addOpenLong(19435, OVER, 19385, QTY, RR);
		//addOpenLong(19410, OVER, 19360, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		
		addCloseLong(19360, UNDER, 19410, QTY, RR);
		addCloseLong(19335, UNDER, 19385, QTY, RR);
		addCloseLong(19310, UNDER, 19360, QTY, RR);
		addCloseLong(19285, UNDER, 19335, QTY, RR);
		addCloseLong(19260, UNDER, 19310, QTY, RR);
		addCloseLong(19235, UNDER, 19285, QTY, RR);
		addCloseLong(19210, UNDER, 19260, QTY, RR);
		addCloseLong(19160, UNDER, 19210, QTY, RR);
		addCloseLong(19110, UNDER, 19210, QTY1, RR);
		
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void setLongStopLoss() {
		
		//longStopLoss(19250, QTY2);
		//createOpenLong(19310, UNDER, 19110, QTY1, 19210, RR);
		createOpenLong(19210, UNDER, 19010, QTY1, 19110, RR);
		longStopLoss(19000, QTY2);
		
		createOpenLong(19010, UNDER, 18810, QTY1, 18910, RR);
		createOpenLong(18810, UNDER, 18610, QTY1, 18710, RR);
		createOpenLong(18610, UNDER, 18410, QTY1, 18510, RR);
		createOpenLong(18410, UNDER, 18210, QTY1, 18310, RR);
		createOpenLong(18210, UNDER, 18010, QTY1, 18110, RR);
		longStopLoss(18750, QTY2);
		longStopLoss(18500, QTY2);
		longStopLoss(18250, QTY2);
		longStopLoss(18000, QTY2);
		
		createOpenLong(18010, UNDER, 17810, QTY1, 17910, RR);
		createOpenLong(17810, UNDER, 17610, QTY1, 17710, RR);
		createOpenLong(17610, UNDER, 17410, QTY1, 17510, RR);
		createOpenLong(17410, UNDER, 17210, QTY1, 17310, RR);
		createOpenLong(17210, UNDER, 17010, QTY1, 17110, RR);
		longStopLoss(17750, QTY2);
		longStopLoss(17500, QTY2);
		longStopLoss(17250, QTY2);
		longStopLoss(17000, QTY2);
		
	}
	
}