package com.anenha.weather.app.model

import com.anenha.weather.app.entity.TodayEntity
import com.google.gson.annotations.SerializedName

/**
 * Created by ajnen on 22/10/2017.
 */

class Favorite(@field:SerializedName("name")
               var name: String?, @field:SerializedName("todayEntity")
               var todayEntity: TodayEntity?) : Comparable<Favorite> {

    override fun compareTo(other: Favorite): Int {

        val thisMinValue: String? = this.name
        val otherMinValue: String? = other.name

        val before = -1
        val equal = 0
        val after = 1

        if (this === other) return equal
        if (thisMinValue!!.compareTo(otherMinValue!!) < 0) return before
        return if (thisMinValue.compareTo(otherMinValue) > 0) after else equal

    }
}
