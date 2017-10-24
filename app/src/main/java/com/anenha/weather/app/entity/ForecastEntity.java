package com.anenha.weather.app.entity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.anenha.weather.app.model.Condition;
import com.anenha.weather.app.model.Forecast;
import com.anenha.weather.app.utils.Formatter;


/**
 * Created by ajnen on 12/10/2017.
 */

public class ForecastEntity extends CoreEntity {
    private Context context;
    private Forecast forecast;

    private Drawable image;
    private String date;
    private String day;
    private String high;
    private String low;
    private String description;

    public ForecastEntity(final Context context, final Forecast forecast) {
        super(context);
        this.context = context;
        this.forecast = forecast;

        setImage();
        setDay();
        setDate();
        setHigh();
        setLow();
        setText();
    }

    private void setImage() {
        int resourceId = context.getResources().getIdentifier("drawable/icon_" + forecast.getCode(), null, context.getPackageName());
        this.image = context.getDrawable(resourceId);
    }

    private void setDay() {
        this.day = Formatter.weekDay(context, forecast.getDay());
    }

    private void setDate() {
        this.date = Formatter.date(context, null, forecast.getDate(), true, false);
    }

    private void setHigh() {
        this.high = Formatter.temperature(Integer.parseInt(forecast.getHigh()), getTemperatureUnit());
    }

    private void setLow() {
        this.low = Formatter.temperature(Integer.parseInt(forecast.getLow()), getTemperatureUnit());
    }

    private void setText() {
        Condition condition = new Condition();
        condition.setCode(forecast.getCode());
        condition.setText(forecast.getText());
        this.description = Formatter.conditionDescription(context, condition);
    }

    public Drawable getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getDescription() {
        return description;
    }
}
