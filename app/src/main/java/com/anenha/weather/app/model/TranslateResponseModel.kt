package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ana.j.saragossa on 10/24/17.
 */

class TranslateResponseModel {
    @SerializedName("extract")
    @Expose
    var extract: ExtractModel? = null
    @SerializedName("originalResponse")
    @Expose
    var originalResponse: String = ""
}
