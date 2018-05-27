package com.anenha.weather.app.provider.yahooWeather

import com.anenha.weather.app.model.YahooResponseModel
import com.anenha.weather.app.provider.Endpoint
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
class YahooWeatherService(private val callback: WeatherServiceCallback) : Callback<YahooResponseModel> {
    private var local: String? = null

    fun refreshWeather(l: String) {
        this.local = l
        val YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", local)

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(Endpoint.YAHOO_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val endpoint = retrofit.create(Endpoint::class.java)

        val call = endpoint.loadWeather(YQL, "json")
        call.enqueue(this)
    }

    override fun onResponse(call: Call<YahooResponseModel>, response: Response<YahooResponseModel>) {
        if (response.isSuccessful) {
            val yahooResponseModel = response.body()

            if (yahooResponseModel!!.query!!.count == 0) {
                callback.serviceFailure(LocationWeatherException("No weather information found for " + local!!))
                return
            }
            callback.serviceSuccess(yahooResponseModel.query!!.results!!.channel!!)

        } else {
            callback.serviceFailure(Exception())
        }

    }

    override fun onFailure(call: Call<YahooResponseModel>, t: Throwable) {
        callback.serviceFailure(Exception())
    }

    inner class LocationWeatherException(detailMessage: String) : Exception(detailMessage)
}
