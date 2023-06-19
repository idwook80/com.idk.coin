package com.idk.coin.bybit;

import java.util.ArrayList;

import com.idk.coin.bybit.model.MarketModel;

public class BybitMarketManager {
	ArrayList<MarketModel> markets ;
	
	public BybitMarketManager() {
		markets = new ArrayList<>();
	}
	public void addMarketModel(MarketModel m) {
		synchronized (markets) {
			markets.add(m);
		}
	}
	public void removeMarketModel(MarketModel m) {
		synchronized (markets) {
			markets.remove(m);
		}
	}
	public MarketModel getMarketModel(String symbol) {
		synchronized (markets) {
			for(int i=0; i<markets.size(); i++) {
				MarketModel m = markets.get(i);
				if(m.getSymbol().equals(symbol)) {
					return m;
				}
			}
		}
		return null;
	}
	public MarketModel createMarket(String symbol, String api_key, String api_secret, boolean debug,int debug_count) {
		
		MarketModel m = getMarketModel(symbol);
		if(m == null) {
			m = new MarketModel(symbol, api_key, api_secret, debug, debug_count);
			addMarketModel(m);
		}
		return m;
	}
	public MarketModel createMarket(String symbol, String api_key, String api_secret, boolean debug) {
		return createMarket(symbol, api_key, api_secret, debug, 10);
	}
	public void startMarket(String symbol) {
		synchronized (markets) {
			for(int i=0; i<markets.size(); i++) {
				MarketModel m = markets.get(i);
				if(m.getSymbol().equals(symbol)) {
					m.startMarket();
				}
			}
		}
	}
	public void startAllMarkets() {
		synchronized (markets) {
			for(int i=0; i<markets.size(); i++) {
				MarketModel m = markets.get(i);
				m.startMarket();
			}
		}
	}
	public void stopAllMarkets() {
		synchronized (markets) {
			for(int i=0; i<markets.size(); i++) {
				MarketModel m = markets.get(i);
				m.stopMarket();
				
			}
		}
	}
	public void stopMarket(String symbol) {
		synchronized (markets) {
			for(int i=0; i<markets.size(); i++) {
				MarketModel m = markets.get(i);
				if(m.getSymbol().equals(symbol)) {
					m.stopMarket();
				}
			}
		}
	}
}
