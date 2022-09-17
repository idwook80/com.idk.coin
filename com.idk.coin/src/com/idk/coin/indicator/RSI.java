package com.idk.coin.indicator;

import java.util.List;
import java.util.Stack;

import com.idk.coin.upbit.Candle;

public class RSI {

	 public static double[] calculateRSIValues(Candle[] candlesticks, int n){

	        double[] results = new double[candlesticks.length];

	        double ut1 = 0;
	        double dt1 = 0;
	        for(int i = 0; i < candlesticks.length; i++){
	            if(i<(n)){
	                continue;
	            }

	            ut1 = calcSmmaUp(candlesticks, n, i, ut1);
	            dt1 = calcSmmaDown(candlesticks, n, i, dt1);

	          // results[i] = 100.0 - 100.0 / (1.0 +   calculateRS(ut1,  dt1));
	                 
	                              

	        }

	        return results;
	    }
	 
	public static double calcSmmaUp(Candle[] candlesticks, double n, int i, double avgUt1){

        if(avgUt1==0){
            double sumUpChanges = 0;

            for(int j = 0; j < n; j++){
                double change = candlesticks[i - j].getTrade_price().doubleValue() - candlesticks[i - j].getOpening_price().doubleValue();

                if(change > 0){
                    sumUpChanges+= change;
                }
            }
            return sumUpChanges / n;
        }else {
            double change = candlesticks[i].getTrade_price().doubleValue()  - candlesticks[i].getOpening_price().doubleValue() ;
            if(change < 0){
               change = 0;
            }
            return ((avgUt1 * (n-1)) + change) / n ;
        }

    }

    public static double calcSmmaDown(Candle[] candlesticks, double n, int i, double avgDt1){
        if(avgDt1==0){
            double sumDownChanges = 0;

            for(int j = 0; j < n; j++){
                double change = candlesticks[i - j].getTrade_price().doubleValue() - candlesticks[i - j].getOpening_price().doubleValue();

                if(change < 0){
                    sumDownChanges-= change;
                }
            }
            return sumDownChanges / n;
        }else {
            double change = candlesticks[i].getTrade_price().doubleValue() - candlesticks[i].getOpening_price().doubleValue();
            if(change > 0){
                change = 0;
            }
            return ((avgDt1 * (n-1)) - change) / n ;
        }

    }
    
   
    public static double calculate(List<Candle> data) { 
     Stack<Averages> avgList = new Stack<>();
     double value; 
    	    
     int periodLength = 14;
     int qhSize = data.size(); 
     int lastBar = qhSize - 1; 
     int firstBar = lastBar - periodLength + 1; 
    
     double gains = 0, losses = 0, avgUp = 0, avgDown = 0; 
    
     double delta = data.get(lastBar).getTrade_price().doubleValue()  - data.get(lastBar - 1).getTrade_price().doubleValue(); 
      
     gains = Math.max(0, delta); 
     losses = Math.max(0, -delta); 
    
     if (avgList.isEmpty()) { 
      for (int bar = firstBar + 1; bar <= lastBar; bar++) { 
       double change = data.get(bar).getTrade_price().doubleValue()
         - data.get(bar - 1).getTrade_price().doubleValue(); 
       gains += Math.max(0, change); 
       losses += Math.max(0, -change); 
      } 
      avgUp = gains / periodLength; 
      avgDown = losses / periodLength; 
      avgList.push(new Averages(avgUp, avgDown)); 
    
     } else { 
    
      Averages avg = avgList.pop(); 
      avgUp = avg.getAvgUp(); 
      avgDown = avg.getAvgDown(); 
      avgUp = ((avgUp * (periodLength - 1)) + gains) / (periodLength); 
      avgDown = ((avgDown * (periodLength - 1)) + losses) 
        / (periodLength); 
      avgList.add(new Averages(avgUp, avgDown)); 
     } 
     value = 100 - (100 / (1 + (avgUp / avgDown))); 
    
     return value; 
    } 
    
     
}
