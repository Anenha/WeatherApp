package com.anenha.weather.app.entity;

import com.anenha.weather.app.model.Favorite;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajnen on 22/10/2017.
 */

public class FavoritesEntity {
    @SerializedName("favorite")
    @Expose
    private List<Favorite> favorite;

    public FavoritesEntity(List<Favorite> favorite){this.favorite = favorite; }

    public List<String> getCities() {
        List<String> cities = new ArrayList<>();

        if(favorite != null && !favorite.isEmpty()){
            for(Favorite f:favorite){
                cities.add(f.getName());
            }
        }

        return cities;
    }

    public List<Favorite> getFavorite() {
        return favorite;
    }
}
