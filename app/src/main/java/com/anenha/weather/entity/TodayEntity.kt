package com.anenha.weather.entity

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Range

import com.anenha.weather.R
import com.anenha.weather.model.ChannelModel
import com.anenha.weather.model.LocationModel
import com.anenha.weather.repository.translate.TranslateCallback
import com.anenha.weather.repository.translate.TranslateService
import com.anenha.weather.utils.Formatter

import java.util.Locale

/**
 * Created by ajnen on 13/10/2017.
 */

class TodayEntity(context: Context, channel: ChannelModel, todayCallback: TodayCallback) : CoreEntity(context) {
    private var resourceId: Int = 0
    val image: Drawable?
    val humidity: String
    val pressure: String
    val date: String
    val tempNow: String
    val tempHigh: String
    val tempLow: String
    val sunrise: String
    val sunset: String
    val condition: String
    private var local: String? = null

    val imageResource: Int?
        get() = resourceId

    interface Callback {
        fun onGetLocale(locale: String)
    }

    interface TodayCallback {
        fun onCreate(te: TodayEntity)
    }

    init {
        val condition = channel.item?.condition
        val astronomy = channel.astronomy
        val atmosphere = channel.atmosphere
        val location = channel.location
        val forecast = channel.item?.forecast!![0]

        val code = Integer.parseInt(condition?.code)

        var iconEndPath = "na"
        if ( (Range.create(0, 47)).contains(code) ) {
            iconEndPath = condition?.code!!
        }

        resourceId = context.resources.getIdentifier("drawable/weather_icon_$iconEndPath", null, context.packageName)

        this.image = context.getDrawable(resourceId)
        this.humidity = context.getString(R.string.humidity, atmosphere?.humidity)

        val pressure = java.lang.Double.parseDouble(atmosphere?.pressure) * 0.0295301
        this.pressure = context.getString(R.string.pressure, String.format("%.0f", pressure))

        val date = condition?.date?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()

        this.date = Formatter.date(context, date!![0], date[1].trim { it <= ' ' }, false, true)
        this.tempNow = Formatter.temperature(Integer.parseInt(condition.temp), temperatureUnit)
        this.tempHigh = Formatter.temperature(Integer.parseInt(forecast.high), temperatureUnit)
        this.tempLow = Formatter.temperature(Integer.parseInt(forecast.low), temperatureUnit)
        this.sunrise = Formatter.time(astronomy?.sunrise!!, timeUnit)
        this.sunset = Formatter.time(astronomy?.sunset!!, timeUnit)
        this.condition = Formatter.conditionDescription(context, condition)

        setLocal(location!!, object : Callback {
            override fun onGetLocale(locale: String) {
                local = locale
                todayCallback.onCreate(this@TodayEntity)
            }
        })
    }

    private fun setLocal(location: LocationModel, callback: Callback) {
        val language = Locale.getDefault().language
        val locale = Formatter.location(location, true)

        if (!language.equals("pt", ignoreCase = true)) {
            callback.onGetLocale(locale.toString())
        } else {
            val translateService = TranslateService(object : TranslateCallback {
                override fun onSucess(translatedText: String) {
                    callback.onGetLocale(translatedText)
                }

                override fun onFailure(exception: Exception) {
                    callback.onGetLocale(locale.toString())
                }
            })
            translateService.translate(locale.toString())
        }
    }

    fun getLocal(fullLocation: Boolean): String {
        if (local != null) {
            val locale = local!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return Formatter.location(LocationModel(locale[0], locale[1], locale[2]), fullLocation)
        }

        return ""
    }

    fun getCity(): String {
        if (local != null) {
            val locale = local!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return locale[0]
        }

        return ""
    }
}
