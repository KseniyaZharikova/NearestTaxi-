package com.example.kseniya.nearesttaxi.data;

import com.example.kseniya.nearesttaxi.models.Main;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitService {


    @GET("/nearest/{lat}/{lon}")
    Call<Main> getInformationTaxi(@Path("lat") double lat,
                                  @Path("lon") double lon);
}
