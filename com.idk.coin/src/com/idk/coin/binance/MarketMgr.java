package com.idk.coin.binance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.MarkPrice;
import com.binance.client.model.market.SymbolPrice;
import com.binance.client.model.market.Trade;

public class MarketMgr {
	public static void main(String... args) {
		new MarketMgr();
	}
	
	SyncRequestClient syncRequestClient;
	public MarketMgr() {
		initClient();
		getMarkPrice();
		getRecentTrades();
		getSymbolPriceTicker();
		
		for(;;) {
				getCandleStick(CandlestickInterval.ONE_MINUTE);
				getCandleStick(CandlestickInterval.FIVE_MINUTES);
				System.out.println("######\n");
				
				try {
						Thread.sleep(1000*1);
				}catch(Exception e) {
					e.printStackTrace();
				}
		}
	}
	public void initClient() {
		RequestOptions options = new RequestOptions();
	    syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

	}
	public void getCandleStick(CandlestickInterval interval) {
		List<Candlestick> list = syncRequestClient.getCandlestick("BTCUSDT", interval, null, null, 2);
		int sec  = new Date(System.currentTimeMillis()).getSeconds();
		
		
		int secVol = 10;
		int minVol = secVol * 6;
		
		
		Candlestick before = null;
		for(Candlestick c : list) {
			if(before == null) before = c;
			
			Long  time = c.getOpenTime();
			Date datetime 		= new Date(time);
			datetime.setSeconds(sec);
			
			
			double volume = c.getVolume().doubleValue();
			double open	  = c.getOpen().doubleValue();
			double low    = c.getLow().doubleValue();
			double close  = c.getClose().doubleValue();
			double high   = c.getHigh().doubleValue();
			double cur    = getSymbolPriceTicker().doubleValue();
			if(open < close) System.out.println("▲▲▲ ↑↑↑↑ ↑↑↑↑  ↑↑↑↑ ▲▲▲");
			else if(open > close) System.out.println("▼▼▼ ↓↓↓↓ ↓↓↓↓ ↓↓↓↓ ▼▼▼");
			//System.out.println(c);
			int compare = (int)(volume - before.getVolume().doubleValue());
			
			System.out.println(interval + " , " + datetime.toGMTString() 
				+ " open:"+ open + " , close : " + close 
				+ " , current : "+ cur 
				+ " , volume[" + c.getVolume().doubleValue() + "]" 
				+ compare
				+ ((sec * secVol ) < c.getVolume().intValue()));
			
		}
	}
	public void getTrades() {
		List<Trade> trades = syncRequestClient.getRecentTrades("BTCUSDT", 10);
	}
	
	
	public void getMarkPrice() {
		List<MarkPrice>	 list = syncRequestClient.getMarkPrice("BTCUSDT");
		for(MarkPrice l : list) {
			System.out.println(l);
		}
	}
	public void getRecentTrades() {
		List<Trade> list = syncRequestClient.getRecentTrades("BTCUSDT", 10);
		for(Trade l : list) {
			System.out.println(l);
		}
	}
	public BigDecimal getSymbolPriceTicker() {
		List<SymbolPrice> list = syncRequestClient.getSymbolPriceTicker("BTCUSDT");
		BigDecimal ret = null;
		for(SymbolPrice l : list) {
			//System.out.println(l);
			ret =  l.getPrice();
		}
		return ret;
	}
	
	
	
}
