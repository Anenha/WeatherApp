package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImageModel {

    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("width")
    @Expose
    var width: String = ""
    @SerializedName("height")
    @Expose
    var height: String = ""
    @SerializedName("link")
    @Expose
    var link: String = ""
    @SerializedName("url")
    @Expose
    var url: String = ""

}
