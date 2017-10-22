package com.anenha.weather.app.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.anenha.weather.R;
import com.anenha.weather.app.entity.TodayEntity;
import com.anenha.weather.app.model.Channel;
import com.anenha.weather.app.model.Favorite;
import com.anenha.weather.app.entity.FavoritesEntity;
import com.anenha.weather.app.model.Favorites;
import com.anenha.weather.app.provider.WeatherServiceCallback;
import com.anenha.weather.app.provider.YahooWeatherService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajnen on 22/10/2017.
 */

public class Prefs {

    public interface PrefsCallback {
        void onAddCity(String city, FavoritesEntity fe);
    }
    public interface UpdateCallback {
        void onUpdate(FavoritesEntity fe);
    }

    public static void addCityDialog(final Context context, final PrefsCallback callback){

        final View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_dialog_input, null, false);


        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(R.string.add_city_dialog_positive_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String city =((EditText) view.findViewById(R.id.dialog_input)).getText().toString();
                        saveCity(context, city, callback);

                    }
                })
                .setNegativeButton(context.getString(R.string.add_city_dialog_negative_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public static List<String> getFavorites(Context context){
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String favorites = sharedPref.getString(context.getString(R.string.pref_favorites_key), null);
        if(favorites == null){
            return new ArrayList<>();
        }
        return new Gson().fromJson(favorites, Favorites.class).getCities();
    }

    public static void updateFavorites(final Context context, final List<String> cities, final UpdateCallback callback){
        YahooWeatherService yahooService;
        final List<Favorite> favorites = new ArrayList<>();
        if(!cities.isEmpty()) {
            for (int pos = 0; pos < cities.size(); pos++) {
                final String city = cities.get(pos);
                final int position = pos;
                yahooService = new YahooWeatherService(new WeatherServiceCallback() {
                    @Override
                    public void serviceSucess(Channel channel) {
                        addFavorite(context, favorites, position, cities, city, channel, callback);
                    }

                    @Override
                    public void serviceFailure(Exception exception) {
                        addFavorite(context, favorites, position, cities, city, null, callback);
                    }
                });

                yahooService.refreshWeather(city);
            }
        } else {
            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(context.getString(R.string.pref_favorites_key), new Gson().toJson(new Favorites(cities)));
            editor.apply();
            callback.onUpdate(null);
        }
    }

    private static void addFavorite(final Context context, final List<Favorite> favorites,
                                    final Integer position, final List<String> cities, final String city, final Channel channel, final UpdateCallback callback){
        favorites.add(new Favorite(city, new TodayEntity(context, channel)));
        if(position == cities.size()-1){
            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            FavoritesEntity fe = new FavoritesEntity(favorites);
            editor.putString(context.getString(R.string.pref_favorites_key), new Gson().toJson(new Favorites(cities)));
            editor.apply();
            callback.onUpdate(fe);
        }
    }

    private static void saveCity(final Context context, final String city, final PrefsCallback callback){
        List<String> cities = getFavorites(context);
        cities.add(city);
        updateFavorites(context, cities, new UpdateCallback() {
            @Override
            public void onUpdate(FavoritesEntity fe) {
                callback.onAddCity(city, fe);
            }
        });

    }
}
