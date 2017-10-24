package com.anenha.weather.app.provider;

import com.anenha.weather.app.model.TranslateResponseModel;
import com.anenha.weather.app.model.YahooResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ajnen on 12/10/2017.
 */

public interface Endpoint {

    String YAHOO_URL = "https://query.yahooapis.com";
    String TRANSLATE_URL = "https://google-translate-proxy.herokuapp.com";   //https://github.com/guyrotem/google-translate-server

    @GET("v1/public/yql")
    Call<YahooResponseModel> loadWeather(@Query("q") String query, @Query("format") String format);

    @GET("api/translate")
    Call<TranslateResponseModel> translate(@Query("query") String query, @Query("targetLang") String targetLang,
                                           @Query("sourceLang") String sourceLang);

}