package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QueryModel {

    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("created")
    @Expose
    var created: String = ""
    @SerializedName("lang")
    @Expose
    var lang: String = ""
    @SerializedName("results")
    @Expose
    var results: ResultsModel? = null

}
