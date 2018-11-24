package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ana.j.saragossa on 10/24/17.
 */

class ExtractModel {
    @SerializedName("translation")
    @Expose
    var translation: String = ""
    @SerializedName("actualQuery")
    @Expose
    var actualQuery: String = ""
    @SerializedName("resultType")
    @Expose
    var resultType: String = ""
    @SerializedName("transliteration")
    @Expose
    var transliteration: String = ""
    @SerializedName("synonyms")
    @Expose
    var synonyms: List<String> = ArrayList()
    @SerializedName("sourceLanguage")
    @Expose
    var sourceLanguage: String = ""
}
