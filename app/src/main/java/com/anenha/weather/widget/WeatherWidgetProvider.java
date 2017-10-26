package com.anenha.weather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.anenha.weather.R;
import com.anenha.weather.app.entity.TodayEntity;
import com.anenha.weather.app.model.Channel;
import com.anenha.weather.app.provider.yahooWeather.WeatherServiceCallback;
import com.anenha.weather.app.provider.yahooWeather.YahooWeatherService;
import com.anenha.weather.app.utils.Prefs;
import com.anenha.weather.app.viewController.HomeActivity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherWidgetProvider extends AppWidgetProvider implements WeatherServiceCallback {
    private Context context;
    private RemoteViews remoteViews;
    private AppWidgetManager appWidgetManager;
    private int[] allWidgetIds;
    private String refreshDate;
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("WIDGET", "ADDED!");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.d("WIDGET", "UPDATED!");

        this.context = context;
        this.appWidgetManager = appWidgetManager;

        YahooWeatherService weatherService = new YahooWeatherService(this);
        weatherService.refreshWeather(Prefs.getInitialCity(context));

        // Get all ids
        ComponentName thisWidget = new ComponentName(context, WeatherWidgetProvider.class);
        allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            remoteViews.setTextViewText(R.id.widget_update_label, context.getString(R.string.app_update_label));

            setOnClickUpdate(appWidgetIds);
            setOnClickStartActivity();

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void setOnClickUpdate(int[] appWidgetIds){
        // Register an onClickListener
        Intent intent = new Intent(context, WeatherWidgetProvider.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_sync_container, pendingIntent);
    }

    private void setOnClickStartActivity(){
        Intent intent = new Intent(context, HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        refreshDate = df.format(Calendar.getInstance().getTime());
        new TodayEntity(context, channel, new TodayEntity.TodayCallback() {
            @Override
            public void onCreate(TodayEntity te) {
                remoteViews.setImageViewResource(R.id.widget_image, te.getImageResource());
                remoteViews.setTextViewText(R.id.widget_locale, te.getLocal(false));
                remoteViews.setTextViewText(R.id.widget_condition, te.getCondition());

                remoteViews.setTextViewText(R.id.widget_temp_now, te.getTempNow());

                String highLow = te.getTempHigh().substring(0, te.getTempHigh().length() -1) +
                        "/" + te.getTempLow().substring(0, te.getTempLow().length() -1);
                remoteViews.setTextViewText(R.id.widget_high_low, highLow);
                remoteViews.setTextViewText(R.id.widget_update_label,
                        context.getString(R.string.app_update_date,
                                refreshDate ));

                for (int widgetId : allWidgetIds) {
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            }
        });
    }

    @Override
    public void serviceFailure(Exception exception) {
        remoteViews.setTextViewText(R.id.widget_update_label,
                context.getString(R.string.app_update_date, refreshDate ));

        for (int widgetId : allWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}