package com.anenha.weather.app.model;

import com.anenha.weather.app.entity.TodayEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ajnen on 22/10/2017.
 */

public class Favorite {
    @SerializedName("name")
    private String name;
    @SerializedName("todayEntity")
    private TodayEntity todayEntity;

    public Favorite(final String name, final TodayEntity todayEntity){
        this.name = name;
        this.todayEntity = todayEntity;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public TodayEntity getTodayEntity() {
        return todayEntity;
    }

    public void setTodayEntity(TodayEntity todayEntity) {
        this.todayEntity = todayEntity;
    }
}
