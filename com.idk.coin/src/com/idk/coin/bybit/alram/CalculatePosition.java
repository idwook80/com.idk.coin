package com.idk.coin.bybit.alram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.Position;

public class CalculatePosition {
	public static Logger LOG 			= LoggerFactory.getLogger(CalculatePosition.class.getName());
	Position buy,sell;
	Balance balance;
	double price;
	double qty;
	
	public CalculatePosition(double price,Position buy, Position sell, Balance balance,double qty) {
		this.price  = price;
		this.buy 	= buy;
		this.sell	= sell;
		this.balance= balance;
		this.qty	= qty;
	}
	public void status() throws Exception{
		LOG.info("###########################################");
		LOG.info("#  										#");
		LOG.info("#  										#");
	
		
		double max_open_size	= 15;
		double limit_open_size	= 35;
		double max_size			= limit_open_size + max_open_size;
		double min_close_size	= 5;
		
		double default_price = getDefaultPrice(price);
		double short_entry   = sell.getEntry_price();
		double short_size	 = sell.getSize();
		double long_entry	 = buy.getEntry_price();
		double long_size	 = buy.getSize();
		
		double enable_open_short_size = limit_open_size - (short_size/qty);
		double enable_close_short_size = (short_size/qty) - min_close_size;
		
		//boolean price_is_higher_than_short_entry = price > short_entry; // true 현재가보다  작다
		boolean is_short_higher					 = price < short_entry; // true 현재가보다  작다
		boolean is_short_more					 = short_size > long_size;
		double  short_more_count				 = (short_size/qty) - (long_size/qty);
		double  price_between					 = default_price - getDefaultPrice(short_entry);
		double  price_between_count				 = price_between / 100;
		
		LOG.info("현재가:"+price+"," +default_price + " , [SHORT] 가격 : " + short_entry + " , 가격차이 : " + (short_entry - price) + " , " + price_between + " , " + price_between_count);
		LOG.info( is_short_higher ? " 현재가보다 크다" : "현재가보다 작다");
		LOG.info( (is_short_more  ?  "SHORT이 많다" : "LONG이 많다") + " , " + short_more_count );
		LOG.info("[SHORT] 오픈 가능 갯수 : " + enable_open_short_size);
		LOG.info("[SHORT] 클로즈 가능 갯수 : " + enable_close_short_size +" , " + (enable_close_short_size - short_more_count ));
		LOG.info("[SHORT] 사이즈 : "+short_size + " , " +(short_size/qty)	);
		
		double start_open_short = default_price + 100;
		double start_close_short = default_price - 100;
		
		double interval_100 = 100;
		double interval_200 = 200;
		double profit_100	= 100;
		double profit_200	= 200;
		
		
		double end_open_short = start_open_short + (enable_open_short_size * 100);
		double max_open_short = end_open_short + (max_open_size * 200);
		
		LOG.info("[SHORT] open 시작 : " + start_open_short	);
		LOG.info("[SHORT] close 시작 : " + start_close_short	);
		
		LOG.info("#  										#");
		startMakeOpenShort(start_open_short , end_open_short, interval_100, profit_100, false);
		LOG.info("#  										#");
		startMakeOpenShort(end_open_short+100 , max_open_short, interval_200, profit_100, true);
		LOG.info("#  										#");
		
		double end_close_short_100_count = price_between_count > 0 ? price_between_count : 0;
		double end_close_short_200_count = enable_close_short_size-end_close_short_100_count-short_more_count;
		double end_close_short_100	= start_close_short - (end_close_short_100_count * 100);
		double end_close_short_200	= end_close_short_100 - ( end_close_short_200_count * 200);
		double max_close_short  = end_close_short_200 - (max_open_size * 200);
		
		LOG.info("#  										#");
		if(price_between_count>0) {
			startMakeCloseShort(start_close_short,end_close_short_100, interval_100, profit_100);
			start_close_short = end_close_short_100-100;
		}
		LOG.info("#  										#");
		startMakeCloseShort(start_close_short,end_close_short_200, interval_100, profit_200);
		LOG.info("#  										#");
		startOpenShort(end_close_short_200-100, max_close_short, interval_200, profit_200);
		
		LOG.info("#  										#");
		LOG.info("#  										#");
		LOG.info("################MY START ################");
	
		calShort(short_entry, short_size, price);
		//calLong(long_entry, long_size, price);

	}
	public void calShort(double entry,double size, double current_price) throws Exception{
		entry = 26810;
		//calculateShortOpenStatus(entry, size, price);
		calculateShortCloseStatus(entry, size, price);
	}
	public void calLong(double entry,double size, double price) throws Exception{
		//entry = 26810;
		calculateLongOpenStatus(entry, size, price);
		calculateLongCloseStatus(entry, size, price);
	}
	public final static double INTERVAL_100 = 100;
	public final static double INTERVAL_200 = 200;
	public final static double PROFIT_100	= 100;
	public final static double PROFIT_200	= 200;
	
	public static int MAX_200_OPEN_SIZE = 10;
	public static int MAX_100_OPEN_SIZE = 35;
	public static int MAX_OPEN_SIZE 	= MAX_100_OPEN_SIZE+ MAX_200_OPEN_SIZE;
	public static int MIN_OPEN_SIZE		= 5;
	
	public void calculateShortOpenStatus(double open_price,double open_size, double current_price) throws Exception{
		LOG.info(open_price + ","+open_size +"################ SHORT OPEN(SELL) STATUS ################"+ current_price);
		//double open_price 		= 0.0;
		//double open_size		= 0.0;
		//double current_price 	= 0.0;
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= default_price + INTERVAL_100;
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (open_size/qty));
		
		boolean is_high		 	= price < open_price; 
		if(is_high) {
			int price_between_size	= Math.abs((int)((price - open_price)/INTERVAL_100));
			LOG.info("#    is_high	현재가가 SHORT(매도가)보다 낮음(순익)		공매수(LONG) 많을가능성 많음 \t#" + price_between_size);
			LOG.info("#   1 매도가까지는 100,200 매수함 -- short 사이즈와 비교해야함  \t#########");
			LOG.info("#   2 매도가이후 100,100 가능매수까지함  \t#########");
			LOG.info("#   3 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			double end_open = 0.0;
			if(price_between_size > 0) {
				end_open = (price_between_size * INTERVAL_100) + start_open;
				startMakeOpenShort(start_open , end_open, INTERVAL_100, PROFIT_200, false);
				start_open  = end_open + INTERVAL_100;
				enable_open_size -= price_between_size;
			}
			if(enable_open_size > 0 ) {
				end_open = (enable_open_size * INTERVAL_100) + start_open;
				startMakeOpenShort(start_open , end_open, INTERVAL_100, PROFIT_100, false);
				start_open  = end_open + INTERVAL_100;
			}
			end_open	= (MAX_200_OPEN_SIZE * INTERVAL_200) + start_open;
			startMakeOpenShort(start_open , end_open, INTERVAL_200, PROFIT_100, false);
		}else {
			LOG.info("#  	is_low	현재가가  SHORT(매도가)보다 높음(손실) 공매도(SHORT) 많을 가능성 높음 \t#");
			LOG.info("#   1 가능 매도수량까지 100,100 매수함  \t#########");
			LOG.info("#   2 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			//is_high= false 진입가가 현재가보다  낮은경우 - open시작점부터 오픈가능 갯수만큼 간격100 이익100 한다 이후 max200 까지 200 오픈한다
			double end_open = 0.0;
			if(enable_open_size > 0 ) {
				end_open = (enable_open_size * INTERVAL_100) + start_open;
				startMakeOpenShort(start_open , end_open, INTERVAL_100, PROFIT_100, false);
				start_open  = end_open + INTERVAL_100;
			}
			end_open	= (MAX_200_OPEN_SIZE * INTERVAL_200) + start_open;
			startMakeOpenShort(start_open , end_open, INTERVAL_200, PROFIT_100, false);
			
		}
	}
	
	public void calculateShortCloseStatus(double open_price,double open_size, double current_price) throws Exception{
		LOG.info("################ SHORT CLOSE STATUS ################");
		//double open_price 		= 0.0;
		//double open_size		= 0.0;
		//double current_price 	= 0.0;
		double default_price 	= getDefaultPrice(current_price);
		double start_close		= default_price - 100;
		int enable_close_size	= (int)((open_size/qty) - MIN_OPEN_SIZE);
		
		int price_between_size	= Math.abs((int)((price - open_price)/100));
		boolean is_high		 	= price < open_price; 
		if(is_high) {  //LONG CLOSE is_high == false 와 절차 같음
			LOG.info("#    is_high	현재가가 SHORT(매도가)보다 낮음(순익)		공매수(LONG) 많을가능성 많음 \t#");
			//is_high= true, 진입가가  현재가보다  높은경우 - close시작부터  가능 클로즈만큼 100,200
			//첫번째는 100,150 설정 가능한지 생각해봄
			//추가로 open 더많은경우 생각해야함
			LOG.info("#   1 가능한 size 100, 200 만큼 close함 -- short 사이즈와 비교해야함  \t#########");
			LOG.info("#   2 이후 가능사이즈 없으면  OPEN 함 \t#########");
			double end_close = 0.0;
			if(enable_close_size > 0 ) {
				end_close = start_close -  (enable_close_size * INTERVAL_100);
				LOG.info("[0]\t makeCloseShort("+(start_close+150)+", UNDER, "+start_close+", QTY, "+(start_close+150)+", RR) ");
				startMakeCloseShort(start_close +100 , end_close, INTERVAL_100, PROFIT_200);
				start_close  = end_close - 100;
			}
			end_close	= start_close - ((MAX_100_OPEN_SIZE- MIN_OPEN_SIZE) * INTERVAL_100);
			startOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200);
			start_close = end_close - INTERVAL_100;
			
			end_close	= start_close - (MAX_200_OPEN_SIZE * INTERVAL_200);
			startOpenShort(start_close, end_close, INTERVAL_200, PROFIT_200);
		 
		}else {
			LOG.info("#  	is_low	현재가가  SHORT(매도가)보다 높음(손실) 공매도(SHORT) 많을 가능성 높음 \t#" + price_between_size);
			//is_high= false 진입가가 현재가보다  낮은경우 - 진입가까지 간격100 이익100,하고 진입가 이하부터 간격100, 이익200한다.
			//추가로 open 더많은경우 생각해야함
			LOG.info("#  1 SHORT(매도가)까지 100, 100 close함 -- 가능한 size확인필요 \t#########");
			LOG.info("#  2 SHORT(매도가)이후 나머지 가능한 size 만큼 100,200 close 함  \t#########");
			LOG.info("#  3 이후 계속 상승시 max_100 , max_200만큼  OPEN 으로 변경함 \t#########");
			double end_close = 0.0;
			if(price_between_size > 0) {
				end_close = start_close - (price_between_size * INTERVAL_100);
				startMakeCloseShort(start_close , end_close, INTERVAL_100, PROFIT_100);
				start_close  = end_close - 100;
				enable_close_size -= price_between_size;
			}
			
			if(enable_close_size > 0 ) {
				end_close = start_close -  (enable_close_size * INTERVAL_100);
				startMakeCloseShort(start_close , end_close, INTERVAL_100, PROFIT_200);
				start_close  = end_close + INTERVAL_100;
			}
			
			end_close	= start_close - ((MAX_100_OPEN_SIZE- MIN_OPEN_SIZE) * INTERVAL_100);
			startOpenShort(start_close, end_close, INTERVAL_100, PROFIT_200);
			start_close = end_close - INTERVAL_100;
			
			end_close	= start_close - (MAX_200_OPEN_SIZE * INTERVAL_200);
			startOpenShort(start_close, end_close, INTERVAL_200, PROFIT_200);
		}
	 
	}
	
	public void calculateLongOpenStatus(double open_price,double open_size, double current_price) throws Exception{
		LOG.info(open_price + ","+open_size +"################ LONG OPEN STATUS ################"+ current_price);
		//double open_price 	= 0.0;
		//double open_size		= 0.0;
		//double current_price 	= 0.0;
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= default_price - 100;
		
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (open_size/qty));
		boolean is_high		 	= price < open_price; 
		if(is_high) {
			LOG.info("#     is_high 현재가가 매수가보다 낮음(손실)		매수가 많을가능성 높음			#" );
			LOG.info("#   1 가능 매수수량까지 100,100 매수함  \t#########");
			LOG.info("#   2 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			
			double end_open = 0.0;
			if(enable_open_size > 0 ) {
				end_open = start_open -  (enable_open_size * INTERVAL_100);
				startMakeOpenLong(start_open, end_open, INTERVAL_100, PROFIT_100, false);
				start_open  = end_open - 100;
			}
			end_open	= start_open - (MAX_200_OPEN_SIZE * INTERVAL_200);
			startMakeOpenLong(start_open, end_open, INTERVAL_200, PROFIT_200, false);
		 
		}else {
			//is_high= false 진입가가 현재가보다  낮은경우 - 진입가까지 간격100 이익200,하고 진입가 이하부터 간격100, 이익100한다.
			//추가로 size 더많은경우 생각해야함
			int price_between_size	= Math.abs((int)( (price - open_price) / 100 ) );
			LOG.info("#  	is_low 현재가가 매수가보다 늪음 (순익)	매도가많을 가능성 높음 #" +price_between_size );
			LOG.info("#   1 매수가까지는 100,200 매수함 -- short 사이즈와 비교해야함  \t#########");
			LOG.info("#   2 매수가이후 100,100 가능매수까지함  \t#########");
			LOG.info("#   3 이후 최대치까지는 200,100 -stopLoss 생각해야함  \t#########");
			double end_open = 0.0;
			if(price_between_size > 0) {
				end_open = start_open - (price_between_size * INTERVAL_100);
				startMakeOpenLong(start_open , end_open, INTERVAL_100, PROFIT_200,false);
				start_open  = end_open - 100;
				enable_open_size -= price_between_size;
			}
			
			if(enable_open_size > 0 ) {
				end_open = start_open -  (enable_open_size * INTERVAL_100);
				startMakeOpenLong(start_open , end_open, INTERVAL_100, PROFIT_100, false);
				start_open  = end_open - 100;
			}
			end_open	= start_open - (MAX_200_OPEN_SIZE * INTERVAL_200);
			startMakeOpenLong(start_open , end_open, INTERVAL_200, PROFIT_100, false);
		}
	}
	
	public void calculateLongCloseStatus(double open_price,double open_size, double current_price) throws Exception{
		LOG.info(open_price + ","+open_size +"################ LONG CLOSE STATUS ################"+ current_price);
		//double open_price 		= 0.0;
		//double open_size		= 0.0;
		//double current_price 	= 0.0;
		double default_price 	= getDefaultPrice(current_price);
		double start_close		= default_price + 100;
		
		int enable_close_size	= (int)((open_size/qty) - MIN_OPEN_SIZE);
		
		boolean is_high		 	= price < open_price; 
		if(is_high) {
			int price_between_size	= Math.abs((int)( (price - open_price) / 100 ) );
			LOG.info("#    is_high 현재가가 매수가보다 낮음(손실)		매수가 많을가능성 높음 \t#" + price_between_size);
			LOG.info("#  1 Close 시작점부터 진입가까지 100, 100 close함 -- 가능한 size확인필요 \t#########");
			LOG.info("#  2 진입가부터는 나머지 가능한 size 만큼 100,200 close 함  \t#########");
			LOG.info("#  3 이후 계속 상승시 max_100 만큼 OPEN 함 \t#########");
			
			double end_close = 0.0;
			if(price_between_size > 0) {
				end_close = start_close + (price_between_size * INTERVAL_100);
				startMakeCloseLong(start_close , end_close, INTERVAL_100, PROFIT_200);
				start_close  = end_close + 100;
				enable_close_size -= price_between_size;
			}
			
			if(enable_close_size > 0 ) {
				end_close = start_close +  (enable_close_size * INTERVAL_100);
				startMakeCloseLong(start_close, end_close, INTERVAL_100, PROFIT_200);
				start_close  = end_close;
			}
			end_close	= start_close + (MAX_100_OPEN_SIZE * INTERVAL_100);
			startOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200);
			start_close  = end_close + 100;
			end_close	= start_close + (MAX_200_OPEN_SIZE * INTERVAL_200);
			startOpenLong(start_close, end_close, INTERVAL_200, PROFIT_200);
		 
		}else {
			LOG.info("#    is_low 현재가가 매수가보다 늪음 (순익)	매도가많을 가능성 높음 \t#" );
			LOG.info("#   1 가능한 size 100, 200 만큼 close함 -- short 사이즈와 비교해야함  \t#########");
			LOG.info("#   2 이후 가능사이즈 없으면  OPEN 함 \t#########");
			
			double end_close = 0.0;
			if(enable_close_size > 0 ) {
				end_close = start_close +  (enable_close_size * INTERVAL_100);
				startMakeCloseLong(start_close , end_close, INTERVAL_100, PROFIT_200);
				start_close  = end_close - INTERVAL_100;
			}
			end_close	= start_close + ((MAX_100_OPEN_SIZE-MIN_OPEN_SIZE) * INTERVAL_200);
			startOpenLong(start_close, end_close, INTERVAL_100, PROFIT_200);
			start_close  = end_close + INTERVAL_100;
			
			end_close	= start_close + (MAX_200_OPEN_SIZE * INTERVAL_200);
			startOpenLong(start_close, end_close, INTERVAL_200, PROFIT_200);
		}
	}
	
	
	
	
	//#################################### 
	
	public void startMakeOpenShort(double start, double end, double interval, double profit, boolean isLoss) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		LOG.info("#  										#");
		for(double i=start; i<=end; i+=interval) {
			double open = i;
			double close  = i - profitLimit;
			double alarm = i-alarmLimit;
			LOG.info("["+(count++)+"]\t makeOpenShort("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
		}
		LOG.info("#  										#");
		if(isLoss) {
			//shortStopLoss(end-profitLimit   , QTY2);
			//shortStopLoss(start+profitLimit , QTY2);
		}
	}
	public void startMakeCloseShort(double start, double end, double interval,double profit) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		LOG.info("#  										#");
		for(double i=start; i>=end; i-=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			LOG.info("["+(count++)+"]\t makeCloseShort("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
		}
	}
	public void startOpenShort(double start, double end, double interval,double profit) throws Exception{
		//openShort(24910, UNDER, 25110, QTY, RR, 24960);
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		LOG.info("#  										#");
		for(double i=start; i>=end; i-=interval) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			//LOG.info("["+(count++)+"]\t openShort("+close+", UNDER, "+open+", QTY, RR, "+alarm+")  ");
			LOG.info("["+(count++)+"]\t makeOpenShort("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
			
		}
	}
	public void startMakeOpenLong(double start, double end, double interval,double profit,boolean isLoss) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		LOG.info("#  										#");
		for(double i=start; i>=end; i-=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			LOG.info("["+(count++)+"]\t makeOpenLong("+alarm+", UNDER, "+open+", QTY, "+close+", RR) ");
		}
		if(isLoss) {
			
		}
	}
	public void startMakeCloseLong(double start, double end, double interval,double profit) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		LOG.info("#  										#");
		for(double i=start; i<=end; i+=interval) {
			double open 	= i;
			double close  	= i - profitLimit;
			double alarm 	= i - alarmLimit;
			LOG.info("["+(count++)+"]\t makeCloseLong("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
		}
	}
	public void startOpenLong(double start, double end, double interval,double profit) throws Exception{
		double profitLimit = profit;
		double alarmLimit  = 150;
		int count = 1;
		LOG.info("#  										#");
		for(double i=start; i<=end; i+=interval) {
			double open 	= i;
			double close  	= i + profitLimit;
			double alarm 	= i + alarmLimit;
			//LOG.info("["+(count++)+"]\t openLong("+close+", OVER, "+open+", QTY, RR, "+alarm+");");
			LOG.info("["+(count++)+"]\t makeOpenLong("+alarm+", OVER, "+open+", QTY, "+close+", RR) ");
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
		calculateShort(default_price, sell.getSize());
	}
	public double getDefaultPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		//double d = Math.floor((c_price -m -h)/ 10) * 10;
		return m + h + 10;
	}
	public void calculateShort(double def, double entry) {
		double startOpenShort		= def + 100;
		double closeShortShort 		= def - 100;
		LOG.info("start open short:"+startOpenShort);
		LOG.info("start close short:"+closeShortShort);
	}
}
