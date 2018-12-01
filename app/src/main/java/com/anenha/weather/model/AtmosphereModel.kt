package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AtmosphereModel {
    @SerializedName("humidity")
    @Expose
    var humidity: String = ""
    @SerializedName("pressure")
    @Expose
    var pressure: String = ""
    @SerializedName("rising")
    @Expose
    var rising: String = ""
    @SerializedName("visibility")
    @Expose
    var visibility: String = ""
}
