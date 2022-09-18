package com.idk.coin.binance.trade;
import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.enums.TimeInForce;
import com.binance.client.model.trade.Order;
import com.idk.coin.CoinConfig;
import com.idk.coin.binance.PrivateConfig;


public class PostOrder {
	
	public static void main(String[] args) {
       
       
//        System.out.println(syncRequestClient.postOrder("BTCUSDT", OrderSide.SELL, PositionSide.BOTH, OrderType.LIMIT, TimeInForce.GTC,
//                "1", "1", null, null, null, null));

        // place dual position side order.
        // Switch between dual or both position side, call: com.binance.client.examples.trade.ChangePositionSide
    }
	
	RequestOptions options = new RequestOptions();
	SyncRequestClient syncRequestClient;
	public PostOrder() {
		CoinConfig.loadConfig();
		
		 syncRequestClient = SyncRequestClient.create(System.getProperty(CoinConfig.BINANCE_KEY), System.getProperty(CoinConfig.BINANCE_SECRET), options);
		
	}
	public void sell() {
		Order order = syncRequestClient.postOrder(
				"BTCUSDT", 
				OrderSide.SELL, 
				PositionSide.SHORT, 
				OrderType.LIMIT, 
				TimeInForce.GTC,
                "1",
                "9000",
                null, null, null, null,
                NewOrderRespType.RESULT);
	}
	public void buy() {
		Order order = syncRequestClient.postOrder(
				"BTCUSDT", 
				OrderSide.BUY, 
				PositionSide.LONG, 
				OrderType.LIMIT, 
				TimeInForce.GTC,
                "1",
                "9000",
                null, null, null, null,
                NewOrderRespType.RESULT);
	}
}
