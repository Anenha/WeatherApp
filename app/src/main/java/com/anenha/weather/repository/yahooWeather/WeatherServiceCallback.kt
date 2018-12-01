package com.anenha.weather.repository.yahooWeather


import com.anenha.weather.model.ChannelModel

/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
interface WeatherServiceCallback {
    fun serviceSuccess(channel: ChannelModel)
    fun serviceFailure(exception: Exception)
}
