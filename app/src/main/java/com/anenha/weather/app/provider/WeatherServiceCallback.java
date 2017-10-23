package com.anenha.weather.app.provider;


import com.anenha.weather.app.model.Channel;

/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
