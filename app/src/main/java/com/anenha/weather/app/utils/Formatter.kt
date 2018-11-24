package com.anenha.weather.app.utils

import android.content.Context
import com.anenha.weather.R
import com.anenha.weather.app.model.ConditionModel
import com.anenha.weather.app.model.LocationModel

import java.text.NumberFormat

/**
 * Created by ajnen on 12/10/2017.
 */

object Formatter {

    fun location(location: LocationModel, fullLocation: Boolean): String {
        val locale = StringBuilder()
        locale.append(location.city).append(", ")
        if (fullLocation) {
            locale.append(location.region!!.trim { it <= ' ' }).append(", ")
        }
        locale.append(location.country)
        return locale.toString()
    }

    fun temperature(temperture: Int?, tempUnit: String): String {
        when (tempUnit) {
            "1" -> return temperture.toString() + "\u00B0" + "C"
            "2" -> return celsiusToFahrenheit(temperture)
            else -> return temperture.toString() + "\u00B0" + "C"
        }
    }

    fun time(hour: String, timeFormat: String): String {
        when (timeFormat) {
            "1" -> return getTime(hour)
            "2" -> return hour
            else -> return hour
        }
    }

    fun weekDay(context: Context, weekDay: String): String {
        return getWeekday(context, weekDay, true)
    }

    fun date(context: Context, day: String, date: String, monthInitials: Boolean, hasYear: Boolean): String {
        return getDate(context, day, date, monthInitials, hasYear)
    }

    fun conditionDescription(context: Context, condition: ConditionModel): String {
        val conditionsArray = context.resources.getStringArray(R.array.condition_descriptions)
        val code = Integer.parseInt(condition.code)
        return if (code >= 0 && code <= 47) {
            conditionsArray[code]
        } else {
            context.getString(R.string.condition_unavailable)
        }
    }

    private fun celsiusToFahrenheit(celsusTemp: Int?): String {
        return (celsusTemp!! * 9 / 5 + 32).toString() + "\u00B0" + "F"
    }

    private fun getTime(time: String): String {
        val hour = time.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (hour[1].equals("AM", ignoreCase = true)) {
            return "0" + hour[0]
        } else {
            val time_PM = hour[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val afternoon = Integer.parseInt(time_PM[0]) + 12
            return afternoon.toString() + ":" + time_PM[1]
        }
    }

    private fun getDate(context: Context, day: String?, date: String, monthInitials: Boolean, hasYear: Boolean): String {
        var formatedDate = ""

        if (day != null && !day.isEmpty()) {
            formatedDate = getWeekday(context, day, false) + ", "
        }

        val finalDate = date.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val intFormater = NumberFormat.getIntegerInstance()
        intFormater.minimumIntegerDigits = 2
        intFormater.maximumIntegerDigits = 2
        intFormater.isGroupingUsed = false
        val monthDay = intFormater.format(Integer.parseInt(finalDate[0]).toLong())

        finalDate[1] = getMonth(context, finalDate[1], monthInitials)
        formatedDate = formatedDate + monthDay + " " + finalDate[1]

        if (hasYear) {
            formatedDate = formatedDate + " " + finalDate[2]
        }
        return formatedDate
    }

    private fun getWeekday(context: Context, weekday: String, initials: Boolean): String {
        val weekdays = context.resources.getStringArray(R.array.weekdays)
        var day = ""

        when (weekday) {
            "Sun" -> day = weekdays[0]
            "Mon" -> day = weekdays[1]
            "Tue" -> day = weekdays[2]
            "Wed" -> day = weekdays[3]
            "Thu" -> day = weekdays[4]
            "Fri" -> day = weekdays[5]
            "Sat" -> day = weekdays[6]
        }

        return if (initials) {
            day.substring(0, 3)
        } else day
    }

    private fun getMonth(context: Context, key: String, initials: Boolean): String {
        val months = context.resources.getStringArray(R.array.months)
        var month = ""

        when (key) {
            "Jan" -> month = months[0]
            "Feb" -> month = months[1]
            "Mar" -> month = months[2]
            "Apr" -> month = months[3]
            "May" -> month = months[4]
            "Jun" -> month = months[5]
            "Jul" -> month = months[6]
            "Aug" -> month = months[7]
            "Sep" -> month = months[8]
            "Oct" -> month = months[9]
            "Nov" -> month = months[10]
            "Dec" -> month = months[11]
        }

        return if (initials) {
            month.substring(0, 3)
        } else month
    }
}
