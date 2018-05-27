package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Condition {

    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("temp")
    @Expose
    var temp: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null

}
