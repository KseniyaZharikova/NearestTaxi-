package com.example.kseniya.nearesttaxi;

import android.app.Application;
import android.content.Context;

import com.example.kseniya.nearesttaxi.data.NetworkBuilder;
import com.example.kseniya.nearesttaxi.data.RetrofitService;


public class TaxiApplication extends Application {
    private RetrofitService service;
    @Override
    public void onCreate() {
        super.onCreate();
        service = NetworkBuilder.initService();
    }
    public static   TaxiApplication get(Context  context ){
        return (TaxiApplication) context.getApplicationContext();
    }
    public  RetrofitService  getService(){
        return service;
    }

}
