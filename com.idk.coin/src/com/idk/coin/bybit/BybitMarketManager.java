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
	public MarketModel createMarket(String symbol, String api_key, String api_secret) {
		MarketModel m = getMarketModel(symbol);
		if(m == null) {
			m = new MarketModel(symbol, api_key, api_secret);
			addMarketModel(m);
		}
		return m;
	}
	public void startAllMarkets() {
		synchronized (markets) {
			for(int i=0; i<markets.size(); i++) {
				MarketModel m = markets.get(i);
				m.startMarket();
			}
		}
	}
}
