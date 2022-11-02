package com.idk.coin.bybit;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;

import com.idk.coin.CoinConfig;

public class BybitWssClient{


	    static String api_key = "";
	    static String api_secret = "";
	    static Session session;

	    public static String generate_signature(String expires){ 
	    	return sha256_HMAC("GET/realtime"+ expires, api_secret);
	    	
	    }
	    private static String byteArrayToHexString(byte[] b) {
	        StringBuilder hs = new StringBuilder();
	        String stmp;
	        for (int n = 0; b!=null && n < b.length; n++) {
	            stmp = Integer.toHexString(b[n] & 0XFF);
	            if (stmp.length() == 1)
	                hs.append('0');
	            hs.append(stmp);
	        }
	        return hs.toString().toLowerCase();
	    }

	    public static String sha256_HMAC(String message, String secret) {
	        String hash = "";
	        try {
	            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
	            sha256_HMAC.init(secret_key);
	            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
	            hash = byteArrayToHexString(bytes);
	        } catch (Exception e) {
	            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
	        }
	        return hash;

	    }

	    public static String getAuthMessage(){
	        JSONObject req=new JSONObject();
	        req.put("op", "auth");
	        List<String> args = new LinkedList<String>();
	        String expires = String.valueOf(System.currentTimeMillis()+(1000*60*60*24*365));
	        args.add(api_key);
	        args.add(expires);
	        args.add(generate_signature(expires));
	        req.put("args", args);
	        return (req.toString());
	    }

	    public static String subscribe(String op, String argv){
	        JSONObject req=new JSONObject();
	        req.put("op", op);
	        List<String> args = new LinkedList<String>();
	        args.add(argv);
	        req.put("args", args);
	        return req.toString();
	    }

	    public BybitWssClient(Object endpoint, String uri,String subscribe) {
	    	  CoinConfig.loadConfig();
	            api_key 	= System.getProperty(CoinConfig.BYBIT_KEY);
	            api_secret 	= System.getProperty(CoinConfig.BYBIT_SECRET);
	        	
	    	  try {
	    		 
		            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		           // String uri = "wss://stream.bybit.com/realtime_public";
		            container.connectToServer(endpoint, URI.create(uri));
		            session.getBasicRemote().sendText("{\"op\":\"ping\"}");
		            session.getBasicRemote().sendText(getAuthMessage());
		            session.getBasicRemote().sendText(subscribe("subscribe", subscribe));
//		            session.getBasicRemote().sendText(subscribe("subscribe", "order"));
		            java.io.BufferedReader r=new  java.io.BufferedReader(new java.io.InputStreamReader( System.in));
		            while(true){
		                String line=r.readLine();
		                if(line.equals("quit")) break;
		                System.out.println("call line " + line);
		                session.getBasicRemote().sendText(line);
		            }

		        } catch ( Exception ex) {
		            ex.printStackTrace();
		        }
	    }
	    public static void main(String[] args) {
	    	Thread thread = new Thread() {
	    		public void run() {
	    			 BybitWebsocket websocket = new BybitWebsocket();
	    			new BybitWssClient(websocket,  "wss://stream.bybit.com/realtime_public" , "trade.BTCUSDT");
	    		}
	    	};
	    	Thread thread2 = new Thread() {
	    		public void run() {
	    		//BybitExecution exSocket = new BybitExecution(null);
	    		//	new BybitWssClient(exSocket, "wss://stream.bybit.com/realtime_private" , "execution");
	    		}
	    	};
	    	
	    	thread.start();
	    	//thread2.start();
	    	 
	    	
	    
	    }
	   /* public static void main(String[] args) {

	        try {
	            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
	            String uri = "wss://stream.bybit.com/realtime_public";
//	            String uri = "wss://stream.bytick.com/realtime";
//	            String uri = "wss://stream-testnet.bybit-cn.com/realtime";
//	            String uri = "wss://stream-testnet.bybit.com/realtime";
	            CoinConfig.loadConfig();
	        	
	            api_key 	= System.getProperty(CoinConfig.BYBIT_KEY);
	            api_secret 	= System.getProperty(CoinConfig.BYBIT_SECRET);
	        	
	            container.connectToServer(BybitWebsocket.class, URI.create(uri));
	            session.getBasicRemote().sendText("{\"op\":\"ping\"}");
	            session.getBasicRemote().sendText(getAuthMessage());
	            session.getBasicRemote().sendText(subscribe("subscribe", "trade.BTCUSDT"));
//	            session.getBasicRemote().sendText(subscribe("subscribe", "order"));
	            java.io.BufferedReader r=new  java.io.BufferedReader(new java.io.InputStreamReader( System.in));
	            while(true){
	                String line=r.readLine();
	                if(line.equals("quit")) break;
	                session.getBasicRemote().sendText(line);
	            }

	        } catch ( Exception ex) {
	            ex.printStackTrace();
	        }
	    }*/
	}
