package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Astronomy {

    @SerializedName("sunrise")
    @Expose
    var sunrise: String? = null
    @SerializedName("sunset")
    @Expose
    var sunset: String? = null

}
