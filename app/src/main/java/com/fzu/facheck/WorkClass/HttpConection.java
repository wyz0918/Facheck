package com.fzu.facheck.WorkClass;

import android.util.Log;

import org.json.JSONArray;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConection {
    public static final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    public static void sendGetRequest(String address,okhttp3.Callback callback)
    {
        OkHttpClient client =new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).build();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendPostRequest(String address,String data,okhttp3.Callback callback)
    {
        OkHttpClient client =new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS).build();
        RequestBody body=RequestBody.create(JSON,data);
        Log.d("HttpConection",body.toString());
        Request request=new Request.Builder().url(address).post(body).build();
        client.newCall(request).enqueue(callback);
    }
}
