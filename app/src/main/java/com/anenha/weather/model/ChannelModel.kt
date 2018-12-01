package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChannelModel {

    @SerializedName("units")
    @Expose
    var units: UnitsModel? = null
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("link")
    @Expose
    var link: String = ""
    @SerializedName("description")
    @Expose
    var description: String = ""
    @SerializedName("language")
    @Expose
    var language: String = ""
    @SerializedName("lastBuildDate")
    @Expose
    var lastBuildDate: String = ""
    @SerializedName("ttl")
    @Expose
    var ttl: String = ""
    @SerializedName("location")
    @Expose
    var location: LocationModel? = null
    @SerializedName("wind")
    @Expose
    var wind: WindModel? = null
    @SerializedName("atmosphere")
    @Expose
    var atmosphere: AtmosphereModel? = null
    @SerializedName("astronomy")
    @Expose
    var astronomy: AstronomyModel? = null
    @SerializedName("image")
    @Expose
    var image: ImageModel? = null
    @SerializedName("item")
    @Expose
    var item: ItemModel? = null

}
