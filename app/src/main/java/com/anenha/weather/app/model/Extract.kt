package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ana.j.saragossa on 10/24/17.
 */

class Extract {
    @SerializedName("translation")
    @Expose
    var translation: String? = null
    @SerializedName("actualQuery")
    @Expose
    var actualQuery: String? = null
    @SerializedName("resultType")
    @Expose
    var resultType: String? = null
    @SerializedName("transliteration")
    @Expose
    var transliteration: String? = null
    @SerializedName("synonyms")
    @Expose
    var synonyms: List<String>? = null
    @SerializedName("sourceLanguage")
    @Expose
    var sourceLanguage: String? = null
}
