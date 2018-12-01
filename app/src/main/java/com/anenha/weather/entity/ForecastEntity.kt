package com.anenha.weather.entity

import android.content.Context
import android.graphics.drawable.Drawable
import com.anenha.weather.model.ConditionModel
import com.anenha.weather.model.ForecastModel
import com.anenha.weather.utils.Formatter


/**
 * Created by ajnen on 12/10/2017.
 */

class ForecastEntity(private val context: Context, private val forecast: ForecastModel) : CoreEntity(context) {
    var image: Drawable? = null
    var date: String = ""
    var day: String = ""
    var high: String = ""
    var low: String = ""
    var description: String = ""

    init {
        setImage()
        setDay()
        setDate()
        setHigh()
        setLow()
        setText()
    }

    private fun setImage() {
        val resourceId = context.resources.getIdentifier("drawable/weather_icon_" + forecast.code, null, context.packageName)
        this.image = context.getDrawable(resourceId)
    }

    private fun setDay() {
        this.day = Formatter.weekDay(context, forecast.day!!)
    }

    private fun setDate() {
        this.date = Formatter.date(context, "", forecast.date!!, true, false)
    }

    private fun setHigh() {
        this.high = Formatter.temperature(Integer.parseInt(forecast.high), temperatureUnit)
    }

    private fun setLow() {
        this.low = Formatter.temperature(Integer.parseInt(forecast.low), temperatureUnit)
    }

    private fun setText() {
        val condition = ConditionModel()
        condition.code = forecast.code
        condition.text = forecast.text
        this.description = Formatter.conditionDescription(context, condition)
    }
}
