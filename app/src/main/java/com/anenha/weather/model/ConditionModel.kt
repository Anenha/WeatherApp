package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConditionModel {

    @SerializedName("code")
    @Expose
    var code: String = ""
    @SerializedName("date")
    @Expose
    var date: String = ""
    @SerializedName("temp")
    @Expose
    var temp: String = ""
    @SerializedName("text")
    @Expose
    var text: String = ""

}
