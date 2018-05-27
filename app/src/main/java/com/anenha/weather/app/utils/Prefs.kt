package com.anenha.weather.app.utils

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText

import com.anenha.weather.R
import com.anenha.weather.app.entity.TodayEntity
import com.anenha.weather.app.model.Channel
import com.anenha.weather.app.model.Favorite
import com.anenha.weather.app.entity.FavoritesEntity
import com.anenha.weather.app.provider.yahooWeather.WeatherServiceCallback
import com.anenha.weather.app.provider.yahooWeather.YahooWeatherService
import com.google.gson.Gson

import java.util.ArrayList

/**
 * Created by ajnen on 22/10/2017.
 *
 */

object Prefs {

    interface PrefsCallback {
        fun onAddCity(city: String)
    }

    interface WeatherCallback {
        fun onUpdate(fe: FavoritesEntity?)
    }

    fun getInitialCity(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_city_key), context.getString(R.string.pref_default_display_city))
    }

    fun addCityDialog(context: Context, callback: PrefsCallback) {

        val view = LayoutInflater.from(context)
                .inflate(R.layout.layout_dialog_input, null, false)

        AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(R.string.add_city_dialog_positive_button)) { dialog, whichButton ->
                    val city = (view.findViewById<View>(R.id.dialog_input) as EditText).text.toString()
                    addCity(context, city, callback)
                }
                .setNegativeButton(context.getString(R.string.add_city_dialog_negative_button)) { dialog, whichButton -> }
                .show()
    }

    fun updateFavorites(context: Context, cities: List<String>) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString(context.getString(R.string.pref_favorites_key), Gson().toJson(cities))
        editor.apply()
    }

    fun getFavoritesWeather(context: Context, callback: WeatherCallback) {
        val cities = getFavorites(context)
        val favorites = ArrayList<Favorite>()
        if (!cities.isEmpty()) {
            var yahooService: YahooWeatherService
            for (pos in cities.indices) {
                val city = cities[pos]
                yahooService = YahooWeatherService(object : WeatherServiceCallback {
                    override fun serviceSuccess(channel: Channel) {
                        addFavorite(context, favorites, cities, city, channel, callback)
                    }

                    override fun serviceFailure(exception: Exception) {
                        addFavorite(context, favorites, cities, city, null, callback)
                    }
                })
                yahooService.refreshWeather(city)
            }
        } else {
            callback.onUpdate(null)
        }
    }

    private fun getFavorites(context: Context): ArrayList<String> {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val favorites = sharedPref.getString(context.getString(R.string.pref_favorites_key), null)
                ?: return ArrayList()
        return Gson().fromJson<ArrayList<String>>(favorites, ArrayList<String>().javaClass)
    }

    private fun addFavorite(context: Context, favorites: MutableList<Favorite>,
                            cities: List<String>, city: String, channel: Channel?,
                            callback: WeatherCallback) {
        favorites.add(Favorite(city, TodayEntity(context, channel!!, object : TodayEntity.TodayCallback {
            override fun onCreate(te: TodayEntity) {
                if (favorites.size == cities.size) {
                    val fe = FavoritesEntity(favorites)
                    callback.onUpdate(fe)
                }
            }
        })))
    }

    private fun addCity(context: Context, city: String, callback: PrefsCallback) {
        val cities = getFavorites(context)
        cities.add(city)
        updateFavorites(context, cities)
        callback.onAddCity(city)
    }
}
