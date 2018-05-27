package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Image {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("width")
    @Expose
    var width: String? = null
    @SerializedName("height")
    @Expose
    var height: String? = null
    @SerializedName("link")
    @Expose
    var link: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null

}
