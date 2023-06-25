package com.idk.coin.bybit.alram;

import java.util.ArrayList;

import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.db.BybitAlarmDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class AlarmManager01 extends BybitAlarmsModel {
	
	public AlarmManager01(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager01(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty 		= user.getDefault_qty();
		if(default_qty == null) default_qty = "0.001";
		setDefault_qty(Double.valueOf(default_qty));
		LOSS_TRIGGER_QTY			= 0.001;
	    MIN_PROFIT					= 50;
	  
	}
	public void alarmSet() throws Exception{
		LOG.info("Alarm Set");
		boolean is_debug  = DEBUG_OFF;
		
		clearAllAlarms();
		if(is_debug) {
			currentStatus(DEBUG_ON);
			enableDatabase(DISABLE);
		}else {
			cancelAllOrder();
			currentStatus(DEBUG_OFF);
			enableDatabase(ENABLE);
		}
		
	}
	public static int IDLE_TIME = 10;      // 10분마다
	public int idle_check_time = 0; //min
	public static int RESET_TIME = 60 * 1; //reset 4시간마다
	public int reset_check_time = RESET_TIME; //min
	public void run() {
		try {
			Thread.sleep(1000*3);
			alarmSet();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		while(is_run) {
			try {
				
				LOG.info("["+user.getId()+"] Actives:"+this.getSize() + ",Idles:"+idles.size()
						+",설정가 : [" + calculator.price+"]" +"현재가 : " + getCurrentPrice()
						+" 알람 리셋 : " + reset_check_time + "/"+RESET_TIME+"분, 알랑 활성체크: " 
						+ idle_check_time + "/"+IDLE_TIME+"분");
				checkBalance();
				Thread.sleep(1000* 60 * 1);
				if(reset_check_time-- < 0) {
					alarmSet();
					idle_check_time = -1;
				}
				if(idle_check_time-- < 0) {
					checkAlarmIdles(5);
					idle_check_time = IDLE_TIME;
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void checkBalance() throws Exception{
		if(startCalculateModel != null) {
			Balance balance 	   	=  WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
			double oldEquity	= startCalculateModel.balance.getEquity();
			double newEquity 	= balance.getEquity();
			double profit	 	= oldEquity == 0 ? 0 : newEquity - oldEquity;
			double per		 	= profit == 0 ? 0 : profit / (oldEquity/100);
			LOG.info("Balance : " + newEquity + " - " + oldEquity + "  =  " + profit + "  ( "+ per+" %)" );
		}
	}
	CalculatePositionV3 startCalculateModel;
	CalculatePositionV3 calculator;
	double startCalculateEquity	 = 0.0;
	int take_1		= 3;
	int take_2		= 5;
	public void currentStatus(boolean debug) throws Exception{
		double price = getCurrentPrice();
		int count = 5;
		while(price < 0	) {
			price = getCurrentPrice();
			thread.sleep(1000 * 1);
			System.out.println("wait");
			if(count-- < 0) break;
		}
		
		if(price > 0) {
			ArrayList<Position> ps	=  PositionRest.getActiveMyPosition(user.getApi_key(),user.getApi_secret(), symbol);
			Balance balance 	   	=  WalletRest.getWalletBalance(user.getApi_key(),user.getApi_secret(), "USDT");
			Position buy			=  Position.getPosition(ps, symbol, "Buy");
			Position sell 			=  Position.getPosition(ps, symbol, "Sell");
			
			double oldEquity = startCalculateModel != null ? startCalculateModel.balance.getEquity() : 0.0;
			double newEquity = balance.getEquity();
			double profit	 = oldEquity == 0 ? 0 : newEquity - oldEquity;
			double per		 = profit == 0 ? 0 : profit / (oldEquity/100);
		
			/*if(per > take_2) {								//all take profit 매시간 검사
				if(clearProfit(price, balance, buy, sell, oldEquity,newEquity,profit, take_2)) return;
			}else if(per > take_1) { 						//half take profit message_count/4 4시간 검사
				if(message_count % 3 == 0)  {
					if(clearProfit(price, balance, buy, sell,oldEquity,newEquity, profit, take_1)) return;
				}
			}
			*/
			
			calculator = new CalculatePositionV3(this,price, buy, sell, balance, QTY, debug);
			calculator.setSizeValue(50, 20, 20);
			calculator.calculateStatus();
			LOG.debug(calculator.toAlarmString());
			insertAlarmMessageToDatabase(calculator);
			if(startCalculateModel == null)  	{
				startCalculateModel = calculator;
				if(startCalculateEquity > 0) startCalculateModel.balance.setEquity(startCalculateEquity);
			}
			
			reset_check_time = RESET_TIME;
		}else {
			LOG.info("아직 price 를 가져오지 못했슨니다." +getCurrentPrice() );
		}
	}
	public int message_count = 1;
	public void insertAlarmMessageToDatabase(CalculatePositionV3 cal) throws Exception{
		if(!db_enable) return;
		double oldEquity = startCalculateModel != null ? startCalculateModel.balance.getEquity() : startCalculateEquity;
		double newEquity = cal.balance.getEquity();
		double profit	 = oldEquity == 0 ? 0 : newEquity - oldEquity;
		double per		 = profit == 0 ? 0 : profit / (oldEquity/100);
	
		BybitAlarmDao.getInstace().insertMessage(user.getId(), user.getUser_id(), getSymbol(),
				oldEquity,newEquity,profit,per,message_count++,cal.toAlarmString());
	
	}
	
	
	public boolean clearProfit(double price,Balance balance,Position buy, Position sell ,double oldEquity, double newEquity, 
						double profit, int per)throws Exception {
		int min_open 			= calculator.MIN_OPEN_SIZE;
		int long_size 			= (int) (buy.getSize()/QTY);
		int short_size 			= (int) (buy.getSize()/QTY);
		
		if(min_open > long_size || min_open > short_size) return false;
		
		int short_close_size	= per == take_1 ? ((short_size - min_open)/2) : (short_size - min_open);
		int long_close_size		= per == take_1 ? ((long_size - min_open)/2) : (long_size - min_open);
		
		double default_price 	= getDefaultPrice(price);
		
		double close_long_size	= long_close_size * QTY;
		double close_short_size = short_close_size * QTY;
		
		double long_trigger		= default_price-50;
		double short_trigger	= default_price+50;
		
		closeLong(long_trigger, OVER, long_trigger, close_long_size,ONCE);
		closeShort(short_trigger, UNDER, short_trigger, close_short_size, ONCE);
		
		StringBuffer msg = new StringBuffer();
		msg.append("Take Profit : "+profit+"("+per+"%)");
		msg.append("closeLong("+long_trigger+", OVER, "+long_trigger+", "+close_long_size+",ONCE)");
		msg.append("closeShort("+short_trigger+", UNDER, "+short_trigger+", "+close_short_size+",ONCE)");
		
		startCalculateModel = calculator;
		reset_check_time = 1;
		message_count = 0;
		BybitAlarmDao.getInstace().insertMessage(user.getId(), user.getUser_id(), getSymbol(),
				oldEquity, newEquity, profit, per, message_count++, msg.toString());
		return true;
	}
	 
	public double getDefaultPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		double d = Math.round((c_price -m -h)/ 10) * 10;
		return m + h + d;
	}
	
/**
 * #################   [SHORT] #############
 * @throws Exception
 */
	public void startMakeOpenShort(double start,double end,double limit, boolean isLoss) throws Exception{
		double profitLimit = 100;
		double alarmLimit  = 150;
		
		for(double i=start; i<=end; i+=limit) {
			double open = i;
			double close  = i - profitLimit;
			double alarm = i-alarmLimit;
			makeOpenShort(alarm, OVER, open, QTY, close, RR);
		}
		if(isLoss) {
			//shortStopLoss(end-profitLimit   , QTY2);
			//shortStopLoss(start+profitLimit , QTY2);
		}
		
	}
	public void createOpenShort()  throws Exception{
		//makeOpenShort(26610, OVER, 26810, QTY, 26610, RR);
		startMakeOpenShort(32110, 32910, 200, true);
		startMakeOpenShort(31110, 31910, 200, true);
		startMakeOpenShort(29110, 29910, 200, true);
		startMakeOpenShort(28110, 28910, 200, true);
		
		startMakeOpenShort(27010, 27910, 100, false);
		
		makeOpenShort(26760, OVER, 26910, QTY, 26810, THIRD);
		makeOpenShort(26660, OVER, 26810, QTY, 26710, THIRD);
		//makeOpenShort(26560, OVER, 26710, QTY, 26610, THIRD);
		//makeOpenShort(25960, OVER, 26110, QTY, 26010, THIRD);
	}
	/** ###########  [Model 01] ########### [SHORT]  **/
	public void setShort() throws Exception{
		
		//closeShort(26810, OVER, 26710, QTY, THIRD);
		closeShort(26710, OVER, 26610, QTY, THIRD);
		closeShort(26610, OVER, 26510, QTY, THIRD);
		//closeShort(26510, OVER, 26410, QTY, THIRD);
		/** ↑↑↑↑ -------  Price Line  26636  -------  Long First ↓↓↓↓  **/
		//openShort(26510, UNDER, 26610, QTY, 6);
		openShort(26410, UNDER, 26510, QTY, 6);
		openShort(25310, UNDER, 25410, QTY, 6);
		openShort(26210, UNDER, 26310, QTY, 6);
		//<-- sync //<--
	}
	/** ###########  [Model 01] ########### **/
	public void createCloseShort()throws Exception{
		
		//makeCloseShort(26560, UNDER, 26410, QTY, 26510, RR);
		//makeCloseShort(26460, UNDER, 26310, QTY, 26410, RR);
		//makeCloseShort(26360, UNDER, 26210, QTY, 26310, RR);
		makeCloseShort(26260, UNDER, 26110, QTY, 26210, RR);
		makeCloseShort(26160, UNDER, 26010, QTY, 26110, RR);
		makeCloseShort(26060, UNDER, 25910, QTY, 26010, RR);
		makeCloseShort(25960, UNDER, 25810, QTY, 25910, RR);
		
		openShort(25710, UNDER, 25860, QTY, RR, 25760);
		
		startOpenShort(25710, 24010, 100);
		
		startOpenShort(24110, 23310, 200);
		startOpenShort(23110, 22310, 200);
		startOpenShort(22110, 21310, 200);
		//makeCloseShort(26210, UNDER, 26010, QTY, 26210, RR);
	}
	public void startOpenShort(double start, double end, double limit) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		//openShort(24110, UNDER, 24310, QTY, RR, 24160);
		
		double profitLimit = 200;
		double alarmLimit  = 150;
		
		for(double i=start; i>=end; i-=limit) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			openShort(close, UNDER, open, QTY, RR, alarm);
		}
	}

	
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */
	public void startOpenLong(double start, double end, double limit) throws Exception{
		//openLong(30010, OVER, 29810, QTY, RR, 29960);
		//openLong(29210, OVER, 29010, QTY, RR, 29160);
		//startOpenLong(29010, 29810, 200);
		double profitLimit = 200;
		double alarmLimit  = 150;
		
		for(double i=start; i<=end; i+=limit) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			openLong(close, OVER, open, QTY, RR, alarm);
		}
	 
		
	}
	public void createCloseLong() throws Exception{
		//makeCloseLong(27810, OVER, 28010, QTY, 27810, RR);
		
		//startOpenLong(32010, 32810, 200);
		//startOpenLong(31010, 31810, 200);
		startOpenLong(30010, 30810, 200);
		startOpenLong(29010, 29810, 200);
		startOpenLong(28010, 28810, 200);
		
		startOpenLong(26910, 27910, 100);
		
		//makeCloseLong(25660, OVER, 25810, QTY, 25610, 8);
		//makeCloseLong(25560, OVER, 25710, QTY, 25510, 8);
	}
	
	/** ###########  [Model 01] ########### [LONG] **/
	public void setLong() throws Exception{
		openLong(26810, OVER, 26610, QTY, 6);//<--
		openLong(26710, OVER, 26510, QTY, 6);//<--
		openLong(26610, OVER, 26410, QTY, 6);
		//openLong(26510, OVER, 26310, QTY, 6);//<--
		/** ↑↑↑↑ -------  Price Line 26256 ------- short  ↓↓↓↓  **/
		closeLong(26310, UNDER, 26510, QTY, THIRD);
		closeLong(26210, UNDER, 26410, QTY, THIRD);
		//<-- sync//<--
	}
	/** ###########  [Model 01] ########### **/
	public void createOpenLong() throws Exception{
		//makeOpenLong(26560, UNDER, 26410, QTY, 26610, THIRD);
		//makeOpenLong(26460, UNDER, 26310, QTY, 26510, THIRD);
		//makeOpenLong(26360, UNDER, 26210, QTY, 26410, THIRD);
		makeOpenLong(26260, UNDER, 26110, QTY, 26310, THIRD);
		makeOpenLong(26160, UNDER, 26010, QTY, 26210, THIRD);
		makeOpenLong(26010, UNDER, 25910, QTY, 26110, THIRD);
		makeOpenLong(25960, UNDER, 25810, QTY, 26010, THIRD);
		makeOpenLong(25860, UNDER, 25710, QTY, 25910, THIRD);
		
		
		startMakeOpenLong(25610, 23010, 100, false);
		
		startMakeOpenLong(22810, 22010, 200, true);
		startMakeOpenLong(21810, 21010, 200, true);
	}
	
	
	
	public void startMakeOpenLong(double start, double end, double limit, boolean isLoss) throws Exception{
		double profitLimit = 100;
		double alarmLimit  = 150;
		
		for(double i=start; i>=end; i-=limit) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i+alarmLimit;
			makeOpenLong(alarm, UNDER, open, QTY, close, RR);
		}
		if(isLoss) {
			//longStopLoss(start-profitLimit  , QTY2);
			//longStopLoss(end+profitLimit 	, QTY2);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}