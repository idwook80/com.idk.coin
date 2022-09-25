package com.idk.coin.bybit;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.websocket.ClientEndpoint;

import com.alibaba.fastjson.JSON;
import com.idk.coin.CoinConfig;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ClientEndpoint
public class BybitClient {
	
	 /**
     * POST: place an active linear perpetual order
     */
    public static  String post(String url, Map map) throws NoSuchAlgorithmException, InvalidKeyException {
        // use json request body to send the request
        String jsonMap = JSON.toJSONString(map);
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body= RequestBody.create(mediaType, jsonMap);
        Request request = new Request.Builder()
                .url(url)
                .method("post", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            assert response.body() != null;
            return response.body().string();
            //System.out.println(response.body().string());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String get(String url) throws NoSuchAlgorithmException, InvalidKeyException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            assert response.body() != null;
            return response.body().string();
            //System.out.println(response.body().string());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * GET: get the active order list
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String get(String url, Map map) throws NoSuchAlgorithmException, InvalidKeyException {
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Object> e : entries ) {
            sb.append(e.getKey())
                    .append("=")
                    .append(e.getValue())
                    .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url+"?" + sb)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            assert response.body() != null;
            return response.body().string();
            //System.out.println(response.body().string());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * To generate the signature with all your parameters
     * @param params: TreeMap input parameters exclude "sign"
     * @return signature used to be a parameter in the request body
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String genSign(Map<String, Object> params) throws NoSuchAlgorithmException, InvalidKeyException {
        Set<String> keySet = params.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key)
                .append("=")
                .append(params.get(key))
                .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(System.getProperty(CoinConfig.BYBIT_SECRET).getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return bytesToHex(sha256_HMAC.doFinal(sb.toString().getBytes()));
    }

    /**
     * To convert bytes to hex
     * @param hash
     * @return hex string
     */
    public  static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    
}
