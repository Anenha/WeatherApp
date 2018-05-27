package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Results {

    @SerializedName("channel")
    @Expose
    var channel: Channel? = null

}
