package com.anenha.weather.app.provider;

import com.anenha.weather.app.model.YahooResponseModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ana.j.saragossa on 20/04/2015.
 */
public class YahooWeatherService implements Callback<YahooResponseModel> {
    private WeatherServiceCallback callback;
    private String local;
    private static final String BASE_URL = "https://query.yahooapis.com";

    public YahooWeatherService (WeatherServiceCallback callback){
        this.callback = callback;
    }

    public void refreshWeather(String l){
        this.local = l;
        final String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", local);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Endpoint endpoint = retrofit.create(Endpoint.class);

        Call<YahooResponseModel> call = endpoint.loadWeather(YQL, "json");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<YahooResponseModel> call, Response<YahooResponseModel> response) {
        if(response.isSuccessful()) {
            YahooResponseModel yahooResponseModel = response.body();

            if(yahooResponseModel.getQuery().getCount() == 0){
                callback.serviceFailure(new LocationWeatherException("No weather information found for "+local));
                return;
            }
            callback.serviceSuccess(yahooResponseModel.getQuery().getResults().getChannel());

        } else {
            callback.serviceFailure( new Exception() );
        }

    }

    @Override
    public void onFailure(Call<YahooResponseModel> call, Throwable t) {
        callback.serviceFailure( new Exception() );
    }

    public class LocationWeatherException extends Exception{

        public LocationWeatherException(String detailMessage){
            super(detailMessage);
        }
    }
}
