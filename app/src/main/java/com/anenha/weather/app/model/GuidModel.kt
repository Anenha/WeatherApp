package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GuidModel {

    @SerializedName("isPermaLink")
    @Expose
    var isPermaLink: String = ""

}
