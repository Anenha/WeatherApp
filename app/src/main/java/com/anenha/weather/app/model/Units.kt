package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Units {

    @SerializedName("distance")
    @Expose
    var distance: String? = null
    @SerializedName("pressure")
    @Expose
    var pressure: String? = null
    @SerializedName("speed")
    @Expose
    var speed: String? = null
    @SerializedName("temperature")
    @Expose
    var temperature: String? = null

}
