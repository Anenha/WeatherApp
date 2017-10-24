package com.anenha.weather.app.model;

import android.support.annotation.NonNull;

import com.anenha.weather.app.entity.FavoritesEntity;
import com.anenha.weather.app.entity.TodayEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ajnen on 22/10/2017.
 */

public class Favorite implements Comparable<Favorite> {
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

    @Override
    public int compareTo(@NonNull Favorite aThat) {

        String thisMinValue, athatMinValue;

        thisMinValue = this.getName();
        athatMinValue = aThat.getName();

        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == aThat) return EQUAL;
        if (thisMinValue.compareTo(athatMinValue) < 0) return BEFORE;
        if (thisMinValue.compareTo(athatMinValue) > 0) return AFTER;

        return EQUAL;
    }
}
