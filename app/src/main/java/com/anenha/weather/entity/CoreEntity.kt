package com.anenha.weather.entity

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.anenha.weather.R

/**
 * Created by ajnen on 14/10/2017.
 */

open class CoreEntity internal constructor(private val context: Context) {
    private val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val temperatureUnit: String
        get() = sharedPref.getString(context.getString(R.string.pref_temperature_key), "1")

    val timeUnit: String
        get() = sharedPref.getString(context.getString(R.string.pref_time_key), "1")
}
