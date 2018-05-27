package com.anenha.weather.app.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location(city: String, region: String, country: String) {

    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("region")
    @Expose
    var region: String? = null
    @SerializedName("country")
    @Expose
    var country: String? = null

    init {
        this.city = city.trim { it <= ' ' }
        this.region = region.trim { it <= ' ' }
        this.country = country.trim { it <= ' ' }
    }

}
