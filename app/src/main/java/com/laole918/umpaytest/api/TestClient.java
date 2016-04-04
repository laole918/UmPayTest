package com.laole918.umpaytest.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastjsonConverterFactory;

/**
 * Created by laole918 on 2016/3/29 0029.
 */
public class TestClient {

    public static final String HOST = "http://112.25.15.148:8080";

    private static TestApi testApi;
    private static final Object monitor = new Object();
    private static Retrofit retrofit;

    private TestClient() {

    }

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastjsonConverterFactory.create())
                .baseUrl(HOST)
                .build();
    }

    public static TestApi getTestApiInstance() {
        if(testApi == null) {
            synchronized (monitor) {
                if (testApi == null) {
                    testApi = retrofit.create(TestApi.class);
                }
            }
        }
        return testApi;
    }
}
