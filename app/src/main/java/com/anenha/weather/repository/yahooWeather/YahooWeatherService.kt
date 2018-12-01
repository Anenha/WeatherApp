package com.anenha.weather.repository.yahooWeather

import com.anenha.weather.BuildConfig
import com.anenha.weather.model.YahooResponseModel
import com.anenha.weather.repository.Endpoint
import com.google.gson.GsonBuilder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
class YahooWeatherService(private val callback: WeatherServiceCallback) : Callback<YahooResponseModel> {
    private var local: String = ""
    private val endpoint: Endpoint

    init {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

        if(BuildConfig.DEBUG){
            client.addInterceptor(logging)
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(Endpoint.YAHOO_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build()

        endpoint = retrofit.create(Endpoint::class.java)
    }

    fun refreshWeather(l: String) {
        this.local = l
        val query = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", local)

        val call = endpoint.loadWeather(query, "json")
        call.enqueue(this)
    }

    override fun onResponse(call: Call<YahooResponseModel>, response: Response<YahooResponseModel>) {
        if (response.isSuccessful) {
            val yahooResponseModel = response.body()

            if (yahooResponseModel!!.query!!.count == 0) {
                callback.serviceFailure(LocationWeatherException("No weather information found for $local"))
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
