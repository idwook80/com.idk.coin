package com.idk.coin.bybit.alram;

import java.util.ArrayList;

import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.account.WalletRest;
import com.idk.coin.bybit.db.BybitAlarmDao;
import com.idk.coin.bybit.db.BybitUser;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class AlarmManager02 extends BybitAlarmsModel {
	public AlarmManager02(String symbol, BybitUser user) throws Exception{
		super(symbol, user);
	}
	public AlarmManager02(String symbol, String web_id, String web_pw) throws Exception{
		super(symbol, web_id, web_pw);
	}
	public void userSet() throws Exception{
		String default_qty = user.getDefault_qty();
		if(default_qty == null) default_qty = "0.05";
		setDefault_qty(Double.valueOf(default_qty));
		LOSS_TRIGGER_QTY= 0.001;
	    MIN_PROFIT		= 50;
	  
	}
	/*public static int IDLE_TIME 	= 10;      // 10분마다
	public int idle_check_time 		= -1; //min
	public static int RESET_TIME 	= 60 * 4; //reset 4시간마다
	public int reset_check_time 	= RESET_TIME; //min
	
	public void run() {
		while(is_run) {
			try {
				Thread.sleep(1000* 60 *1);
				LOG.info(this.getSize() + "  : " + this.getClass().getName() +" 알람 리셋 : " + reset_check_time + "분, 알랑 활성체크: " + idle_check_time + "분");
				status();
				if(idle_check_time-- < 0) {
					this.printListString();
					checkAlarmIdles(5);
					idle_check_time = IDLE_TIME;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	public void alarmSet() throws Exception{
		createOpenShort();
		setShort();
		createCloseShort();
		
		createCloseLong();
		setLong();
		createOpenLong();
		enableDatabase(DISABLE);
	}
	*/
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
	double startCalculateEquity = 0.0;
	int first_per		= 3;
	int second_per		= 5;
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
		
			if(per > second_per) {								//all take profit 매시간 검사
				if(clearProfit(price, balance, buy, sell, oldEquity,newEquity,profit, second_per)) return;
			}else if(per > first_per) { 						//half take profit message_count/4 4시간 검사
				if(message_count % 2 == 0)  {
					if(clearProfit(price, balance, buy, sell,oldEquity,newEquity, profit, first_per)) return;
				}
			}
			
			
			calculator = new CalculatePositionV3(this,price, buy, sell, balance, QTY, debug);
			calculator.setSizeValue(20, 5, 5);
			calculator.calculateStatus();
			LOG.debug(calculator.toAlarmString());
			insertAlarmMessageToDatabase(calculator);
			if(startCalculateModel == null)  	{
				startCalculateModel = calculator;
				if(startCalculateEquity > 0 ) startCalculateModel.balance.setEquity(startCalculateEquity);
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
		
		int short_close_size	= per == first_per ? ((short_size - min_open)/2) : (short_size - min_open);
		int long_close_size		= per == first_per ? ((long_size - min_open)/2) : (long_size - min_open);
		
		double default_price 	= getDefaultPrice(price);
		
		double close_long_size		= long_close_size * QTY;
		double close_short_size  	= short_close_size * QTY;
		
		StringBuffer msg = new StringBuffer();
		msg.append("Take Profit : "+profit+"("+per+"%)");
		double long_trigger		= default_price-50;
		double short_trigger	= default_price+50;
		
		closeLong(long_trigger, OVER, long_trigger, close_long_size,ONCE);
		closeShort(short_trigger, UNDER, short_trigger, close_short_size, ONCE);
		
		msg.append("closeLong("+long_trigger+", OVER, "+long_trigger+", "+close_long_size+",ONCE)");
		msg.append("closeShort("+short_trigger+", UNDER, "+short_trigger+", "+close_short_size+",ONCE)");
		
		startCalculateModel = calculator;
		reset_check_time = 1;
		message_count = 1;
		BybitAlarmDao.getInstace().insertMessage(user.getId(), user.getUser_id(), getSymbol(),
				oldEquity,newEquity,profit,per,0,msg.toString());
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
	public void createOpenShort()  throws Exception{
		makeOpenShort(32710, OVER, 32910, QTY, 32810, RR);
		makeOpenShort(32510, OVER, 32710, QTY, 32610, RR);
		shortStopLoss(32610, QTY2);
		makeOpenShort(32310, OVER, 32510, QTY, 32410, RR);
		makeOpenShort(32110, OVER, 32310, QTY, 32210, RR);
		shortStopLoss(32210, QTY2);
		makeOpenShort(31910, OVER, 32110, QTY, 32010, RR);
		
		makeOpenShort(31710, OVER, 31910, QTY, 31860, RR);
		shortStopLoss(31810, QTY2);
		makeOpenShort(31510, OVER, 31710, QTY, 31660, RR);
		makeOpenShort(31310, OVER, 31510, QTY, 31460, RR);
		makeOpenShort(31110, OVER, 31310, QTY, 31260, RR);
		shortStopLoss(31210, QTY2);
		makeOpenShort(30910, OVER, 31110, QTY, 31060, RR);
		 
		makeOpenShort(30710, OVER, 30910, QTY, 30860, RR);
		shortStopLoss(30810, QTY2);
		makeOpenShort(30510, OVER, 30710, QTY, 30660, THIRD);
		makeOpenShort(30310, OVER, 30510, QTY, 30460, THIRD);
		makeOpenShort(30110, OVER, 30310, QTY, 30260, THIRD);
		shortStopLoss(30210, QTY2);
		makeOpenShort(29910, OVER, 30110, QTY, 30060, THIRD);
		
		makeOpenShort(29710, OVER, 29910, QTY, 29860, THIRD);
		shortStopLoss(29810, QTY2);
		makeOpenShort(29510, OVER, 29710, QTY, 29660, THIRD);
		//makeOpenShort(29310, OVER, 29510, QTY, 29460, THIRD);
		//makeOpenShort(29110, OVER, 29310, QTY, 29260, THIRD);
		//makeOpenShort(28910, OVER, 29110, QTY, 29060, THIRD);
		
		//Short First ## http://172.16.203.17:8081/mvest/bybit/
		//POSITION  --> [LONG] 
		
	}
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [SHORT]  **/
	public void setShort() throws Exception{
		closeShort(29510, OVER, 29460, QTY, THIRD);
		//closeShort(29310, OVER, 29260, QTY, THIRD);
		//closeShort(29260, OVER, 29210, QTY, THIRD);
		
		/** ↑↑↑↑ -------  Price Line  27432  -------  Long First ↓↓↓↓  **/
		openShort(29260, UNDER, 29310, QTY, 6);
		openShort(29210, UNDER, 29260, QTY, 6);
		openShort(28960, UNDER, 29010, QTY, 6);
		openShort(28860, UNDER, 28910, QTY, 6);
		openShort(28760, UNDER, 28810, QTY, 6);
		openShort(28710, UNDER, 28760, QTY, 6);
		openShort(28660, UNDER, 28710, QTY, 6);
 
 
		//<-- sync //<--
	}
	/** ###########  [Model 02] ########### **/
	public void createCloseShort()throws Exception{
		makeCloseShort(28660, UNDER, 28510, QTY, 28610, RR);
		makeCloseShort(28560, UNDER, 28410, QTY, 28510, RR);
		makeCloseShort(28460, UNDER, 28310, QTY, 28410, RR);
		makeCloseShort(28360, UNDER, 28210, QTY, 28310, RR);
		makeCloseShort(28260, UNDER, 28110, QTY, 28210, RR);
		makeCloseShort(28160, UNDER, 28010, QTY, 28110, RR);
		
		
		makeCloseShort(28060, UNDER, 27910, QTY, 28010, RR);
		makeCloseShort(27860, UNDER, 27710, QTY, 27910, RR);
		makeCloseShort(27660, UNDER, 27510, QTY, 27710, RR);
		makeCloseShort(27460, UNDER, 27310, QTY, 27510, RR);
		makeCloseShort(27260, UNDER, 27110, QTY, 27310, RR);
		
		makeCloseShort(27060, UNDER, 26910, QTY, 27110, RR);
		makeCloseShort(26860, UNDER, 26710, QTY, 26910, RR);
		makeCloseShort(26660, UNDER, 26510, QTY, 26710, RR);
		makeCloseShort(26460, UNDER, 26310, QTY, 26510, RR);
		makeCloseShort(26260, UNDER, 26110, QTY, 26310, RR);
		//makeCloseShort(26160, UNDER, 26010, QTY, 26210, RR);
		
		openShort(26010, UNDER, 26210, QTY, 6, 25960);
		openShort(25910, UNDER, 26110, QTY, 6, 25960);
		openShort(25710, UNDER, 25910, QTY, 6, 25760);
		openShort(25510, UNDER, 25710, QTY, 6, 25560);
		openShort(25310, UNDER, 25510, QTY, 6, 25360);
		openShort(25110, UNDER, 25310, QTY, 6, 25160);
		
		openShort(24910, UNDER, 25110, QTY, 6, 24960);
		openShort(24710, UNDER, 24910, QTY, 6, 24760);
		openShort(24510, UNDER, 24710, QTY, 6, 24560);
		openShort(24410, UNDER, 24610, QTY, 6, 24460);
		openShort(24310, UNDER, 24510, QTY, 6, 24360);
		openShort(24110, UNDER, 24310, QTY, 6, 24160);
		//createCloseShort(25110, UNDER, 24910, QTY, 25110, RR);
		
		openShort(23910, UNDER, 24110, QTY, RR, 23960);
		openShort(23710, UNDER, 23910, QTY, RR, 23760);
		openShort(23510, UNDER, 23710, QTY, RR, 23560);
		openShort(23310, UNDER, 23510, QTY, RR, 23360);
		openShort(23110, UNDER, 23310, QTY, RR, 23160);
		//createCloseShort(24110, UNDER, 23910, QTY, 24110, RR);
		
		openShort(22910, UNDER, 23110, QTY, RR, 22960);
		openShort(22710, UNDER, 22910, QTY, RR, 22760);
		openShort(22510, UNDER, 22710, QTY, RR, 22560);
		openShort(22310, UNDER, 22510, QTY, RR, 22360);
		openShort(22110, UNDER, 22310, QTY, RR, 22160);
	}

	
	
	
/**
 * #################   [LONG]      ############# [LONG][LONG]
 * @throws Exception
 */
	public void createCloseLong() throws Exception{
		openLong(33010, OVER, 32810, QTY, RR, 32960);
		openLong(32810, OVER, 32610, QTY, RR, 32760);
		openLong(32610, OVER, 32410, QTY, RR, 32560);
		openLong(32410, OVER, 32210, QTY, RR, 32360);
		openLong(32210, OVER, 32010, QTY, RR, 32160);
		
		openLong(32010, OVER, 31810, QTY, RR, 31960);
		openLong(31810, OVER, 31610, QTY, RR, 31760);
		openLong(31610, OVER, 31410, QTY, RR, 31560);
		openLong(31410, OVER, 31210, QTY, RR, 31360);
		openLong(31210, OVER, 31010, QTY, RR, 31160);
		
		openLong(31010, OVER, 30810, QTY, RR, 30960);
		openLong(30810, OVER, 30610, QTY, RR, 30760);
		openLong(30610, OVER, 30410, QTY, RR, 30560);
		openLong(30410, OVER, 30210, QTY, RR, 30360);
		openLong(30210, OVER, 30010, QTY, RR, 30160);
		
		//createCloseLong(29010, OVER, 29210, QTY, 29010, RR);
		openLong(30010, OVER, 29810, QTY, RR, 29960);
		openLong(29810, OVER, 29610, QTY, RR, 29760);
		openLong(29610, OVER, 29410, QTY, RR, 29560);
		//openLong(29410, OVER, 29210, QTY, RR, 29360);
		//openLong(29210, OVER, 29010, QTY, RR, 29160);
		
		
		//openLong(29110, OVER, 28910, QTY, RR, 29060);
		//openLong(29010, OVER, 28810, QTY, RR, 28960);
		//openLong(28910, OVER, 28710, QTY, RR, 28860);
		
		

		
		//createCloseLong(25210, OVER, 25310, QTY, 25210, 6);
	}
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	/** ###########  [Model idwook02][BTCUSDT] ########### [LONG] **/
	public void setLong() throws Exception{
		
		//<-- sync //<--
//		openLong(28010, OVER, 27810, QTY, RR);
		/** ↑↑↑↑ -------  Price Line 27049 ------- short  ↓↓↓↓  **/
		closeLong(29210, UNDER, 29410, QTY, THIRD); 
		closeLong(29010, UNDER, 29210, QTY, THIRD); 
		
		closeLong(28910, UNDER, 29110, QTY, THIRD); 
		closeLong(28810, UNDER, 29010, QTY, THIRD); 
		closeLong(28710, UNDER, 28910, QTY, THIRD);
		closeLong(28610, UNDER, 28810, QTY, THIRD);
		closeLong(28410, UNDER, 28610, QTY, THIRD);
		//closeLong(27010, UNDER, 27210, QTY, THIRD);
		
		//<-- sync //<--
	}
	/** ###########  [Model 02] ########### **/
	public void createOpenLong() throws Exception{
		makeOpenLong(28360, UNDER, 28210, QTY, 28410, THIRD);
		makeOpenLong(28160, UNDER, 28010, QTY, 28210, THIRD);
		
		makeOpenLong(27960, UNDER, 27810, QTY, 28010, THIRD);
		makeOpenLong(27760, UNDER, 27610, QTY, 27810, THIRD);
		makeOpenLong(27560, UNDER, 27410, QTY, 27610, THIRD);
		makeOpenLong(27360, UNDER, 27210, QTY, 27410, THIRD);
		makeOpenLong(27160, UNDER, 27010, QTY, 27210, THIRD);
		
		makeOpenLong(26960, UNDER, 26810, QTY, 27010, THIRD);
		makeOpenLong(26760, UNDER, 26610, QTY, 26810, THIRD);
		makeOpenLong(26560, UNDER, 26410, QTY, 26610, THIRD);
		makeOpenLong(26360, UNDER, 26210, QTY, 26260, THIRD);
		makeOpenLong(26160, UNDER, 26010, QTY, 26060, THIRD);
	 
		makeOpenLong(25960, UNDER, 25810, QTY, 25860, THIRD);
		makeOpenLong(25760, UNDER, 25610, QTY, 25660, THIRD);
		makeOpenLong(25560, UNDER, 25410, QTY, 25460, THIRD);
		makeOpenLong(25360, UNDER, 25210, QTY, 25260, THIRD);
		makeOpenLong(25160, UNDER, 25010, QTY, 25060, THIRD);
		
		makeOpenLong(24960, UNDER, 24810, QTY, 24860, THIRD);
		makeOpenLong(24760, UNDER, 24610, QTY, 24660, THIRD);
		makeOpenLong(24560, UNDER, 24410, QTY, 24460, THIRD);
		makeOpenLong(24360, UNDER, 24210, QTY, 24260, THIRD);
		makeOpenLong(24160, UNDER, 24010, QTY, 24110, THIRD);
		
		
		makeOpenLong(24010, UNDER, 23810, QTY, 23860, THIRD);
		longStopLoss(23710, QTY2);
		makeOpenLong(23810, UNDER, 23610, QTY, 23660, THIRD);
		makeOpenLong(23610, UNDER, 23410, QTY, 23460, THIRD);
		makeOpenLong(23410, UNDER, 23210, QTY, 23260, THIRD);
		longStopLoss(23110, QTY2);
		makeOpenLong(23210, UNDER, 23010, QTY, 23060, THIRD);
		
		makeOpenLong(23010, UNDER, 22810, QTY, 22860, THIRD);
		longStopLoss(22710, QTY2);
		makeOpenLong(22810, UNDER, 22610, QTY, 22660, THIRD);
		makeOpenLong(22610, UNDER, 22410, QTY, 22460, THIRD);
		makeOpenLong(22410, UNDER, 22210, QTY, 22260, THIRD);
		longStopLoss(22110, QTY2);
		makeOpenLong(22210, UNDER, 22010, QTY, 22060, THIRD);
		/**
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
	}
	
	
}
