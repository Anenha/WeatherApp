package com.anenha.weather.app.provider.yahooWeather


import com.anenha.weather.app.model.Channel

/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
interface WeatherServiceCallback {
    fun serviceSuccess(channel: Channel)
    fun serviceFailure(exception: Exception)
}
