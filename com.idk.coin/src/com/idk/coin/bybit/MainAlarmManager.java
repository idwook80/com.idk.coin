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
		try {
			setShortStopLoss();
			setShort();
			setShortTakeProfit();
			setLongTakeProfit();
			setLong();
			setLongStopLoss();
		}catch(Exception e) {
			e.printStackTrace();
			AlarmSound.playDistress();
		}
	}

	public void setShortStopLoss() throws Exception {
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
		shortStopLoss(21750, QTY3);
		shortStopLoss(21500, QTY3);
		shortStopLoss(21250, QTY3);
		shortStopLoss(21000, QTY3);
		
		createOpenShort(20710, OVER, 20910, QTY, 20810, RR);
		createOpenShort(20510, OVER, 20710, QTY, 20610, RR);
		createOpenShort(20310, OVER, 20510, QTY, 20410, RR);
		createOpenShort(20110, OVER, 20310, QTY, 20210, RR);
		createOpenShort(19910, OVER, 20110, QTY, 20010, RR);
		
		shortStopLoss(20750, QTY3);
		shortStopLoss(20500, QTY3);
		shortStopLoss(20250, QTY3);
		shortStopLoss(20000, QTY3);
		
		createOpenShort(19810, OVER, 20010, QTY, 19910, RR);
		createOpenShort(19710, OVER, 19910, QTY, 19810, RR);
		createOpenShort(19610, OVER, 19810, QTY, 19710, RR);
		createOpenShort(19510, OVER, 19710, QTY, 19610, RR);
		createOpenShort(19410, OVER, 19610, QTY, 19510, RR);
		shortStopLoss(19750, QTY2);
		
		createOpenShort(19310, OVER, 19510, QTY, 19410, RR);
		createOpenShort(19210, OVER, 19410, QTY, 19310, RR);
	}
	
/**	############################################### **/
/** ######### Short Order exists  Start ########### **/  //<[SHORT]<----------------------------
/**	############################################### **/
	public void setShort() throws Exception {
		
		//addCloseShort(19410, OVER, 19310, QTY, RR);
		addCloseShort(19310, OVER, 19260, QTY, RR);
		addCloseShort(19260, OVER, 19210, QTY, RR);
		addCloseShort(19210, OVER, 19160, QTY, RR);
		addCloseShort(19185, OVER, 19335, QTY, ONCE);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		addOpenShort(19110, UNDER, 19160, QTY, RR);
		addOpenShort(19060, UNDER, 19160, QTY, RR);
		addOpenShort(19010, UNDER, 19110, QTY, RR);
	}
/**	############################################### **/
/** ######### Short Order exists  End ############# **------------------------------------------/
/**	############################################### **/
	public void setShortTakeProfit() throws Exception {
		
		createCloseShort(19110, UNDER, 18910, QTY, 19010, RR);
		createCloseShort(19010, UNDER, 18810, QTY, 18910, RR);
		createCloseShort(18910, UNDER, 18710, QTY, 18810, RR);
		//createCloseShort(18810, UNDER, 18610, QTY, 18710, RR);
		
		createCloseShort(18710, UNDER, 18510, QTY, 18610, RR);
		createCloseShort(18510, UNDER, 18310, QTY, 18410, RR);
		createCloseShort(18310, UNDER, 18110, QTY, 18310, RR);
		
		createCloseShort(18110, UNDER, 17910, QTY, 18110, RR);
		createCloseShort(17910, UNDER, 17710, QTY, 17910, RR);
		createCloseShort(17710, UNDER, 17510, QTY, 17710, RR);
		createCloseShort(17510, UNDER, 17310, QTY, 17510, RR);
		createCloseShort(17310, UNDER, 17110, QTY, 17310, RR);
		
		createCloseShort(17110, UNDER, 16910, QTY, 17110, RR);
		createCloseShort(16910, UNDER, 16710, QTY, 16910, RR);
		createCloseShort(16710, UNDER, 16510, QTY, 16710, RR);
		createCloseShort(16510, UNDER, 16310, QTY, 16510, RR);
		createCloseShort(16310, UNDER, 16110, QTY, 16310, RR);
	}
	
	
/**   ############### LONG SETTINGS ####################  **/
	public void setLongTakeProfit() throws Exception {
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
		createCloseLong(19610, OVER, 19810, QTY, 19710, RR);
		createCloseLong(19410, OVER, 19610, QTY, 19510, RR);
		
		createCloseLong(19310, OVER, 19510, QTY, 19410, RR);
		createCloseLong(19210, OVER, 19410, QTY, 19310, RR);
	
	}
/**	#######################################[LONG]##### **/
/** ######### Long Order exists  Start ####[LONG]### **///<[LONG]<----------------------------
/**	#######################################[LONG]### **/
	public void setLong() throws Exception {
		//addOpenLong(19410, OVER, 19310, QTY, RR);
		addOpenLong(19310, OVER, 19210, QTY, RR);
		addOpenLong(19210, OVER, 19110, QTY, RR);
		
		/** ↑↑↑↑ -------  Price Line -------  ↓↓↓↓  **/
		//addCloseLong(19110, UNDER, 19210, QTY, RR);
		addCloseLong(19060, UNDER, 19110, QTY, RR);
		addCloseLong(19010, UNDER, 19060, QTY, RR);
	}
/**	############################################### **/
/** ######### Long Order exists  End ############## **---------------------------------------/
/**	############################################### **/
	
	public void setLongStopLoss() throws Exception {
		
		createOpenLong(19110, UNDER, 18910, QTY, 19010, RR);
		createOpenLong(19010, UNDER, 18810, QTY, 18910, RR);
		createOpenLong(18910, UNDER, 18710, QTY, 18810, RR);
		
		//longStopLoss(19000, QTY2);
		//longStopLoss(18750, QTY2);
		
		createOpenLong(18810, UNDER, 18610, QTY, 18710, RR);
		createOpenLong(18710, UNDER, 18510, QTY, 18610, RR);
		createOpenLong(18610, UNDER, 18410, QTY, 18510, RR);
		
		createOpenLong(18510, UNDER, 18310, QTY, 18410, RR);
		createOpenLong(18410, UNDER, 18210, QTY, 18310, RR);
		createOpenLong(18310, UNDER, 18110, QTY, 18210, RR);
		
		longStopLoss(18500, QTY2);
		longStopLoss(18250, QTY2);
		longStopLoss(18000, QTY3);
		
		createOpenLong(18210, UNDER, 18010, QTY, 18110, RR);
		createOpenLong(18010, UNDER, 17810, QTY, 17910, RR);
		createOpenLong(17810, UNDER, 17610, QTY, 17710, RR);
		createOpenLong(17610, UNDER, 17410, QTY, 17510, RR);
		createOpenLong(17410, UNDER, 17210, QTY, 17310, RR);
		createOpenLong(17210, UNDER, 17010, QTY, 17110, RR);
		longStopLoss(17750, QTY3);
		longStopLoss(17500, QTY3);
		longStopLoss(17250, QTY3);
		longStopLoss(17000, QTY3);
		
		createOpenLong(17010, UNDER, 16810, QTY, 16910, RR);
		createOpenLong(16810, UNDER, 16610, QTY, 16710, RR);
		createOpenLong(16610, UNDER, 16410, QTY, 16510, RR);
		createOpenLong(16410, UNDER, 16210, QTY, 16310, RR);
		createOpenLong(16210, UNDER, 16010, QTY, 16110, RR);
		longStopLoss(16750, QTY3);
		longStopLoss(16500, QTY3);
		longStopLoss(16250, QTY3);
		longStopLoss(16000, QTY3);
		
	}
	/**	############################################### **/
	/** ######### Long Order exists  End ############## **---------------------------------------/
	/**	############################################### **//**	############################################### **/
	/** ######### Long Order exists  End ############## **---------------------------------------/
	/**	############################################### **//**	############################################### **/
	/** ######### Long Order exists  End ############## **---------------------------------------/
	/**	############################################### **//**	############################################### **/
	/** ######### Long Order exists  End ############## **---------------------------------------/
	/**	############################################### **/
	
}