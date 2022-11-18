package com.idk.coin.binance;

import java.awt.Toolkit;
import java.lang.reflect.GenericArrayType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.idk.coin.AlarmSound;
import com.idk.coin.CoinConfig;
import com.idk.coin.upbit.BTCbot;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.*;
import java.io.*;
import javax.sound.sampled.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BinanceTest {
	

	

public static Logger LOG =   LoggerFactory.getLogger(BinanceTest.class.getName());


	public static void main(String... args) {
		CoinConfig.loadConfig();
		System.out.println("Test Binance API");
		getCandle();
	}
	
	public static  String getPrice() {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(System.getProperty(CoinConfig.BINANCE_KEY), System.getProperty(CoinConfig.BINANCE_SECRET),options);
        List<MarkPrice> list = syncRequestClient.getMarkPrice("BTCUSDT");
      /*  addJsonArray("prices", list);*/
       return  list.toString();
	}
	public static  void getCandle() {
		//playSound();
		LOG.warn("test log");
		RequestOptions options = new RequestOptions();
	    SyncRequestClient syncRequestClient = SyncRequestClient.create(System.getProperty(CoinConfig.BINANCE_KEY), System.getProperty(CoinConfig.BINANCE_SECRET), options);
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
