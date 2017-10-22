package com.anenha.weather.app.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.anenha.weather.R;
import com.anenha.weather.app.model.Astronomy;
import com.anenha.weather.app.model.Atmosphere;
import com.anenha.weather.app.model.Channel;
import com.anenha.weather.app.model.Condition;
import com.anenha.weather.app.model.Forecast;
import com.anenha.weather.app.model.Location;
import com.anenha.weather.app.utils.Formatter;

import java.util.List;

/**
 * Created by ajnen on 13/10/2017.
 */

public class TodayEntity extends CoreEntity {

    private Drawable image;
    private String humidity;
    private String pressure;
    private String local;
    private String date;
    private String tempNow;
    private String tempHigh;
    private String tempLow;
    private String sunrise;
    private String sunset;
    private String condition;

    public TodayEntity(final Context context, final Channel channel){
        super(context);

        final Condition condition = channel.getItem().getCondition();
        final Location location = channel.getLocation();
        final Astronomy astronomy = channel.getAstronomy();
        final Atmosphere atmosphere = channel.getAtmosphere();
        final Forecast forecast = channel.getItem().getForecast().get(0);

        int resourceId;
        final Integer code = Integer.parseInt(condition.getCode());
        if (code >= 0 && code <= 47) {
            resourceId = context.getResources().getIdentifier("drawable/icon_" + condition.getCode(), null, context.getPackageName());
        } else {
            resourceId = context.getResources().getIdentifier("drawable/icon_na", null, context.getPackageName());
        }

        this.image =  context.getDrawable(resourceId);
        this.humidity = context.getString(R.string.humidity, atmosphere.getHumidity());

        final Double pressure = Double.parseDouble(atmosphere.getPressure())*0.0295301;
        this.pressure = context.getString(R.string.pressure, String.format("%.0f", pressure));

        final String[] date = condition.getDate().split(",");


        this.local = Formatter.location(location);
        this.date = Formatter.date(context, date[0], date[1].trim(), false, true);
        this.tempNow = Formatter.temperature(Integer.parseInt(condition.getTemp()), getTemperatureUnit());
        this.tempHigh = Formatter.temperature(Integer.parseInt(forecast.getHigh()), getTemperatureUnit());
        this.tempLow = Formatter.temperature(Integer.parseInt(forecast.getLow()), getTemperatureUnit());
        this.sunrise = Formatter.time(astronomy.getSunrise(), getTimeUnit());
        this.sunset = Formatter.time(astronomy.getSunset(), getTimeUnit());
        this.condition = Formatter.conditionDescription(context, condition);
    }

    public String getDate() { return date; }

    public Drawable getImage() { return image; }

    public String getCondition() { return condition; }

    public String getHumidity() { return humidity; }

    public String getLocal() { return local; }

    public String getSunrise() { return sunrise; }

    public String getSunset() { return sunset; }

    public String getPressure() { return pressure; }

    public String getTempHigh() { return tempHigh; }

    public String getTempLow() { return tempLow; }

    public String getTempNow() { return tempNow; }
}
