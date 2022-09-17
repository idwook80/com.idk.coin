package com.idk.coin.indicator;

public class Averages {
	private final double avgUp; 
	  private final double avgDown; 
	 
	  public Averages(final double up, final double down) { 
	   this.avgDown = down; 
	   this.avgUp = up; 
	  } 
	 
	  public double getAvgUp() { 
	   return avgUp; 
	  } 
	 
	  public double getAvgDown() { 
	   return avgDown; 
	  } 
}
