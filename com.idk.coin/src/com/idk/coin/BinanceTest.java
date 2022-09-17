package com.idk.coin;

import java.awt.Toolkit;
import java.lang.reflect.GenericArrayType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.idk.coin.upbit.BTCbot;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.*;
import java.io.*;
import javax.sound.sampled.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BinanceTest {
	
	
public final static String API_KEY 		= "Gju5mxDvTQwRmIyfMT9BzqSlpomk0FSSpkzbQZAfXG1goB48j4bsRlydXontikUp";
public final static String SECRET_KEY 	= "rCotzv08GlsISkVoujokuxsfAh3jeZedIQ6DsuKhNFwBfdsBrtU6WtB8KTm5Beq0";

public static Logger LOG =   LoggerFactory.getLogger(BinanceTest.class.getName());

	public static void main(String... args) {
		System.out.println("Test Binance API");
		getCandle();
	}
	
	public static  String getPrice() {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(API_KEY, SECRET_KEY, options);
        List<MarkPrice> list = syncRequestClient.getMarkPrice("BTCUSDT");
      /*  addJsonArray("prices", list);*/
        
       return  list.toString();
	}
	public static  void getCandle() {
		playSound();
		LOG.warn("test log");
		RequestOptions options = new RequestOptions();
	    SyncRequestClient syncRequestClient = SyncRequestClient.create(API_KEY, SECRET_KEY, options);
	    int before 		= 0;
	    double interval = 3;
	    int perVolume	= 15;	//10,15,20
	    int maxVolume 	= 800; //500,800,1000
	    
	    double minInter = interval;
	    double maxInter = interval * 2;
	    
	    BigDecimal last_close = null;
	    BigDecimal last_open  = null;
	    for(;;) {
	    			
	    			List<Candlestick> list = syncRequestClient.getCandlestick("BTCUSDT", CandlestickInterval.ONE_MINUTE, null, null, 1);
	    			int sec  = new Date(System.currentTimeMillis()).getSeconds();
	    			
	    			List<Candlestick> mins_5 = syncRequestClient.getCandlestick("BTCUSDT", CandlestickInterval.FIVE_MINUTES, null, null, 1);
	    			List<SymbolPrice> prices = syncRequestClient.getSymbolPriceTicker("BTCUSDT");
	    			BigDecimal price = null;
	    			for(SymbolPrice l : prices) {
	    				price =  l.getPrice();
	    			}
	    			
	    			for(Candlestick c : list) {
	    				if(last_close == null) {
	    					last_close = c.getOpen();
	    					last_open  = c.getOpen();
	    				}
	    				
	    				Long  time = c.getOpenTime();
	    				Long close_time = c.getCloseTime();
	    				Date datetime 		= new Date(time);
	    				datetime.setSeconds(sec);
	    				Date closeDateTime = new Date(close_time);
	    				
	    				BigDecimal volume  	= c.getVolume();
	    				BigDecimal open 	= c.getOpen();
	    				BigDecimal low 		= c.getLow();
	    				BigDecimal close 	= c.getClose();
	    				BigDecimal high 	= c.getHigh();
	    				
	    				int curVol = (volume.intValue() - before );
	    				double defVol = interval * perVolume;
	    				
	    				LOG.warn(datetime.toGMTString() + " : " + sec + " sec , " + volume.intValue() + " - " + before + "  =  "+ curVol + " , " + String.format("%.2f",curVol/interval));
	    				LOG.warn(datetime.toGMTString()+  " 현재가  : " + price + " : " + (open.doubleValue() < close.doubleValue() ? "상승" : "하락"));
	    				
	    				/*if(last_close.doubleValue() < close.doubleValue() )  LOG.warn("############### " + last_close + " ++++ " + c.getClose() + " ##################");
	    				else LOG.warn("############### " + last_close + " ---- " + c.getClose() + " ##################");
	    				if(last_open.doubleValue() < close.doubleValue() )  LOG.warn("############## " + last_open + " 상승 " + c.getClose() + " ##################");
	    				else LOG.warn("############### " + last_open + " 하락 " + c.getClose() + " ##################");
	    				*/
	    				
	    				last_close	= c.getClose();
	    			    last_open 	= c.getOpen();
	    				
	    				if(  volume.intValue() > maxVolume ||  curVol > defVol ) {
	    					
	    					if(volume.intValue() > maxVolume) {
	    						interval = maxInter;
	    						playAlarm();
	    					}
	    					else if( curVol > (defVol * 2) ) playBeep03();
	    					else playBeep01();
	    					
	    					
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
	    						
	    						/*if(tr.getIsBuyerMaker()) LOG.warn(tr.toString() + "[매도]");
	    						else LOG.error(tr.toString() +  "[매수]");*/
	    					}
	    					//LOG.error("매수 갯수 : " + buyCount + " , Qty : " + buyQty);
	    					//LOG.warn("매도 갯수 : " + sellCout + " , Qty : " + sellQty);
	    					
	    					/*if(buyCount < sellCout && buyQty < sellQty) {
	    						LOG.warn("매도(SELL, SHORT)우위   매도(SELL , OPEN SHORT, CLOSE LONG)   하락 ---------------------");
	    					}else if(buyCount > sellCout && buyQty > sellQty) {
	    						LOG.warn("매수(BUY OPEN) 우위   매수(BUY  OPEN LONG , CLOSE SHORT)   상승  +++++++++++++++");
	    					}*/
	    					 
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
	public static void playBeep01() {
		LOG.warn("##########################대량 거래 01 ~~~#################################");
		playSound("beep01.wav");	
	}
	public static void playBeep02() {
		playSound("beep02.wav");
	}
	public static void playBeep03() {
		LOG.warn("##########################대량 거래 04 ~~~#################################");
		playSound("beep04.wav");
	}
	public static void playAlarm() {
	//playSound("distress.wav");
	//playSound("beep-07a.wav");
		playSound("Alarm02.wav");
	}
	public static void playSound() {
		playSound("Alarm02.wav");
	}
	public static void playSound(String filename) {
		try {
			
			URL url = CoinMain.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = .5D; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(gainControl.getMaximum());
			// float ovol  = gainControl.getValue();
			// gainControl.setValue(10.0f);
			 clip.start();
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 try {
					 Thread.sleep(1000*2); 
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
 
	
	
}
