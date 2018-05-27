package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Channel {

    @SerializedName("units")
    @Expose
    var units: Units? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("link")
    @Expose
    var link: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("language")
    @Expose
    var language: String? = null
    @SerializedName("lastBuildDate")
    @Expose
    var lastBuildDate: String? = null
    @SerializedName("ttl")
    @Expose
    var ttl: String? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("wind")
    @Expose
    var wind: Wind? = null
    @SerializedName("atmosphere")
    @Expose
    var atmosphere: Atmosphere? = null
    @SerializedName("astronomy")
    @Expose
    var astronomy: Astronomy? = null
    @SerializedName("image")
    @Expose
    var image: Image? = null
    @SerializedName("item")
    @Expose
    var item: Item? = null

}
