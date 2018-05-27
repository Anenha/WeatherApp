package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Item {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("lat")
    @Expose
    var lat: String? = null
    @SerializedName("long")
    @Expose
    var long: String? = null
    @SerializedName("link")
    @Expose
    var link: String? = null
    @SerializedName("pubDate")
    @Expose
    var pubDate: String? = null
    @SerializedName("condition")
    @Expose
    var condition: Condition? = null
    @SerializedName("forecast")
    @Expose
    var forecast: MutableList<Forecast>? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("guid")
    @Expose
    var guid: Guid? = null

}
