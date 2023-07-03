package com.idk.coin.bybit.alram;

import com.idk.coin.AlarmSound;
import com.idk.coin.bybit.model.Balance;
import com.idk.coin.bybit.model.BybitAlarmsModel;
import com.idk.coin.bybit.model.Position;

public class CalculatePositionV4 extends CalculateModel {
	
	public CalculatePositionV4(BybitAlarmsModel parent,double price,Position buy, Position sell, Balance balance,double qty, boolean debug) {
		 super(parent, price, buy, sell, balance, qty, debug);
	}
	public double getDefaultPrice(double c_price) {
		double m = Math.floor(c_price / 1000) *1000;
		double h = Math.floor((c_price - m) / 100) * 100;
		//double d = Math.round((c_price -m -h)/ 10) * 10;
		double d = c_price -m -h;
		d = d > 70 ? 110 : (d > 30 ? 60 : 10);
		return m + h + d;
	}
	
	public void calculateStatus() throws Exception{
		logInfo("########### [ "+parent.getUser().getId()+" ]################");
		logInfo("#  		    MY START V4		 	     #");
		
		logInfo("기본 수량 : " + MAX_100_OPEN_SIZE + " , 초과 수량 : " + MAX_200_OPEN_SIZE );
		logInfo("최대 수량 : " + MAX_OPEN_SIZE + " , 최소 수량 : " + MIN_OPEN_SIZE );
		logInfo("간격 : " + INTERVAL_100 + " , 간격 : " + INTERVAL_200 );
		logInfo("수익1 : " + PROFIT_100 + " , 수익2 : " + PROFIT_200 );
		logInfo("기본수량 : " + qty + " , 현재가 : " + price + " , 가격대비 0.25% : " + (price/100) *0.25 );
		logInfo("수익1 : " + parent.take_1 + "% , 수익2 : " + parent.take_2+"% , 손절1 : "  + parent.loss_1+"%" );
		
		
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
	
	//############################## SHORT  #############################
	//############################## SHORT  #############################
	//############################## SHORT  #############################
	public void calculateShortStatus(double short_price,double short_size, double current_price, int vs_size) throws Exception{
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= vs_size <= 0 ? default_price + (PROFIT_100/2) : default_price + PROFIT_100;
		double start_close		= vs_size >= 0 ? default_price - (PROFIT_100/2) : default_price - PROFIT_100;
		
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (short_size/qty));
		int enable_close_size	= (int)((short_size/qty) - MIN_OPEN_SIZE);
		if(enable_open_size < 0) enable_close_size += enable_open_size; // 더이상 open 불가시 -초과 OPEN short open에서 수행
		
		int price_between_size	= Math.abs((int)((price - short_price)/PROFIT_100));
		boolean is_high		 	= price < short_price; 
		
		if(vs_size == 0 && default_price < price) {
			if(is_high && enable_close_size >= 0 && start_close < default_price) start_close = default_price;   
		}
		
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
	//############################## LONG  #############################
	//############################## LONG  #############################
	//############################## LONG  #############################
	
	public void calculateLongStatus(double long_price,double long_size, double current_price,int vs_size) throws Exception{
		double default_price 	= getDefaultPrice(current_price);
		double start_open		= vs_size <= 0 ? default_price - (PROFIT_100/2) : default_price - PROFIT_100;
		double start_close		= vs_size >= 0 ? default_price + (PROFIT_100/2) : default_price + PROFIT_100;
		
		int enable_open_size	= (int)(MAX_100_OPEN_SIZE - (long_size/qty));
		int enable_close_size	= (int)((long_size/qty) - MIN_OPEN_SIZE); //최소수량까지는 오픈함
		if(enable_open_size < 0)  enable_close_size += enable_open_size;  //더이상 OPEN 할수 없는경우 초과open은 long open수행
		
		int price_between_size	= Math.abs((int)( (price - long_price) / PROFIT_100 ) );
		boolean is_high		 	= price < long_price; 
		
		if(vs_size == 0 && default_price > price) {
			if(!is_high && enable_close_size >= 0 && start_close > default_price) start_close = default_price;   
		}
		
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
	 
	
	
	
 
}
