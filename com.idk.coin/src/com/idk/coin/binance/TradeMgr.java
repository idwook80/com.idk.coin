package com.idk.coin.binance;

import java.util.List;

import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SubscriptionListener;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.enums.WorkingType;
import com.binance.client.model.event.SymbolTickerEvent;
import com.binance.client.model.market.ExchangeInformation;
import com.binance.client.model.trade.AccountBalance;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.MyTrade;
import com.binance.client.model.trade.Order;
import com.binance.client.model.trade.Position;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.idk.coin.CoinConfig;

public class TradeMgr {
	public final static String SYM_BTCUSDT = "BTCUSDT";
	
	String API_KEY = "";
	String API_SECRET = "";
	public static void main(String... args) {
		TradeMgr tr = new TradeMgr();
		//tr.getAccountInformation();
		//tr.getPositionSide();
		//tr.getPositionRisk();
		//tr.getAccountTrades();
		//tr.getBalance();
		//tr.orderTest();
		//tr.getOpenOrder();
		tr.subscriptionClient();
	}
	
	RequestOptions options = new RequestOptions();
	SyncRequestClient syncRequestClient;
	public TradeMgr() {
		 CoinConfig.loadConfig();
		 API_KEY = System.getProperty(CoinConfig.BINANCE_KEY);
		 API_SECRET = System.getProperty(CoinConfig.BINANCE_SECRET);
		 
		 syncRequestClient = SyncRequestClient.create(API_KEY, API_SECRET, options);
	}
	public void getAccountInformation () {
		AccountInformation ai = syncRequestClient.getAccountInformation();
		List<Position> positions = ai.getPositions();
		for(Position p : positions) {
			if(p.getSymbol().equals("BTCUSDT")){
				System.out.println(p);
			}
		}
		for(;;) {
			ai = syncRequestClient.getAccountInformation();
			System.out.println(ai.getTotalMarginBalance()+ " , " +ai.getTotalWalletBalance() + " , " + ai.getTotalUnrealizedProfit() );
			System.out.println(ai.getTotalMarginBalance().doubleValue() + ai.getTotalUnrealizedProfit().doubleValue());
			System.out.println(ai.getTotalWalletBalance().doubleValue() + ai.getTotalUnrealizedProfit().doubleValue());
			try {
				Thread.sleep(1000*3);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void getAccountTrades() {
		
		 List<MyTrade>  list = syncRequestClient.getAccountTrades("BTCUSDT", null, null, null, null);
		 for(MyTrade l : list) {
			 System.out.println(l.toString());
		 }
	}
	public void getBalance() {
		 List<AccountBalance> list = syncRequestClient.getBalance();
		 for(AccountBalance b : list) {
			 System.out.println(b.toString());
		 }
		 
	}
	public void getOpenOrder() {
		   List<Order> list = syncRequestClient.getOpenOrders("BTCUSDT");
		   for(Order l : list) {
			   System.out.println(l.toString());
		   }
	}
	public void getOrder() {
	}
	public void getPositionSide() {
		JsonParser parser = new JsonParser();
        JsonElement el =  parser.parse(syncRequestClient.getPositionSide().toString());
        //System.out.println(el);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(el));
	}
	
	public void getPositionRisk() {
		System.out.println(syncRequestClient.getPositionRisk());
	}
	
	public void orderTest() {
		//openLongOrder(SYM_BTCUSDT, "0.001", "19710");
		//openShortOrder(SYM_BTCUSDT, "0.001", "20010");
		//closeLongOrder(SYM_BTCUSDT, "0.001", "20010");
		//closeShortOrder(SYM_BTCUSDT, "0.001", "19710");
		//closeShortOrder("BTCUSDT", "0.001", "16910");
	}
	public void postOrder(String symbol,OrderSide side,PositionSide positionSide, String quantity, String price) {
		OrderType 		orderType 			= OrderType.LIMIT;
        TimeInForce 	timeInForce 		= TimeInForce.GTC;
        String 			reduceOnly 			= null;
        String 			newClientOrderId 	= null;
        String 			stopPrice 			= null;
        WorkingType 	workingType 		= null;
        NewOrderRespType newOrderRespType 	= NewOrderRespType.RESULT;
        
        Order order = syncRequestClient.postOrder(symbol, side, positionSide, orderType, timeInForce,
                quantity, price, reduceOnly, newClientOrderId, stopPrice, workingType, NewOrderRespType.RESULT);
        System.out.println(order);
		/* syncRequestClient.postOrder("BTCUSDT", OrderSide.SELL, PositionSide.SHORT, OrderType.LIMIT, TimeInForce.GTC,
	                "1", "9000", null, null, null, null, NewOrderRespType.RESULT);*/
	}
	public void openLongOrder(String symbol,String quantity, String price) {
		postOrder(symbol,OrderSide.BUY, PositionSide.LONG, quantity, price);
	}
	public void openShortOrder(String symbol, String quantity, String price) {
		postOrder(symbol,OrderSide.SELL, PositionSide.SHORT, quantity, price);
	}
	public void closeLongOrder(String symbol, String quantity, String price) {
		postOrder(symbol, OrderSide.SELL, PositionSide.LONG, quantity, price);
	}
	public void closeShortOrder(String symbol, String quantity, String price) {
		postOrder(symbol, OrderSide.BUY, PositionSide.SHORT, quantity, price);
	}
	
	
	public void subscriptionClient() {
	        // Start user data stream
	        String listenKey = syncRequestClient.startUserDataStream();
	        System.out.println("listenKey: " + listenKey);

	        // Keep user data stream
	        syncRequestClient.keepUserDataStream(listenKey);

	        // Close user data stream
	        syncRequestClient.closeUserDataStream(listenKey);

	        SubscriptionClient client = SubscriptionClient.create();

	       // client.subscribeSymbolTickerEvent("btcusdt", System.out::println, null);
	        //client.subscribeUserDataEvent(listenKey, System.out::println, null);
	        client.subscribeSymbolTickerEvent("btcusdt", new SubscriptionListener<SymbolTickerEvent>() {
				
				@Override
				public void onReceive(SymbolTickerEvent data) {
					// TODO Auto-generated method stub
					System.out.println(data.getLastPrice().doubleValue());
					
				}
			}, null);
	}
}
