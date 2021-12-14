package com.cjj.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/***
 * author：caijj1
 * time：2018/5/25
 * mail:caijj1@chanjet.com
 **/
public class HttpUtil {

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
