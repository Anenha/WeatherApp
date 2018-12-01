package com.anenha.weather.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocationModel(city: String, region: String, country: String) {

    @SerializedName("city")
    @Expose
    var city: String = ""
    @SerializedName("region")
    @Expose
    var region: String = ""
    @SerializedName("country")
    @Expose
    var country: String = ""

    init {
        this.city = city.trim { it <= ' ' }
        this.region = region.trim { it <= ' ' }
        this.country = country.trim { it <= ' ' }
    }

}
