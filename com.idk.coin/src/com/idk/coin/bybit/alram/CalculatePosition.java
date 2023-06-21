package com.idk.coin.bybit.alram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class CalculatePosition {
	public static Logger LOG 			= LoggerFactory.getLogger(CalculatePosition.class.getName());
	Position buy,sell;
	Balance balance;
	double price;
	double qty;
	BybitAlarmsModel parent;
	boolean debug 	= false;
	StringBuffer printBuffer;
	
	public CalculatePosition(BybitAlarmsModel parent,double price,Position buy, Position sell, Balance balance,double qty, boolean debug) {
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
	public String toString() {
		return printBuffer.toString();
	}
	public void calculateStatus() throws Exception{
		logInfo("###########################################");
		logInfo("#  										#");
		logInfo("#  										#");
	
		double default_price = getDefaultPrice(price);
		double short_entry   = sell.getEntry_price();
		double short_size	 = sell.getSize();
		double long_entry	 = buy.getEntry_price();
		double long_size	 = buy.getSize();
		
		
		logInfo("현재가:"+price+", 기본가:" +default_price + " , [SHORT] 가격 : " + short_entry + " , 가격차이 : " + (short_entry - price) );
		logInfo("################ MY START ################");
		
		int ss = (int)(short_size/qty);
		int ls = (int)(long_size/qty);
		
		logInfo("################ SHORT START ################");
		//calShort(short_entry, short_size, price);
		calculateShortOpenStatus(short_entry, short_size, price);
		calculateShortCloseStatus(short_entry, short_size, price, ss-ls);
		logInfo("################ LONG START ################");
		//calLong(long_entry, long_size, price);
		calculateLongOpenStatus(long_entry, long_size, price);
		calculateLongCloseStatus(long_entry, long_size, price, ls-ss);

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
	
	public void calculateShortOpenStatus(double open_price,double open_size, double current_price) throws Exception{
		logInfo(open_price + ","+open_size +"################ SHORT OPEN(SELL) STATUS ################"+ current_price);
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= default_price + INTERVAL_100;
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (open_size/qty));
		logInfo("PRICE:" + current_price + " , DEFAULT:"+default_price + ", START OPEN:" + start_open);
		
		boolean is_high		 	= price < open_price; 
		double end_open 		= 0.0;
		if(is_high) {
			int price_between_size	= Math.abs((int)((price - open_price)/INTERVAL_100));
			logInfo("#   [SHORT HIGH] 현재가가 SHORT(매도가)보다 낮음(순익)		공매수(LONG) 많을가능성 많음 \t#" + price_between_size);
			logInfo("#   1 매도가까지는 100,200 매수함 -- short 사이즈와 비교해야함  \t#########");
			logInfo("#   2 매도가이후 100,100 가능매수까지함  \t#########");
			logInfo("#   3 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			if(price_between_size > 0) {
				end_open = (price_between_size * INTERVAL_100) + start_open;
				startMakeOpenShort(start_open , end_open, INTERVAL_100, PROFIT_200, false,"price between");
				start_open  = end_open + INTERVAL_100;
				enable_open_size -= price_between_size;
			}
			/**
			if(enable_open_size > 0 ) {
				end_open = (enable_open_size * INTERVAL_100) + start_open;
				startMakeOpenShort(start_open , end_open, INTERVAL_100, PROFIT_100, false);
				start_open  = end_open + INTERVAL_100;
			}**/
			//end_open	= (MAX_200_OPEN_SIZE * INTERVAL_200) + start_open;
			//startMakeOpenShort(start_open , end_open, INTERVAL_200, PROFIT_100, false);
		}else {
			logInfo("#   [SHORT LOW] 현재가가  SHORT(매도가)보다 높음(손실) 공매도(SHORT) 많을 가능성 높음 \t#");
			logInfo("#   1 가능 매도수량까지 100,100 매수함  \t#########");
			logInfo("#   2 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			//is_high= false 진입가가 현재가보다  낮은경우 - open시작점부터 오픈가능 갯수만큼 간격100 이익100 한다 이후 max200 까지 200 오픈한다
			/*if(enable_open_size > 0 ) {
				end_open = (enable_open_size * INTERVAL_100) + start_open;
				startMakeOpenShort(start_open , end_open, INTERVAL_100, PROFIT_100, false);
				start_open  = end_open + INTERVAL_100;
			}*/
		}
		
		if(enable_open_size > 0 ) {
			end_open = (enable_open_size * INTERVAL_100) + start_open;
			startMakeOpenShort(start_open , end_open, INTERVAL_100, PROFIT_100, false,"ShortOpen enable open");
			start_open  = end_open + INTERVAL_100;
		}
		
		end_open	= (MAX_200_OPEN_SIZE * INTERVAL_200) + start_open;
		startMakeOpenShort(start_open , end_open, INTERVAL_200, PROFIT_100, false,"ShortOpen common");
	}
	
	public void calculateShortCloseStatus(double open_price,double open_size, double current_price,int over_size) throws Exception{
		logInfo(open_price+"################ SHORT CLOSE(BUY) STATUS ################"+current_price);
		double default_price 	= getDefaultPrice(current_price);
		double start_close		= default_price - 100;
		int enable_close_size	= (int)((open_size/qty) - MIN_OPEN_SIZE);
		logInfo("PRICE:" + current_price + " , DEFAULT:"+default_price + ", START CLOSE:" + start_close);
		
		int price_between_size	= Math.abs((int)((price - open_price)/100));
		boolean is_high		 	= price < open_price; 
		double end_close 		= 0.0;
		
		if(is_high) {  
			logInfo("#   [SHORT HIGH]	현재가가 SHORT(매도가)보다 낮음(순익)		공매수(LONG) 많을가능성 많음 \t#");
			logInfo("#   1 가능한 size 100, 200 만큼 close함 -- short 사이즈와 비교해야함  \t#########");
			logInfo("#   2 이후 가능사이즈 없으면  OPEN 함 \t#########");
			if(over_size < 0) {
				start_close += INTERVAL_100;
				int size = Math.abs(over_size/3);
				end_close	= start_close - (size * INTERVAL_100);
				startOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "Over Size common");
				start_close = end_close - PROFIT_200;
			} 
			
			if(enable_close_size > 0 ) {
				end_close = start_close -  (enable_close_size * INTERVAL_100);
				logInfo("[0]\t makeCloseShort("+(start_close+150)+", UNDER, "+start_close+", QTY, "+(start_close+150)+", RR) ");
				startMakeCloseShort(start_close +100 , end_close, INTERVAL_100, PROFIT_200, "enable close");
				start_close  = end_close - INTERVAL_100;
			}
			end_close	= start_close - ((MAX_100_OPEN_SIZE- MIN_OPEN_SIZE) * INTERVAL_100);
			startOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "ShortClose high");
			start_close = end_close - INTERVAL_100;
			
			//end_close	= start_close - (MAX_200_OPEN_SIZE * INTERVAL_200);
			//startOpenShort(start_close, end_close, INTERVAL_200, PROFIT_200);
		 
		}else {
			logInfo("#  [SHORT LOW] 현재가가  SHORT(매도가)보다 높음(손실) 공매도(SHORT) 많을 가능성 높음 \t#" + price_between_size + " , " + over_size/2);
			logInfo("#  1 SHORT(매도가)까지 100, 100 close함 -- 가능한 size확인필요 \t#########");
			logInfo("#  1-1 매도가 많은경우 추가 \t#########");
			logInfo("#  2 SHORT(매도가)이후 나머지 가능한 size 만큼 100,200 close 함  \t#########");
			logInfo("#  3 이후 계속 상승시 max_100 , max_200만큼  OPEN 으로 변경함 \t#########");
			if(over_size > 0 ) price_between_size += over_size/2;
			if(price_between_size > enable_close_size) price_between_size = enable_close_size;
			
			if(price_between_size > 0) {
				end_close = start_close - (price_between_size * INTERVAL_100);
				startMakeCloseShort(start_close , end_close, INTERVAL_100, PROFIT_100,"price between");
				start_close  = end_close - 100;
				enable_close_size -= price_between_size;
			}
			
			if(enable_close_size > 0 ) {
				end_close = start_close -  (enable_close_size * INTERVAL_100);
				startMakeCloseShort(start_close , end_close, INTERVAL_100, PROFIT_200, "enable close");
				start_close  = end_close + INTERVAL_100;
			}
			
			end_close	= start_close - ((MAX_100_OPEN_SIZE- MIN_OPEN_SIZE) * INTERVAL_100);
			startOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200, "Short Open low");
			start_close = end_close - INTERVAL_100;
		}
		end_close	= start_close - (MAX_200_OPEN_SIZE * INTERVAL_200);
		startOpenShort(start_close, end_close, INTERVAL_200, PROFIT_200, "Short Open common");
	 
	}
	
	public void calculateLongOpenStatus(double open_price,double open_size, double current_price) throws Exception{
		logInfo(open_price + ","+open_size +"################ LONG OPEN(BUY) STATUS ################"+ current_price);
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= default_price - 100;
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (open_size/qty));
		boolean is_high		 	= price < open_price; 
		double end_open 		= 0.0;
		
		logInfo("PRICE:" + current_price + " , DEFAULT:"+default_price + ", START OPEN:" + start_open);
		
		
		if(is_high) {
			logInfo("#   [LONG HIHG] 현재가가 매수가보다 낮음(손실)		매수가 많을가능성 높음			#" );
			logInfo("#   1 가능 매수수량까지 100,100 매수함  \t#########");
			logInfo("#   2 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			
			/*if(enable_open_size > 0 ) {
				end_open = start_open -  (enable_open_size * INTERVAL_100);
				startMakeOpenLong(start_open, end_open, INTERVAL_100, PROFIT_100, false,"enable open");
				start_open  = end_open - 100;
			}*/
			//end_open	= start_open - (MAX_200_OPEN_SIZE * INTERVAL_200);
			//startMakeOpenLong(start_open, end_open, INTERVAL_200, PROFIT_200, false);
		 
		}else {
			//is_high= false 진입가가 현재가보다  낮은경우 - 진입가까지 간격100 이익200,하고 진입가 이하부터 간격100, 이익100한다.
			//추가로 size 더많은경우 생각해야함
			int price_between_size	= Math.abs((int)( (price - open_price) / 100 ) );
			logInfo("#   [LONG LOW] 현재가가 매수가보다 늪음 (순익)	Short 많을 가능성 높음 #" +price_between_size );
			logInfo("#   1 매수가까지는 100,200 매수함 -- short 사이즈와 비교해야함  \t#########");
			logInfo("#   2 매수가이후 100,100 가능매수까지함  \t#########");
			logInfo("#   3 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			if(price_between_size > 0) {
				end_open = start_open - (price_between_size * INTERVAL_100);
				startMakeOpenLong(start_open , end_open, INTERVAL_100, PROFIT_200,false,"price between");
				start_open  = end_open - INTERVAL_100;
				enable_open_size -= price_between_size;
			}
			
			/*if(enable_open_size > 0 ) {
				end_open = start_open -  (enable_open_size * INTERVAL_100);
				startMakeOpenLong(start_open , end_open, INTERVAL_100, PROFIT_100, false, "enable open");
				start_open  = end_open - 100;
			}*/
		}
		
		if(enable_open_size > 0 ) {
			end_open = start_open -  (enable_open_size * INTERVAL_100);
			startMakeOpenLong(start_open , end_open, INTERVAL_100, PROFIT_100, false, "enable open");
			start_open  = end_open - 100;
		}
		end_open	= start_open - (MAX_200_OPEN_SIZE * INTERVAL_200);
		startMakeOpenLong(start_open , end_open, INTERVAL_200, PROFIT_100, false, "Open Long common");
	}
	
	public void calculateLongCloseStatus(double open_price,double open_size, double current_price,int over_size) throws Exception{
		logInfo(open_price + ","+open_size +"################ LONG CLOSE(SELL) STATUS ################"+ current_price);
		double default_price 	= getDefaultPrice(current_price);
		double start_close		= default_price + 100;
		logInfo("PRICE:" + current_price + " , DEFAULT:"+default_price + ", START CLOSE:" + start_close);
		
		int enable_close_size	= (int)((open_size/qty) - MIN_OPEN_SIZE);
		
		boolean is_high		 	= price < open_price; 
		double end_close 		= 0.0;
		
		if(is_high) {
			int price_between_size	= Math.abs((int)( (price - open_price) / 100 ) );
			logInfo("#  [LONG HIGH] 현재가가 매수가보다 낮음(손실)		LONG 많을가능성 높음 \t#" + price_between_size + " , " + over_size/2);
			logInfo("#  1 매수가까지 100, 100 close함 -- 가능한 size확인필요 \t#########");
			logInfo("#  1-1 매수가 많은경우 추가 \t#########");
			logInfo("#  2 진입가부터는 나머지 가능한 size 만큼 100,200 close 함  \t#########");
			logInfo("#  3 이후 계속 상승시 max_100 만큼 OPEN 함 \t#########");
			
			if(over_size > 0 ) price_between_size += over_size/2;
			if(price_between_size > enable_close_size) price_between_size = enable_close_size;
			
			if(price_between_size > 0) {
				end_close = start_close + (price_between_size * INTERVAL_100);
				startMakeCloseLong(start_close , end_close, INTERVAL_100, PROFIT_200, "price between");
				start_close  = end_close + 100;
				enable_close_size -= price_between_size;
			}
			
			if(enable_close_size > 0 ) {
				end_close = start_close +  (enable_close_size * INTERVAL_100);
				startMakeCloseLong(start_close, end_close, INTERVAL_100, PROFIT_200, "enable close");
				start_close  = end_close;
			}
			end_close	= start_close + (MAX_100_OPEN_SIZE * INTERVAL_100);
			startOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"LongClose high");
			start_close  = end_close + INTERVAL_100;
			
			//end_close	= start_close + (MAX_200_OPEN_SIZE * INTERVAL_200);
			//startOpenLong(start_close, end_close, INTERVAL_200, PROFIT_200);
		 
		}else {
			logInfo("#   [LONG LOW] 현재가가 매수가보다 늪음 (순익)	SHORT 많을 가능성 높음 \t#" );
			logInfo("#   1 가능한 size 100, 200 만큼 close함 -- short 사이즈와 비교해야함  \t#########");
			logInfo("#   2 이후 가능사이즈 없으면  OPEN 함 \t#########");
			
			if(over_size < 0) {
				start_close -= INTERVAL_100;
				int size = Math.abs(over_size/3);
				end_close	= start_close + (size * INTERVAL_100);
				startOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"Over Size common");
				start_close = end_close +PROFIT_200;
			} 
			
			start_close += INTERVAL_100;
			if(enable_close_size > 0 ) {
				end_close = start_close +  (enable_close_size * INTERVAL_100);
				startMakeCloseLong(start_close , end_close, INTERVAL_100, PROFIT_200, "enable close");
				start_close  = end_close - INTERVAL_100;
			}
			
			end_close	= start_close + ((MAX_100_OPEN_SIZE-MIN_OPEN_SIZE) * INTERVAL_100);
			startOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200,"LongClose low");
			start_close  = end_close + INTERVAL_100;
			
		}
		
		end_close	= start_close + (MAX_200_OPEN_SIZE * INTERVAL_200);
		startOpenLong(start_close, end_close, INTERVAL_200, PROFIT_200,"LongClose common");
		
	}
	
	
	//#################################### 
	
	public void startMakeOpenShort(double start, double end, double interval, double profit, boolean isLoss,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  				"+msg+"						#");
		for(double i=start; i<=end; i+=interval) {
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
	public void startMakeCloseShort(double start, double end, double interval,double profit,String msg) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i>=end; i-=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseShort("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeCloseShort(alarm, parent.UNDER, open, parent.QTY, close, parent.RR);
		}
	}
	public void startOpenShort(double start, double end, double interval,double profit,String msg) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"					#");
		for(double i=start; i>=end; i-=interval) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			//logInfo("["+(count++)+"]\t openShort("+close+", UNDER, "+open+", QTY, RR, "+alarm+")  ");
			logInfo("["+(count++)+"]\t makeOpenShort("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenLong(alarm, parent.UNDER, open, parent.QTY, close, parent.RR);
			
		}
	}
	public void startMakeOpenLong(double start, double end, double interval,double profit,boolean isLoss,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"				#");
		for(double i=start; i>=end; i-=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			logInfo("["+(count++)+"]\t makeOpenLong("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenLong(alarm, parent.UNDER, open, parent.QTY, close, parent.RR);
		}
		if(isLoss) {
			
		}
	}
	public void startMakeCloseLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"						#");
		for(double i=start; i<=end; i+=interval) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			logInfo("["+(count++)+"]\t makeCloseLong("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeCloseLong(alarm, parent.OVER, open, parent.QTY, close, parent.RR);
		}
	}
	public void startOpenLong(double start, double end, double interval,double profit,String msg) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		logInfo("#  			"+msg+"				#");
		for(double i=start; i<=end; i+=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			//logInfo("["+(count++)+"]\t openLong("+close+", OVER, "+open+", QTY, RR, "+alarm+");");
			logInfo("["+(count++)+"]\t makeOpenLong("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
			if(!debug) parent.makeOpenLong(alarm, parent.OVER, open, parent.QTY, close, parent.RR);
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
