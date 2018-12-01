package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForecastModel {

    @SerializedName("code")
    @Expose
    var code: String = ""
    @SerializedName("date")
    @Expose
    var date: String = ""
    @SerializedName("day")
    @Expose
    var day: String = ""
    @SerializedName("high")
    @Expose
    var high: String = ""
    @SerializedName("low")
    @Expose
    var low: String = ""
    @SerializedName("text")
    @Expose
    var text: String = ""

}
