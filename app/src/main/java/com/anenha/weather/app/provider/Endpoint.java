package com.anenha.weather.app.provider;

import com.anenha.weather.app.model.YahooResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ajnen on 12/10/2017.
 */

public interface Endpoint {
    @GET("v1/public/yql")
    Call<YahooResponseModel> loadWeather(@Query("q") String query, @Query("format") String format);}