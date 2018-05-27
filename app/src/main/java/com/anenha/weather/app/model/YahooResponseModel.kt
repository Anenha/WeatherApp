package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class YahooResponseModel {

    @SerializedName("query")
    @Expose
    var query: Query? = null

}
