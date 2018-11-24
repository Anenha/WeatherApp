package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnitsModel {

    @SerializedName("distance")
    @Expose
    var distance: String = ""
    @SerializedName("pressure")
    @Expose
    var pressure: String = ""
    @SerializedName("speed")
    @Expose
    var speed: String = ""
    @SerializedName("temperature")
    @Expose
    var temperature: String = ""

}
