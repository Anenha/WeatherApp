package com.anenha.weather.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ajnen on 22/10/2017.
 */

public class Favorites {
    @SerializedName("cities")
    @Expose
    private List<String> cities;

    public Favorites(List<String> cities){
        this.cities = cities;
    }

    public List<String> getCities() {
        return cities;
    }
}
