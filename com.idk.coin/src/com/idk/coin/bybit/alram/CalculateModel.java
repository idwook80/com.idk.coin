package com.idk.coin.bybit.alram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

abstract public class CalculateModel {
	public static Logger LOG 			= LoggerFactory.getLogger(CalculatePositionV3.class.getName());
	Position buy,sell;
	Balance balance;
	double price;
	double qty;
	BybitAlarmsModel parent;
	boolean debug 	= false;
	StringBuffer printBuffer;
	public double INTERVAL_100  = 100;
	public double INTERVAL_200  = 200;
	public double PROFIT_100	= 100;
	public double PROFIT_200	= 200;
	
	public int MAX_100_OPEN_SIZE = 20;
	public int MAX_200_OPEN_SIZE = 10;
	public int MAX_OPEN_SIZE 	= MAX_100_OPEN_SIZE+ MAX_200_OPEN_SIZE;
	public int MIN_OPEN_SIZE	= 5;
	
	public CalculateModel(BybitAlarmsModel parent,double price,Position buy, Position sell, Balance balance,double qty, boolean debug) {
		this.parent = parent;
		this.price  = price;
		this.buy 	= buy;
		this.sell	= sell;
		this.balance= balance;
		this.qty	= qty;
		this.debug 	= debug;
		printBuffer = new StringBuffer();
	}
	public void logInfo(String str) {
		printBuffer.append(str+"\n");
	}
	public String toAlarmString() {
		return printBuffer.toString();
	}
	
	public void setIntervalProfit(double interval_100, double interval_200, double profit_100, double profit_200) {
		INTERVAL_100  = interval_100;
		INTERVAL_200  = interval_200;
		PROFIT_100	= profit_100;
		PROFIT_200	= profit_200;
	}

	public void setSizeValue(int open_100,int open_200,int open_min) {
		this.MAX_100_OPEN_SIZE	= open_100;
		this.MAX_200_OPEN_SIZE 	= open_200;
		this.MIN_OPEN_SIZE 		= open_min;
		MAX_OPEN_SIZE 			= open_100 + open_200;
	}
	
	abstract public void calculateStatus() throws Exception;
	abstract public double getDefaultPrice(double c_price) ;

	
	
	//############################## SHORT  #############################
	//############################## SHORT  #############################
	//############################## SHORT  #############################

	
	
	public void calculateShortOpen(double default_price, double start_open, 
			int enable_open_size,boolean is_high, int price_between_size, int vs_size) throws Exception{
		logInfo("");
		logInfo("############################# SHORT OPEN(SELL) STRATEGY ################################"); 
		
		double end_open 		= 0.0;
		int between_size		= price_between_size;
		//가격 상승시 전략
		if(enable_open_size < 0) {  //더이상 open 불가 short stoploss 개념
			int over_size = Math.abs(enable_open_size);
			start_open -= PROFIT_100;
			end_open = start_open + (over_size * INTERVAL_100);
			startOverCloseShort(start_open, end_open, INTERVAL_100, PROFIT_200, "11.[상승][오픈 불가][단가 ?] Over Close I100,P200("+(over_size)+")");
			start_open = end_open + PROFIT_200;
		}else {
			if(is_high) {
				logInfo("#   [SHORT HIGH] 현재가가 SHORT(매도가)보다 낮음(순익) LONG 많을가능성 많음 \t#" + between_size);
				logInfo("#   1 매도가까지는 100,200 매수함 -- short 사이즈와 비교해야함  \t#########");
				logInfo("#   2 매도가이후 100,100 가능매수까지함  \t#########");
				logInfo("#   3 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
				
				
				if(between_size > enable_open_size) between_size = enable_open_size;
				if(between_size > 0) {
					end_open = (between_size * INTERVAL_100) + start_open;
					startOverOpenShort(start_open , end_open, INTERVAL_100, PROFIT_200, false,"12.[상승][오픈 가능][단가 높음] I100,P200 ("+between_size+")");
					start_open  = end_open;
					enable_open_size -= between_size;
				}
			}else {
				logInfo("#   [SHORT LOW] 현재가가  SHORT(매도가)보다 높음(손실) SHORT 많을 가능성 높음 \t#");
				logInfo("#   1 가능 매도수량까지 100,100 매수함  \t#########");
				logInfo("#   2 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			}
			
			if(enable_open_size > 0 ) {
				end_open = start_open + (enable_open_size * INTERVAL_100) ;
				startOverOpenShort(start_open , end_open, INTERVAL_100, PROFIT_100, false,"13.[상승][오픈 가능][단가 낮음] I100,P100 ("+enable_open_size+")");
				start_open  = end_open;
			}
		}
		
		end_open	= start_open + (MAX_200_OPEN_SIZE * INTERVAL_200);
		startOverOpenShort(start_open , end_open, INTERVAL_200, PROFIT_100, false,"14.[상승][오픈 불가][단가 낮음] 초과오픈 I200,P100("+MAX_200_OPEN_SIZE+")");
	}
	
	
	
	
	public void calculateShortClose(double default_price, double start_close, 
			int enable_close_size,boolean is_high,int price_between_size,int vs_size) throws Exception{
		logInfo("");
		logInfo("############################# SHORT CLOSE(BUY) STRATEGY ################################"); 
		
		double end_close 		= 0.0;
		int between_size		= price_between_size;
		//상승 대비 close는 ShortOpen이 대신함
		if(is_high) {  
			logInfo("#   [SHORT HIGH]	현재가가 SHORT 보다 낮음(순익)		LONG 많을가능성 많음 \t#");
			logInfo("#   1 가능한 size 100, 200 만큼 close함 -- short 사이즈와 비교해야함  \t#########");
			logInfo("#   2 이후 가능사이즈 없으면  OPEN 함 \t#########");
			if(vs_size < 0 ) { //short이 적다 if(vs_size < 0 && enable_close_size < 0) {
				boolean is_long = enable_close_size < Math.abs(vs_size);  //long 너무 많은경우 먼전 long 클로즈 
				int size = Math.abs(vs_size/3);
				logInfo("21.[상승][][단가?] Long Enable : " + is_long + " , size : " + size);
				if(size > 0) {
					start_close += INTERVAL_100;
					end_close	= start_close - (size * INTERVAL_100);
					if(is_long)  	startUnderCloseLong(start_close, end_close, INTERVAL_100, PROFIT_200, "21.[하락][포지션반대][단가?] Long 2배이상 I100,P200("+size+")");
					else startUnderOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "21.[하락][오픈가능][단가?] Long 2배이하 I100,P200("+size+")");
					start_close = end_close - INTERVAL_100;
				}
			} 
			
		}else {
			logInfo("#  [SHORT LOW] 현재가가  SHORT보다 높음(손실) SHORT 많을 가능성 높음 \t#" + between_size + " , " + vs_size);
			logInfo("#  1 SHORT(매도가)까지 100, 100 close함 -- 가능한 size확인필요 \t#########");
			logInfo("#  1-1 매도가 많은경우 추가 \t#########");
			logInfo("#  2 SHORT(매도가)이후 나머지 가능한 size 만큼 100,200 close 함  \t#########");
			logInfo("#  3 이후 계속 상승시 max_100 , max_200만큼  OPEN 으로 변경함 \t#########");
			
			if(vs_size < 0 ) { //short 많을 가능성이 크나 반대로 Long 많은 경우
				boolean is_long = enable_close_size < Math.abs(vs_size);  //long 너무 많은경우 먼전 long 클로즈 
				int size = Math.abs(vs_size/3);
				logInfo("21-1.[상승][][단가?] Long Enable : " + is_long + " , size : " + size);
				if(size > 0) {
					start_close += INTERVAL_100;
					end_close	= start_close - (size * INTERVAL_100);
					if(is_long) startUnderCloseLong(start_close, end_close, INTERVAL_100, PROFIT_200, "21-1.[하락][포지션반대][단가?] Long 2배이상 I100,P200("+size+")");
					else  startUnderOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "21-2.[하락][오픈가능][단가?] Long 2배이하 I100,P200("+size+")");	
					start_close = end_close;
				}
			}else { //if(vs_size > 0) short 많은경우
				between_size += vs_size/2;
			}
			
			if(between_size > enable_close_size) between_size = enable_close_size;
			
			if(between_size > 0) {
				end_close = start_close - (between_size * INTERVAL_100);
				startUnderCloseShort(start_close , end_close, INTERVAL_100, PROFIT_100,"22.[하락][클로즈 가능][단가 낮음] I100,P100("+between_size+")");
				start_close  		= end_close;
				enable_close_size -= between_size;
			}
			
			
		}
		if(enable_close_size > 0 ) {
			end_close = start_close -  (enable_close_size * INTERVAL_100);
			startUnderCloseShort(start_close , end_close, INTERVAL_100, PROFIT_200, "23.[하락][클로즈 가능][단가 낮음] I100,P200 ("+enable_close_size+")");
			start_close  = end_close + PROFIT_200;
		}
		
		int open_size = (MAX_100_OPEN_SIZE- MIN_OPEN_SIZE);
		end_close	= start_close - (open_size * INTERVAL_100);
		startUnderOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "24.[하락][클로즈 블가][단가 낮음] 오픈전환 I100,P200 ("+open_size+")");
		start_close = end_close - INTERVAL_100;
		
		end_close	= start_close - (MAX_200_OPEN_SIZE * INTERVAL_200);
		startUnderOpenShort(start_close, end_close, INTERVAL_200, PROFIT_200, "25.[하락][클로즈 블가][단가 낮음] 초과오픈  I200,P200("+MAX_200_OPEN_SIZE+")");
	 
	}
	
	//############################## LONG  #############################
	//############################## LONG  #############################
	//############################## LONG  #############################
	
	
	public void calculateLongOpen(double default_price, double start_open, 
				int enable_open_size,boolean is_high, int price_between_size, int vs_size) throws Exception{
		logInfo("");
		logInfo("############################# LONG OPEN(BUY) STRATEGY ################################"); 
		double end_open 		= 0.0;
		int between_size		= price_between_size;
		//가격 하락시 전략
		if(enable_open_size < 0) {  //long stop loss 개념
			//하락시 더이상 LONG OPEN 할수 없는경우  Under Close함 <- 하락대비 Close전략 
			int over_size  = Math.abs(enable_open_size); //초과 수량 Close 먼저 수행
			start_open += PROFIT_100;
			end_open = start_open -  (over_size * INTERVAL_100);
			startUnderCloseLong(start_open , end_open, INTERVAL_100, PROFIT_200, "31.[하락][오픈 불가][단가 ?] Under Close I100,P200("+(over_size)+")");
			start_open  = end_open - PROFIT_200;
			
		}else {
			//오픈 가능한 경우
			if(!is_high) {
				logInfo("#   [LONG LOW] 현재가가 더 놈음(순익)	Short 많을 가능성 높음 #" +between_size );
				logInfo("#   1 매수가까지는 100,200 매수함 -- 수익 극대화  \t#########");
				logInfo("#   2 매수가이후 100,100 가능매수까지함  \t#########");
				logInfo("#   3 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
				
				if(between_size > enable_open_size) between_size = enable_open_size;
				if(between_size > 0) { 		//오픈가가 기존 오픈가보다 높아  OPEN 단가 상승함 (수익을 최대화해야함)
					end_open = start_open - (between_size * INTERVAL_100);
					startUnderOpenLong(start_open , end_open, INTERVAL_100, PROFIT_200,false,"32.[하락][오픈 가능][단가 낮음] I100,P200 ("+between_size+") between size");
					start_open  = end_open;
					enable_open_size -= between_size;
				}
				
			}else {
				logInfo("#   [LONG HIGH] 현재가가 더 낮음(손실)		Long 많을가능성 높음			#" );
				logInfo("#   1 가능 매수수량까지 100,100 매수함 단가낮춤  \t#########");
				logInfo("#   2 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			}
		
			if(enable_open_size > 0 ) {  
				end_open = start_open -  (enable_open_size * INTERVAL_100);
				startUnderOpenLong(start_open , end_open, INTERVAL_100, PROFIT_100, false, "33.[하락][오픈 가능][단가 높음] I100:P100("+enable_open_size+") enable open size");
				start_open  = end_open;
			}
		}
		
		
		//이후 게속 하락시 혹시 모르니 초과 수량 OPEN 함
		end_open	= start_open - (MAX_200_OPEN_SIZE * INTERVAL_200);
		startUnderOpenLong(start_open , end_open, INTERVAL_200, PROFIT_100, false, "34.[하락][오픈 불가][단가 높음] I200:P100("+MAX_200_OPEN_SIZE+")");
	}
	
	public void calculateLongClose(double default_price, double start_close, 
						int enable_close_size,boolean is_high,int price_between_size,int vs_size) throws Exception{
		logInfo("");
		logInfo("############################# LONG CLOSE(SELL) STRATEGY ################################"); 
		double end_close 		= 0.0;
		int between_size		= price_between_size;
		//하락대비 CLOSE는 LongOpen이 대신함
		if(!is_high) {
			logInfo("#   [LONG LOW] 현재가가 더 늪음 (순익)	SHORT 많을 가능성 높음 \t#" );
			logInfo("#   1 가능한 size 100, 200 만큼 close함 -- short 사이즈와 비교해야함  \t#########");
			logInfo("#   2 이후 가능사이즈 없으면  OPEN 함 \t#########");
			
			if(vs_size < 0) { //단가는 낮아 수익이 있지만 Short 사이즈가 많은 경우 상승 오픈으로 갯수if(vs_size < 0 && enable_close_size < 0) { 
				boolean is_short = enable_close_size < Math.abs(vs_size);
				int size = Math.abs(vs_size/3);
				logInfo("41.[상승][][단가?] Short Enable : " + is_short + " , size : " + size);
				if(size > 0) {
					start_close -= INTERVAL_100;
					end_close	= start_close + (size * INTERVAL_100);
					if(is_short) startOverCloseShort(start_close, end_close, INTERVAL_100, PROFIT_200,"41.[상승][반대가능][단가?] Short 2배이상  I100,P200("+size+")");
					else  startOverOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"41.[상승][오픈가능][단가?] Short 2배이하 I100,P200("+size+")");
					start_close = end_close;
				}
			} 
		}else {
			logInfo("#  [LONG HIGH] 현재가가 더 낮음(손실)		LONG 많을가능성 높음 \t#" + price_between_size + " , " + vs_size/2);
			logInfo("#  1 현재가~매수가 100,100 단가  낮춤 -- 가능한 size확인필요 \t#########");
			logInfo("#  1-1 매수가 많은경우 추가 \t#########");
			logInfo("#  2 매수가~ size 만큼 100,200 close 함  \t#########");
			logInfo("#  3 이후 계속 상승시 오픈 가능만큼 오픈 함 100,200 \t#########");
			if(vs_size < 0) { //단가는 낮아 수익이 있지만 Short 사이즈가 많은 경우 상승 오픈으로 갯수if(vs_size < 0 && enable_close_size < 0) { 
				boolean is_short = enable_close_size < Math.abs(vs_size);
				int size = Math.abs(vs_size/3);
				logInfo("41-1.[상승][][단가?] Short Enable : " + is_short + " , size : " + size);
				if(size > 0) {
					start_close -= INTERVAL_100;
					end_close	= start_close + (size * INTERVAL_100);
					if(is_short) startOverCloseShort(start_close, end_close, INTERVAL_100, PROFIT_200,"41-1.[상승][반대가능][단가?] < Short 2배이상  I100,P200("+size+")");
					else startOverOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"41-1.[상승][오픈가능][단가?] < Short 2배이하 I100,P200("+size+")");
					start_close = end_close ;
				}
			} else {
				between_size += vs_size/2; 
			}
			if(between_size > enable_close_size) between_size = enable_close_size;
			
			if(between_size > 0) {
				end_close = start_close + (between_size * INTERVAL_100);
				startOverCloseLong(start_close , end_close, INTERVAL_100, PROFIT_100, "42.[상승][클로즈 가능][단가 높음] - 단가까지 I100,P100 ("+between_size+")");
				start_close 	 	= end_close;
				enable_close_size 	-= between_size;
			}
		}
		
		if(enable_close_size > 0 ) {  
			end_close = start_close +  (enable_close_size * INTERVAL_100);
			startOverCloseLong(start_close , end_close, INTERVAL_100, PROFIT_200, "43.[상승][클로즈 가능][단가 낮음] - 나머지 클로즈 I100,P200 ("+enable_close_size+")");
			start_close  = end_close - PROFIT_200;
		}
		
		//더이상 CLOSE 할수 없고  상승시  OPEN으로 대신함
		int open_size = (MAX_100_OPEN_SIZE-MIN_OPEN_SIZE-MIN_OPEN_SIZE);
		end_close	= start_close + ( open_size * INTERVAL_100);
		startOverOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"44.[상승][클로즈 불가][단가 낮음]  상승 오픈전환  - I100 P200 OPEN ("+open_size+")");
		start_close  = end_close + INTERVAL_100;
		
		end_close	= start_close + (MAX_200_OPEN_SIZE * INTERVAL_200);
		startOverOpenLong(start_close, end_close, INTERVAL_200, PROFIT_200,"45.[상승][클로즈 불가][단가 낮음] 초과 상승오픈 - I200 P200 OPEN ("+MAX_200_OPEN_SIZE+")");
		
	}
	
	
	
	//#################################### SHORT 
	
	public void startOverOpenShort(double start, double end, double interval, double profit, boolean isLoss,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  				"+msg+"						#");
		for(double i=start; i<end; i+=interval) {
			double open = i;
			double close  = i - profitLimit;
			double alarm = i-alarmLimit;
			logInfo("["+(count++)+"]\t makeOpenShort("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenShort(alarm, parent.OVER, open, parent.QTY, close, parent.RR);
		}
		logInfo("#  										#");
		if(isLoss) {
			//shortStopLoss(end-profitLimit   , QTY2);
			//shortStopLoss(start+profitLimit , QTY2);
		}
	}
	public void startUnderOpenShort(double start, double end, double interval,double profit,String msg) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i>end; i-=interval) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			//logInfo("["+(count++)+"]\t openShort("+close+", UNDER, "+open+", QTY, RR, "+alarm+")  ");
			logInfo("["+(count++)+"]\t makeOpenShort("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenShort(alarm, parent.UNDER, open, parent.QTY, close, parent.RR);
			
		}
	}
	public void startUnderCloseShort(double start, double end, double interval,double profit,String msg) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i>end; i-=interval) {
			double close 	= i;
			double open  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseShort("+alarm+", UNDER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseShort(alarm, parent.UNDER, close, parent.QTY, open, parent.RR);
		}
	}
	public void startOverCloseShort(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i<end; i+=interval) {
			double close 	= i;
			double open  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseShort("+alarm+", OVER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseShort(alarm, parent.OVER, close, parent.QTY, open, parent.RR);
		}
	}
	
	
	//#################################### LONG
	public void startUnderOpenLong(double start, double end, double interval,double profit,boolean isLoss,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"				#");
		for(double i=start; i>end; i-=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeOpenLong("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenLong(alarm, parent.UNDER, open, parent.QTY, close, parent.RR);
		}
		if(isLoss) {
			
		}
	}
	public void startOverOpenLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"				#");
		for(double i=start; i<end; i+=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			//logInfo("["+(count++)+"]\t openLong("+close+", OVER, "+open+", QTY, RR, "+alarm+");");
			logInfo("["+(count++)+"]\t makeOpenLong("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenLong(alarm, parent.OVER, open, parent.QTY, close, parent.RR);
		}
	}
	public void startOverCloseLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"						#");
		for(double i=start; i<end; i+=interval) {
			double close 	= i;
			double open  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseLong("+alarm+", OVER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseLong(alarm, parent.OVER, close, parent.QTY, open, parent.RR);
		}
	}
	public void startUnderCloseLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"						#");
		for(double i=start; i>end; i-=interval) {
			double close 	= i;
			double open  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
	 
			/*logInfo("["+(count++)+"]\tcloseLong("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");*/
			logInfo("["+(count++)+"]\t makeCloseLong("+alarm+", UNDER, "+close+", QTY, "+open+", RR) ");
			if(!debug) parent.makeCloseLong(alarm, parent.UNDER, close, parent.QTY, open, parent.RR);
		}
	}
	public void currentStatus() throws Exception{
		LOG.info(balance.toString());
		LOG.info("[LONG]"+buy.toString());
		LOG.info("[SHORT]"+sell.toString());
		double default_price = getDefaultPrice(price);
		
		LOG.info("현재가:["+default_price + "],잔고 : [" + String.format("%.2f",balance.getEquity())  + "]"
				+","+ "SHORT : [" + sell.getEntry_price() + "]," + String.format("%.2f",sell.getSize()) + "주"
				+","+ "LONG : ["+ buy.getEntry_price() +"],"+ String.format("%.2f",buy.getSize()) + "주"); 
	}

	public Position getBuy() {
		return buy;
	}
	public void setBuy(Position buy) {
		this.buy = buy;
	}
	public Position getSell() {
		return sell;
	}
	public void setSell(Position sell) {
		this.sell = sell;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public BybitAlarmsModel getParent() {
		return parent;
	}
	public void setParent(BybitAlarmsModel parent) {
		this.parent = parent;
	}
	
}
