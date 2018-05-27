package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Atmosphere {
    @SerializedName("humidity")
    @Expose
    var humidity: String? = null
    @SerializedName("pressure")
    @Expose
    var pressure: String? = null
    @SerializedName("rising")
    @Expose
    var rising: String? = null
    @SerializedName("visibility")
    @Expose
    var visibility: String? = null
}
