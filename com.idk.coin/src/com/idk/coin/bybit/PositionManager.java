package com.idk.coin.bybit;

import java.util.ArrayList;

import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.account.PositionRest;
import com.idk.coin.bybit.model.Position;

public class PositionManager {
	ArrayList<Position> positions = new ArrayList<Position>();
	static String API_KEY = "";
    static String API_SECRET = "";
    Position btcBuyPosition;
    Position btcSellPosition;
    
    BybitMain main;
    
    
    public static void main(String[] args) {
    	PositionManager pm = new PositionManager(null);
    	pm.changedPositions();
    	
    	Position p = pm.getPosition("BTCUSDT", "Buy");
    	System.out.println(p);
    }
    
	public PositionManager(BybitMain main) {
		this.main = main;
		CoinConfig.loadConfig();
    	API_KEY 	= System.getProperty(CoinConfig.BYBIT_KEY);
    	API_SECRET 	= System.getProperty(CoinConfig.BYBIT_SECRET);
    	changedPositions();
    	
	}
	public boolean removePosition(Position p) {
		if(p == null) return false;
		synchronized (p) {
			return positions.remove(p);
		}
	}
	public void addPosition(Position p) {
		synchronized(positions) {
			positions.add(p);
		}
	}
	public void setPosition(Position p) {
		String symbol 	= p.getSymbol();
		String side 	= p.getSide();
		Position old = getPosition(symbol, side);
		removePosition(old);
		addPosition(p);
	}
	public Position getPosition(String symbol, String side) {
		synchronized(positions) {
			Position[] ps = positions.toArray(new Position[0]);
			for(Position p : ps) {
				if(p.getSymbol().equals(symbol) && 
					p.getSide().equals(side)) return p;
			}
		}
		return null;
	}
	
	public void changedPositions() {
		System.out.println("################## changed positions ##############");
		try {
				ArrayList<Position> ps = PositionRest.getActiveMyPosition("BTCUSDT", API_KEY);
				if(ps == null) return;
				for(Position p : ps) {
					setPosition(p);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		btcBuyPosition = getPosition("BTCUSDT", "Buy");
		btcSellPosition = getPosition("BTCUSDT", "Sell");
	}
	
	public Position getBtcBuyPosition(){
		return btcBuyPosition;
	}
	public Position getBtcSellPosition() {
		return btcSellPosition;
	}
}
