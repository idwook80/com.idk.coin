package com.idk.coin.binance.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SubscriptionListener;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.event.SymbolTickerEvent;
import com.binance.client.model.market.Candlestick;
import com.binance.client.model.market.MarkPrice;
import com.binance.client.model.market.SymbolPrice;
import com.binance.client.model.market.Trade;
import com.idk.coin.AlarmSound;
import com.idk.coin.CoinConfig;
import com.idk.coin.binance.BinanceTest;
import com.idk.coin.bybit.model.PriceListener;

public class BinanceMarketModel  implements Runnable {
	public static Logger LOG =   LoggerFactory.getLogger(BinanceMarketModel.class.getName());
	public boolean is_run = false;
	Thread thread;
	String symbol;
	String api_key;
	String api_secret;
	public static boolean DEBUG = false;
	
	ArrayList<PriceListener> listeners;
	
	public static void main(String... args) {
		CoinConfig.loadConfig();
		//new BinanceMarketModel();
	}
	public BinanceMarketModel(String symbol, String api_key, String api_secret){
		listeners = new ArrayList();
		this.symbol = symbol;
		this.api_key  = api_key;
		this.api_secret = api_secret;
		init();
		//startMarket();
	}
	public void init() {}
	public void startMarket() {
		is_run = true;
		thread  = new Thread(this);
		thread.start();
	}
	public void stopMarket() {
		is_run = false;
		if(thread != null) {
			try {
				thread.interrupt();
			}catch(Exception e) {
				e.printStackTrace();
			}
			thread = null;
		}
	}
	RequestOptions options;
	SyncRequestClient syncRequestClient;
	public void run() {
		options = new RequestOptions();
	    syncRequestClient = SyncRequestClient.create(api_key, api_secret, options);
	    String listenKey = syncRequestClient.startUserDataStream();
	    System.out.println("listenKey: " + listenKey);
        syncRequestClient.keepUserDataStream(listenKey);
        SubscriptionClient client = SubscriptionClient.create();
        client.subscribeSymbolTickerEvent("btcusdt", new SubscriptionListener<SymbolTickerEvent>() {
			
			@Override
			public void onReceive(SymbolTickerEvent data) {
				// TODO Auto-generated method stub
/*				System.out.println(data.getLastPrice().doubleValue());*/
				notifyPrice(data.getLastPrice().doubleValue());
				
			}
		}, null);
		while(is_run) {
			try {
				getCandlestick();
			Thread.sleep(500);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		 syncRequestClient.closeUserDataStream(listenKey);
	}
	
	public void addPriceListener(PriceListener l) {
		synchronized (listeners) {
			listeners.add(l);
		}
	}
	public void removePriceListener(PriceListener l) {
		synchronized (listeners) {
			listeners.remove(l);
		}
	}
	public synchronized void notifyPrice(double price) {
		//LOG.info(" # " + price + " # ");
		PriceListener[] ls = listeners.toArray(new PriceListener[0]);
		for(PriceListener l : ls) {
			l.checkAlarmPrice(price);
		}
	}
	
	int count = 0;
	public  void getCandlestick() {
		//playSound();
	    int before 		= 0;
	    int maxValue 	= 500; //500,800,1000
	    
		List<Candlestick> list = syncRequestClient.getCandlestick("BTCUSDT", CandlestickInterval.ONE_MINUTE, null, null, 1);
		int sec  = new Date(System.currentTimeMillis()).getSeconds();
		
		for(Candlestick c : list) {
			Long  time = c.getOpenTime();
			Date datetime 		= new Date(time);
			datetime.setSeconds(sec);
			BigDecimal volume  	= c.getVolume();
			BigDecimal open 	= c.getOpen();
			BigDecimal low 		= c.getLow();
			BigDecimal close 	= c.getClose();
			BigDecimal high 	= c.getHigh();
			BigDecimal price 		= getSymbolPriceTicker();
			int curVol = (volume.intValue() - before );
			if(count-- < 0 ) {
				LOG.info(close.doubleValue() + " # " +price.doubleValue() + " # "
							+ volume.intValue() + " - " + before + "  =  "+ curVol);
				count = 1000;
			}
			notifyPrice(close.doubleValue());
		}
	    		
	    	 
        
	}
	public  String getPrice() {
        List<MarkPrice> list = syncRequestClient.getMarkPrice("BTCUSDT");
      /*  addJsonArray("prices", list);*/
       return  list.toString();
	}
	public  void getCandle() {
		//playSound();
		LOG.warn("test log");
	    int before 		= 0;
	    double interval = 1;
	    int ration 		= 10;	//10,15,20
	    int maxValue 	= 500; //500,800,1000
	    
	    double minInter = interval;
	    double maxInter = interval*2;
	    
	    for(;;) {
	    			
	    			List<Candlestick> list = syncRequestClient.getCandlestick("BTCUSDT", CandlestickInterval.ONE_MINUTE, null, null, 1);
	    			int sec  = new Date(System.currentTimeMillis()).getSeconds();
	    			
	    			for(Candlestick c : list) {
	    				Long  time = c.getOpenTime();
	    				Date datetime 		= new Date(time);
	    				datetime.setSeconds(sec);
	    				BigDecimal volume  	= c.getVolume();
	    				BigDecimal open 	= c.getOpen();
	    				BigDecimal low 		= c.getLow();
	    				BigDecimal close 	= c.getClose();
	    				BigDecimal high 	= c.getHigh();
	    				
	    				int curVol = (volume.intValue() - before );
	    				double comp = interval * ration;
	    				
	    				LOG.warn(datetime.toGMTString() + " : " + sec + "sec , " + volume.intValue() + " - " + before + "  =  "+ curVol);
	    				
	    				if(  volume.intValue() > maxValue ||  curVol > comp) {
	    					
	    					LOG.warn(datetime.toGMTString()+ " : " + c);
	    					if(volume.intValue() > maxValue) {
	    						interval = maxInter;
	    						AlarmSound.beep01();
	    					}
	    					else if(curVol > (comp*2)) AlarmSound.beep04();
	    					else AlarmSound.beep02();
	    					
	    					List<Trade> trades = syncRequestClient.getRecentTrades("BTCUSDT", 10);
	    					
	    					int sellCout 	= 0;  //isBuyMarker = true
	    					int buyCount 	= 0;  //isBuyMaker  = false
	    					double sellQty	= 0.0;
	    					double buyQty 	= 0.0;
	    					
	    					for(Trade tr : trades) {
	    						if(tr.getIsBuyerMaker()) {
	    							sellCout++;
	    							sellQty += tr.getQty().doubleValue();
	    							
	    						}else {
	    							buyCount++;
	    							buyQty += tr.getQty().doubleValue();
	    						}
	    						LOG.warn(tr.toString() + " , " + (tr.getIsBuyerMaker() ?  "[메도]" : "[매수]") );
	    					}
	    					LOG.warn("매수 갯수 : " + buyCount + " , Qty : " + buyQty);
	    					LOG.warn("매도 갯수 : " + sellCout + " , Qty : " + sellQty);
	    					
	    					if(buyCount < sellCout && buyQty < sellQty) {
	    						LOG.warn("매도(SELL, SHORT)우위   매도(SELL , OPEN SHORT, CLOSE LONG)   하락중~ ---------------------");
	    					}else if(buyCount > sellCout && buyQty > sellQty) {
	    						LOG.warn("매수(BUY OPEN) 우위   매수(BUY  OPEN LONG , CLOSE SHORT)   상승중~  +++++++++++++++++");
	    					}
	    					 
	    				}
	    				before = volume.intValue();
	    			}
	    		if(sec < 10 || sec + interval > 60 ) interval = minInter;
	    		
	    	try {
				Thread.sleep((int)(1000*interval));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
        
	}
	public BigDecimal getSymbolPriceTicker() {
		RequestOptions options = new RequestOptions();
	    SyncRequestClient syncRequestClient = SyncRequestClient.create(System.getProperty(CoinConfig.BINANCE_KEY), System.getProperty(CoinConfig.BINANCE_SECRET), options);
	   
		List<SymbolPrice> list = syncRequestClient.getSymbolPriceTicker("BTCUSDT");
		BigDecimal ret = null;
		for(SymbolPrice l : list) {
			//System.out.println(l);
			ret =  l.getPrice();
		}
		return ret;
	}
	
	
}
