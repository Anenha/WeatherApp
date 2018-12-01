package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class YahooResponseModel {

    @SerializedName("query")
    @Expose
    var query: QueryModel? = null

}
