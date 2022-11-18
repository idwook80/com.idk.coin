package com.idk.coin.binance.example;
import com.binance.client.RequestOptions;
import com.binance.client.SubscriptionClient;
import com.binance.client.SyncRequestClient;
import com.idk.coin.CoinConfig;


public class UserDataStreamExample {
	public static void main(String[] args) {
		CoinConfig.loadConfig();
		String API_KEY = System.getProperty(CoinConfig.BINANCE_KEY);
		String  API_SECRET = System.getProperty(CoinConfig.BINANCE_SECRET);
		System.out.println(API_KEY + " , " + API_SECRET);
		   RequestOptions options = new RequestOptions();
	        SyncRequestClient syncRequestClient = SyncRequestClient.create(API_KEY, API_SECRET,
	                options);

	        // Start user data stream
	        String listenKey = syncRequestClient.startUserDataStream();
	        System.out.println("listenKey: " + listenKey);

	        // Keep user data stream
	        syncRequestClient.keepUserDataStream(listenKey);

	        // Close user data stream
	        //syncRequestClient.closeUserDataStream(listenKey);

	        SubscriptionClient client = SubscriptionClient.create();

	   
	        client.subscribeUserDataEvent(listenKey, System.out::println, null);
	        //client.subscribeAggregateTradeEvent("btcusdt", System.out::println, null);
	}
}