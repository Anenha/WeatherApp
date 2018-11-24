package com.anenha.weather.app.repository.yahooWeather


import com.anenha.weather.app.model.ChannelModel

/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
interface WeatherServiceCallback {
    fun serviceSuccess(channel: ChannelModel)
    fun serviceFailure(exception: Exception)
}
