package com.idk.coin.bybit;
import java.awt.Font;
import java.io.Console;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.idk.coin.CoinConfig;
import com.idk.coin.bybit.model.OrderExecution;

import io.contek.invoker.bybit.api.common._Position;
import okhttp3.internal.connection.RealConnection;

@ClientEndpoint
public class BybitExecution implements Runnable {
	
	   public static Logger LOG =   LoggerFactory.getLogger(BybitExecution.class.getName());
	   
	    String api_key = "";
	    String api_secret = "";
	    Session session;
	    Thread thread ;
	    Thread readThread;
	    BybitRealTime bybitRealTime;
	    public static void main(String[] args) {
	    	new BybitExecution(null);
	    }
	    
	    public BybitExecution(BybitRealTime manager) {
			// TODO Auto-generated constructor stub
	    	this.bybitRealTime = manager;
	    	 CoinConfig.loadConfig();
	    	 api_key 	= System.getProperty(CoinConfig.BYBIT_KEY);
	         api_secret = System.getProperty(CoinConfig.BYBIT_SECRET);
	         init();
	            
		}
	    public void run() {
	    	while(true) {
	    		 try {
	    			if(session == null) connect();
	    			if(!session.isOpen()) connect();
	 	            session.getBasicRemote().sendText(subscribe("subscribe", "execution"));
	 	            Thread.sleep(1000*60*5);
			        } catch ( Exception ex) {
			            ex.printStackTrace();
			        }
	    		  
	    	}
	    }
	    public void init() {
	    	  try {
		    		
		           // WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		           // String uri = "wss://stream.bybit.com/realtime_private";
		           // container.connectToServer(this, URI.create(uri));
		           /* session.getBasicRemote().sendText("{\"op\":\"ping\"}");
		            session.getBasicRemote().sendText(getAuthMessage());
		            session.getBasicRemote().sendText(subscribe("subscribe", "execution"));*/
		            thread = new Thread(this);
		            thread.start();
		            
		            readThread = new Thread() {
		            	public void run() {
		            		 try {
		 			            java.io.BufferedReader r = new  java.io.BufferedReader(new java.io.InputStreamReader( System.in));
		 			            while(true){
		 			                String line=r.readLine();
		 			                if(line.equals("quit")) break;
		 			                session.getBasicRemote().sendText(line);
		 			            }

		 			        } catch ( Exception ex) {
		 			            ex.printStackTrace();
		 			        }
		            	}
		            };
		            readThread.start();
		           
		            
		            
		        } catch ( Exception ex) {
		            ex.printStackTrace();
		        }
	    }
	    public void connect() {
	    	  try {
		    		
	    		  WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		            String uri = "wss://stream.bybit.com/realtime_private";
		            container.connectToServer(this, URI.create(uri));
		           session.getBasicRemote().sendText("{\"op\":\"ping\"}");
		            session.getBasicRemote().sendText(getAuthMessage());
		           // session.getBasicRemote().sendText(subscribe("subscribe", "execution"));
		        } catch ( Exception ex) {
		            ex.printStackTrace();
		        }
	    	  
	    }

	 @OnOpen
	    public void onOpen(Session session) {
	        System.out.println("Connected to endpoint: " + session.getBasicRemote());
	        try {
	            this.session=session;
	            System.out.println(BybitWssClient.session);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    @OnMessage
	    public void processMessage(String message) {
	        System.out.println("Received message in client: " + message);
	    	lineParsing(message);
	    }

	    @OnError
	    public void processError(Throwable t) {
	        t.printStackTrace();
	    }
	    public void lineParsing(String str) {
	    	JsonParser parser = new JsonParser();
	        JsonElement el =  parser.parse(str);
	       // System.out.println(el);
	        
	         
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        //System.out.println(gson.toJson(el));
	        Map<String, Object> map  = gson.fromJson(str, Map.class);
	        String topic = (String)map.get("topic");
	        if(topic == null || !topic.equals("execution")) return;
	        
	        System.out.println(gson.toJson(el));
	        ArrayList data = (ArrayList)map.get("data");
	        
	        for(int i=0; i<data.size(); i++) {
	       	 LinkedTreeMap t1 = (LinkedTreeMap)data.get(i);
	       	 
	       	 OrderExecution execution = new OrderExecution(t1);
	       	 System.out.println(execution);
	       	 notifyAlarm(execution);
	        }
	    }
	    
	    public String generate_signature(String expires){ 
	    	return sha256_HMAC("GET/realtime"+ expires, api_secret);
	    	
	    }
	    private String byteArrayToHexString(byte[] b) {
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

	    public String sha256_HMAC(String message, String secret) {
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

	    public String getAuthMessage(){
	        JSONObject req=new JSONObject();
	        req.put("op", "auth");
	        List<String> args = new LinkedList<String>();
	        String expires = String.valueOf(System.currentTimeMillis()+(1000*60*60));
	        args.add(api_key);
	        args.add(expires);
	        args.add(generate_signature(expires));
	        req.put("args", args);
	        return (req.toString());
	    }

	    public String subscribe(String op, String argv){
	        JSONObject req=new JSONObject();
	        req.put("op", op);
	        List<String> args = new LinkedList<String>();
	        args.add(argv);
	        req.put("args", args);
	        return req.toString();
	    }
	    
	    public synchronized void notifyAlarm(OrderExecution e) {
	    	bybitRealTime.eventExecution(e);
	    }
	    
	   
	 
}
