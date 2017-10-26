package com.anenha.weather.app.entity;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.anenha.weather.R;
import com.anenha.weather.app.model.Astronomy;
import com.anenha.weather.app.model.Atmosphere;
import com.anenha.weather.app.model.Channel;
import com.anenha.weather.app.model.Condition;
import com.anenha.weather.app.model.Forecast;
import com.anenha.weather.app.model.Location;
import com.anenha.weather.app.provider.translate.TranslateCallback;
import com.anenha.weather.app.provider.translate.TranslateService;
import com.anenha.weather.app.utils.Formatter;

import java.util.Locale;

/**
 * Created by ajnen on 13/10/2017.
 */

public class TodayEntity extends CoreEntity {

    public interface Callback {
        void onGetLocale(String locale);
    }
    public interface TodayCallback {
        void onCreate(TodayEntity te);
    }

    private int resourceId;
    private Drawable image;
    private String humidity;
    private String pressure;
    private String date;
    private String tempNow;
    private String tempHigh;
    private String tempLow;
    private String sunrise;
    private String sunset;
    private String condition;
    private String local;

    public TodayEntity(final Context context, final Channel channel, final TodayCallback todayCallback){
        super(context);

        final Condition condition = channel.getItem().getCondition();
        final Astronomy astronomy = channel.getAstronomy();
        final Atmosphere atmosphere = channel.getAtmosphere();
        final Location location = channel.getLocation();
        final Forecast forecast = channel.getItem().getForecast().get(0);


        final Integer code = Integer.parseInt(condition.getCode());
        if (code >= 0 && code <= 47) {
            resourceId = context.getResources().getIdentifier("drawable/weather_icon_" + condition.getCode(), null, context.getPackageName());
        } else {
            resourceId = context.getResources().getIdentifier("drawable/weather_icon_na", null, context.getPackageName());
        }

        this.image =  context.getDrawable(resourceId);
        this.humidity = context.getString(R.string.humidity, atmosphere.getHumidity());

        final Double pressure = Double.parseDouble(atmosphere.getPressure())*0.0295301;
        this.pressure = context.getString(R.string.pressure, String.format("%.0f", pressure));

        final String[] date = condition.getDate().split(",");

        this.date = Formatter.date(context, date[0], date[1].trim(), false, true);
        this.tempNow = Formatter.temperature(Integer.parseInt(condition.getTemp()), getTemperatureUnit());
        this.tempHigh = Formatter.temperature(Integer.parseInt(forecast.getHigh()), getTemperatureUnit());
        this.tempLow = Formatter.temperature(Integer.parseInt(forecast.getLow()), getTemperatureUnit());
        this.sunrise = Formatter.time(astronomy.getSunrise(), getTimeUnit());
        this.sunset = Formatter.time(astronomy.getSunset(), getTimeUnit());
        this.condition = Formatter.conditionDescription(context, condition);

        setLocal(location, new Callback() {
            @Override
            public void onGetLocale(String locale) {
                local = locale;
                todayCallback.onCreate(TodayEntity.this);
            }
        });
    }

    public String getDate() { return date; }

    public Drawable getImage() { return image; }

    public Integer getImageResource() { return resourceId; }

    public String getCondition() { return condition; }

    public String getHumidity() { return humidity; }

    private void setLocal(final Location location, final Callback callback) {
        final String language = Locale.getDefault().getLanguage();
        final String locale = Formatter.location(location, true);

        if(!language.equalsIgnoreCase("pt")){
            callback.onGetLocale(locale.toString());
        } else {
            TranslateService translateService = new TranslateService(new TranslateCallback() {
                @Override
                public void onSucess(String translatedText) {
                    callback.onGetLocale(translatedText);
                }

                @Override
                public void onFailure(Exception exception) {
                    callback.onGetLocale(locale.toString());
                }
            });
            translateService.translate(locale.toString());
        }
    }

    public String getLocal(final boolean fullLocation) {
        if(local != null) {
            String[] locale = local.split(",");
            return Formatter.location(new Location(locale[0], locale[1], locale[2]), fullLocation);
        } else {
            return  "";
        }
    }

    public String getSunrise() { return sunrise; }

    public String getSunset() { return sunset; }

    public String getPressure() { return pressure; }

    public String getTempHigh() { return tempHigh; }

    public String getTempLow() { return tempLow; }

    public String getTempNow() { return tempNow; }
}
