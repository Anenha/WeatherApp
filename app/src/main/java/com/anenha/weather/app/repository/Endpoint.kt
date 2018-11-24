package com.anenha.weather.app.repository

import com.anenha.weather.app.model.TranslateResponseModel
import com.anenha.weather.app.model.YahooResponseModel

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ajnen on 12/10/2017.
 */

interface Endpoint {

    @GET("v1/public/yql")
    fun loadWeather(@Query("q") query: String, @Query("format") format: String): Call<YahooResponseModel>

    @GET("api/translate")
    fun translate(@Query("query") query: String, @Query("targetLang") targetLang: String,
                  @Query("sourceLang") sourceLang: String): Call<TranslateResponseModel>

    companion object {
        const val YAHOO_URL = "https://query.yahooapis.com/"
        const val TRANSLATE_URL = "https://google-translate-proxy.herokuapp.com/"   //https://github.com/guyrotem/google-translate-server
    }

}