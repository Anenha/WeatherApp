package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Forecast {

    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("day")
    @Expose
    var day: String? = null
    @SerializedName("high")
    @Expose
    var high: String? = null
    @SerializedName("low")
    @Expose
    var low: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null

}
