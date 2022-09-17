package com.idk.coin.binance;

import java.util.List;

import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.enums.WorkingType;
import com.binance.client.model.trade.AccountBalance;
import com.binance.client.model.trade.MyTrade;
import com.binance.client.model.trade.Order;

public class TradeMgr {
	public final static String SYM_BTCUSDT = "BTCUSDT";
	
	
	public static void main(String... args) {
		TradeMgr tr = new TradeMgr();
		tr.getAccountInformation();
		tr.getPositionSide();
		//tr.getPositionRisk();
		tr.getAccountTrades();
		tr.getBalance();
		//tr.orderTest();
		tr.getOpenOrder();
		//tr.subscriptionClient();
	}
	
	RequestOptions options = new RequestOptions();
	SyncRequestClient syncRequestClient;
	public TradeMgr() {
		 syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);
	}
	public void getAccountInformation () {
		System.out.println(syncRequestClient.getAccountInformation());
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
		 System.out.println(syncRequestClient.getPositionSide());
	}
	
	public void getPositionRisk() {
		System.out.println(syncRequestClient.getPositionRisk());
	}
	
	public void orderTest() {
		//openLongOrder(SYM_BTCUSDT, "0.001", "19710");
		//openShortOrder(SYM_BTCUSDT, "0.001", "20010");
		//closeLongOrder(SYM_BTCUSDT, "0.001", "20010");
		//closeShortOrder(SYM_BTCUSDT, "0.001", "19710");
	}
	public void postOrder(String symbol,OrderSide side,PositionSide positionSide, String quantity, String price) {
		OrderType 		orderType 			= OrderType.LIMIT;
        TimeInForce 	timeInForce 		= TimeInForce.GTC;
        String 			reduceOnly 			= null;
        String 			newClientOrderId 	= null;
        String 			stopPrice 			= null;
        WorkingType 	workingType 		= null;
        NewOrderRespType newOrderRespType 	= NewOrderRespType.RESULT;
        
        syncRequestClient.postOrder(symbol, side, positionSide, orderType, timeInForce,
                quantity, price, reduceOnly, newClientOrderId, stopPrice, workingType, NewOrderRespType.RESULT);
        
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

	   
	        client.subscribeUserDataEvent(listenKey, System.out::println, null);
	}
}
