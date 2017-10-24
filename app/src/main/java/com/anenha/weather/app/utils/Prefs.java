package com.anenha.weather.app.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.anenha.weather.R;
import com.anenha.weather.app.entity.TodayEntity;
import com.anenha.weather.app.model.Channel;
import com.anenha.weather.app.model.Favorite;
import com.anenha.weather.app.entity.FavoritesEntity;
import com.anenha.weather.app.provider.yahooWeather.WeatherServiceCallback;
import com.anenha.weather.app.provider.yahooWeather.YahooWeatherService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajnen on 22/10/2017.
 *
 */

public class Prefs {

    public interface PrefsCallback { void onAddCity(String city); }
    public interface WeatherCallback { void onUpdate(FavoritesEntity fe); }

    public static String getInitialCity(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_city_key), context.getString(R.string.pref_default_display_city));
    }

    public static void addCityDialog(final Context context, final PrefsCallback callback){

        final View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_dialog_input, null, false);

        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(R.string.add_city_dialog_positive_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String city =((EditText) view.findViewById(R.id.dialog_input)).getText().toString();
                        addCity(context, city, callback);

                    }
                })
                .setNegativeButton(context.getString(R.string.add_city_dialog_negative_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public static void updateFavorites(final Context context, final List<String> cities){
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.pref_favorites_key), new Gson().toJson(cities));
        editor.apply();
    }

    public static void getFavoritesWeather(final Context context, final WeatherCallback callback){
        final List<String> cities = getFavorites(context);
        final List<Favorite> favorites = new ArrayList<>();
        if(!cities.isEmpty()) {
            YahooWeatherService yahooService;
            for (int pos = 0; pos < cities.size(); pos++) {
                final String city = cities.get(pos);
                yahooService = new YahooWeatherService(new WeatherServiceCallback() {
                    @Override
                    public void serviceSuccess(Channel channel) {
                        addFavorite(context, favorites, cities, city, channel, callback);
                    }

                    @Override
                    public void serviceFailure(Exception exception) {
                        addFavorite(context, favorites, cities, city, null, callback);
                    }
                });
                yahooService.refreshWeather(city);
            }
        } else {
            callback.onUpdate(null);
        }
    }

    private static ArrayList<String> getFavorites(Context context){
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String favorites = sharedPref.getString(context.getString(R.string.pref_favorites_key), null);
        if(favorites == null){
            return new ArrayList<>();
        }
        return new Gson().fromJson(favorites, (new ArrayList<String>()).getClass());
    }

    private static void addFavorite(final Context context, final List<Favorite> favorites,
                                    final List<String> cities, final String city, final Channel channel,
                                    final WeatherCallback callback){
        favorites.add(new Favorite(city, new TodayEntity(context, channel, new TodayEntity.TodayCallback() {
            @Override
            public void onCreate(TodayEntity te) {
                if(favorites.size() == cities.size()){
                    FavoritesEntity fe = new FavoritesEntity(favorites);
                    callback.onUpdate(fe);
                }
            }
        })));
    }

    private static void addCity(final Context context, final String city, final PrefsCallback callback){
        ArrayList<String> cities = getFavorites(context);
        cities.add(city);
        updateFavorites(context, cities);
        callback.onAddCity(city);
    }
}
