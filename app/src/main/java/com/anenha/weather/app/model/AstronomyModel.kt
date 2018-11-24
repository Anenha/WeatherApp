package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AstronomyModel {

    @SerializedName("sunrise")
    @Expose
    var sunrise: String = ""
    @SerializedName("sunset")
    @Expose
    var sunset: String = ""

}
