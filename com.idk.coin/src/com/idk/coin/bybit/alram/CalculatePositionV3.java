package com.idk.coin.bybit.alram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.AlarmSound;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class CalculatePositionV3 {
	public static Logger LOG 			= LoggerFactory.getLogger(CalculatePositionV3.class.getName());
	Position buy,sell;
	Balance balance;
	double price;
	double qty;
	BybitAlarmsModel parent;
	boolean debug 	= false;
	StringBuffer printBuffer;
	
	public CalculatePositionV3(BybitAlarmsModel parent,double price,Position buy, Position sell, Balance balance,double qty, boolean debug) {
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
	public void calculateStatus() throws Exception{
		logInfo("########################################################################");
		logInfo("#  																	#");
		logInfo("#  							 MY START								#");
		
		logInfo("OPEN 100 수량 : " + MAX_100_OPEN_SIZE + " , OPEN 200 수량 : " + MAX_200_OPEN_SIZE );
		logInfo("최대 수량 : " + MAX_OPEN_SIZE + " , 최소 수량 : " + MIN_OPEN_SIZE );
		
		
		double default_price = getDefaultPrice(price);
		double short_entry   = sell.getEntry_price();
		double short_size	 = sell.getSize();
		double long_entry	 = buy.getEntry_price();
		double long_size	 = buy.getSize();
	
		
		int ss = (int)(short_size/qty);
		int ls = (int)(long_size/qty);
		
		logInfo("");
		logInfo("############################# SHORT START ################################");
		calculateShortStatus(short_entry, short_size, price, ss-ls);
		logInfo("");
		logInfo("############################# LONG START ################################");
		calculateLongStatus(long_entry, long_size, price, ls-ss);

	}
	public final static double INTERVAL_100 = 100;
	public final static double INTERVAL_200 = 200;
	public final static double PROFIT_100	= 100;
	public final static double PROFIT_200	= 200;
	
	public int MAX_200_OPEN_SIZE = 10;
	public int MAX_100_OPEN_SIZE = 40;
	public int MAX_OPEN_SIZE 	= MAX_100_OPEN_SIZE+ MAX_200_OPEN_SIZE;
	public int MIN_OPEN_SIZE		= 5;
	public void setSizeValue(int open_100,int open_200,int open_min) {
		this.MAX_100_OPEN_SIZE	= open_100;
		this.MAX_200_OPEN_SIZE 	= open_200;
		this.MIN_OPEN_SIZE 		= open_min;
		MAX_OPEN_SIZE 			= open_100 + open_200;
	}
	
	
	//############################## SHORT  #############################
	//############################## SHORT  #############################
	//############################## SHORT  #############################
	public void calculateShortStatus(double short_price,double short_size, double current_price, int vs_size) throws Exception{
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= default_price + PROFIT_100;
		double start_close		= default_price - PROFIT_100;
		
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (short_size/qty));
		int enable_close_size	= (int)((short_size/qty) - MIN_OPEN_SIZE);
		if(enable_open_size < 0) enable_close_size += enable_open_size; // 더이상 open 불가시 -초과 OPEN short open에서 수행
		
		int price_between_size	= Math.abs((int)((price - short_price)/PROFIT_100));
		boolean is_high		 	= price < short_price; 
		
		logInfo("");
		logInfo("############################# 	SHORT STATUS START ################################");
		logInfo(sell.toString());
		logInfo("현재가:" + current_price + " , 기준가 : "+default_price);
		logInfo("오픈가:" + short_price + " , 수량 : "+short_size);
		logInfo("오픈 가능수량 : " + enable_open_size + " , 클로즈 가능 수량:"+enable_close_size);
		logInfo("오픈 시작가 : " + start_open  + ", 클로즈 시작가:" + start_close);
		logInfo("가격 차이 : "  + (current_price - short_price) + " , 현재가대비 : "+ ( is_high ? "Short 진입이 높다(순익)" : "Short 진입이 낮다(손실)" ));
		logInfo("vs[PRICE]사이즈 : "  + price_between_size);
		logInfo("vs[LONG]사이즈 : "  + vs_size + " 개 " +(vs_size > 0 ? "SHORT 많다" : " SHORT 적다") );
		logInfo("############################# 	LONG STATUS END	################################");
		
		calculateShortOpen(default_price, start_open, enable_open_size, is_high, price_between_size, vs_size);
		calculateShortClose(default_price, start_close, enable_close_size, is_high, price_between_size, vs_size);
	}
	
	
	
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
				logInfo("#   [SHORT LOW] 현재가가  SHORT(매도가)보다 높음(손실) 공매도(SHORT) 많을 가능성 높음 \t#");
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
			logInfo("#   [SHORT HIGH]	현재가가 SHORT(매도가)보다 낮음(순익)		공매수(LONG) 많을가능성 많음 \t#");
			logInfo("#   1 가능한 size 100, 200 만큼 close함 -- short 사이즈와 비교해야함  \t#########");
			logInfo("#   2 이후 가능사이즈 없으면  OPEN 함 \t#########");
			if(vs_size < 0 ) { //short이 적다 if(vs_size < 0 && enable_close_size < 0) {
				boolean is_long = enable_close_size < Math.abs(vs_size);  //long 너무 많은경우 먼전 long 클로즈 
				start_close += INTERVAL_100;
				int size = Math.abs(vs_size/3);
				end_close	= start_close - (size * INTERVAL_100);
				if(is_long) startUnderCloseLong(start_close, end_close, INTERVAL_100, PROFIT_200, "21.[하락][반대가능][단가?] < Long 2배이상 I100,P200("+size+")");
				else startUnderOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "21.[하락][오픈가능][단가?] < Long 2배이하 I100,P200("+size+")");
				start_close = end_close - PROFIT_200;
			} 
			
		}else {
			logInfo("#  [SHORT LOW] 현재가가  SHORT(매도가)보다 높음(손실) SHORT 많을 가능성 높음 \t#" + between_size + " , " + vs_size);
			logInfo("#  1 SHORT(매도가)까지 100, 100 close함 -- 가능한 size확인필요 \t#########");
			logInfo("#  1-1 매도가 많은경우 추가 \t#########");
			logInfo("#  2 SHORT(매도가)이후 나머지 가능한 size 만큼 100,200 close 함  \t#########");
			logInfo("#  3 이후 계속 상승시 max_100 , max_200만큼  OPEN 으로 변경함 \t#########");
			
			if(vs_size < 0 ) { //short이 적은경우
				boolean is_long = enable_close_size < Math.abs(vs_size);  //long 너무 많은경우 먼전 long 클로즈 
				start_close += INTERVAL_100;
				int size = Math.abs(vs_size/3);
				end_close	= start_close - (size * INTERVAL_100);
				if(is_long) startUnderCloseLong(start_close, end_close, INTERVAL_100, PROFIT_200, "21-1.[하락][반대가능][단가?] < Long 2배이상 I100,P200("+size+")");
				else startUnderOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "21-2.[하락][오픈가능][단가?] < Long 2배이하 I100,P200("+size+")");
				start_close = end_close - PROFIT_100;
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
	
	public void calculateLongStatus(double long_price,double long_size, double current_price,int vs_size) throws Exception{
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= default_price - PROFIT_100;
		double start_close		= default_price + PROFIT_100;
		
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (long_size/qty));
		int enable_close_size	= (int)((long_size/qty) - MIN_OPEN_SIZE); //최소수량까지는 오픈함
		if(enable_open_size < 0)  enable_close_size += enable_open_size;  //더이상 OPEN 할수 없는경우 초과open은 long open수행
		
		int price_between_size	= Math.abs((int)( (price - long_price) / PROFIT_100 ) );
		boolean is_high		 	= price < long_price; 
		
		logInfo("");
		logInfo("############################# 	LONG STATUS START ################################");
		logInfo(buy.toString());
		logInfo("현재가 :" + current_price + " , 기준가 : "+default_price);
		logInfo("오픈가 : " + long_price + " , 수량 : "+long_size);
		logInfo("오픈 가능수량 : " + enable_open_size + " , 클로즈 가능 수량 : "+enable_close_size);
		logInfo("오픈 시작가 : " + start_open  + ", 클로즈 시작가 : " + start_close);
		logInfo("가격 차이 : "  + (current_price - long_price) + " , 현재가대비 : "+ ( is_high ? "Long 진입이높다(손실)" : "Long 진입이낮다(순익)" ));
		logInfo("vs[PRICE]사이즈 : "  + price_between_size);
		logInfo("vs[SHORT]사이즈 : "  + vs_size + " 개 " +(vs_size>0 ? "LONG 많다" : " LONG 적다") );
		logInfo("############################# 	LONG STATUS END	################################");
		
		calculateLongOpen(default_price, start_open, enable_open_size, is_high, price_between_size,vs_size);
		calculateLongClose(default_price, start_close, enable_close_size, is_high, price_between_size,vs_size);
		AlarmSound.playReset();
	}
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
				start_close -= INTERVAL_100;
				int size = Math.abs(vs_size/3);
				end_close	= start_close + (size * INTERVAL_100);
				if(is_short) startOverCloseShort(start_close, end_close, INTERVAL_100, PROFIT_200,"41.[상승][반대가능][단가?] < Short 2배이상  I100,P200("+size+")");
				else startOverOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"41.[상승][오픈가능][단가?] < Short 2배이하 I100,P200("+size+")");
				start_close = end_close + PROFIT_200;
			} 
		}else {
			logInfo("#  [LONG HIGH] 현재가가 더 낮음(손실)		LONG 많을가능성 높음 \t#" + price_between_size + " , " + vs_size/2);
			logInfo("#  1 현재가~매수가 100,100 단가  낮춤 -- 가능한 size확인필요 \t#########");
			logInfo("#  1-1 매수가 많은경우 추가 \t#########");
			logInfo("#  2 매수가~ size 만큼 100,200 close 함  \t#########");
			logInfo("#  3 이후 계속 상승시 오픈 가능만큼 오픈 함 100,200 \t#########");
			if(vs_size < 0) { //단가는 낮아 수익이 있지만 Short 사이즈가 많은 경우 상승 오픈으로 갯수if(vs_size < 0 && enable_close_size < 0) { 
				boolean is_short = enable_close_size < Math.abs(vs_size);
				start_close -= INTERVAL_100;
				int size = Math.abs(vs_size/3);
				end_close	= start_close + (size * INTERVAL_100);
				if(is_short) startOverCloseShort(start_close, end_close, INTERVAL_100, PROFIT_200,"41.[상승][반대가능][단가?] < Short 2배이상  I100,P200("+size+")");
				else startOverOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"41.[상승][오픈가능][단가?] < Short 2배이하 I100,P200("+size+")");
				start_close = end_close + PROFIT_100;
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
	public double getDefaultPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		//double d = Math.floor((c_price -m -h)/ 10) * 10;
		double d = Math.round((c_price -m -h)/ 10) * 10;
		d = d > 50 ? 110 : 10;
		return m + h + d;
	}
 
}
