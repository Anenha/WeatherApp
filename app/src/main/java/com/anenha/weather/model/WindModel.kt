package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WindModel {

    @SerializedName("chill")
    @Expose
    var chill: String = ""
    @SerializedName("direction")
    @Expose
    var direction: String = ""
    @SerializedName("speed")
    @Expose
    var speed: String = ""

}
