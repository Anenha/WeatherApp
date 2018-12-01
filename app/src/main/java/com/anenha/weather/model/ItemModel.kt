package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ItemModel {

    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("lat")
    @Expose
    var lat: String = ""
    @SerializedName("long")
    @Expose
    var long: String = ""
    @SerializedName("link")
    @Expose
    var link: String = ""
    @SerializedName("pubDate")
    @Expose
    var pubDate: String = ""
    @SerializedName("condition")
    @Expose
    var condition: ConditionModel? = null
    @SerializedName("forecast")
    @Expose
    var forecast: MutableList<ForecastModel>? = null
    @SerializedName("description")
    @Expose
    var description: String = ""
    @SerializedName("guid")
    @Expose
    var guid: GuidModel? = null

}
