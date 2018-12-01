package com.anenha.weather.entity

import com.anenha.weather.model.FavoriteModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Created by ajnen on 22/10/2017.
 */

class FavoritesEntity(@field:SerializedName("favorite")
                      @field:Expose
                      val favorite: MutableList<FavoriteModel>?) {

    val cities: MutableList<String>
        get() {
            val cities = ArrayList<String>()

            if (favorite != null && !favorite.isEmpty()) {
                for (f in favorite) {
                    cities.add(f.name!!)
                }
            }

            return cities
        }

}
