package com.example.kseniya.nearesttaxi.data;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class NetworkBuilder {
    private  static RetrofitService service = null;
    public  static RetrofitService initService (){
        if (service==null) {
            service = new Retrofit.Builder()
                    .baseUrl("http://openfreecabs.org")
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RetrofitService.class);
        }
        return service;
    }
    private static OkHttpClient  getClient(){
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request()
                                .newBuilder()
                                .addHeader("Accept", "application/json;versions=1");
                        return chain.proceed(ongoing.build());

                    }
                }).build();

    }


}
