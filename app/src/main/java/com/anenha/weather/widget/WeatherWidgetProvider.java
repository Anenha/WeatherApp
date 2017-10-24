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

public class WeatherWidgetProvider extends AppWidgetProvider implements WeatherServiceCallback {
    private Context context;
    private RemoteViews remoteViews;
    private AppWidgetManager appWidgetManager;
    private int[] allWidgetIds;

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
            remoteViews.setTextViewText(R.id.widget_condition, context.getString(R.string.app_loading));
            remoteViews.setTextViewText(R.id.widget_locale, "");

            // Register an onClickListener
            Intent intent = new Intent(context, WeatherWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        new TodayEntity(context, channel, new TodayEntity.TodayCallback() {
            @Override
            public void onCreate(TodayEntity te) {
                remoteViews.setImageViewResource(R.id.widget_image, te.getImageResource());
                remoteViews.setTextViewText(R.id.widget_locale, te.getLocal(false));
                remoteViews.setTextViewText(R.id.widget_condition, te.getCondition());

                for (int widgetId : allWidgetIds) {
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            }
        });
    }

    @Override
    public void serviceFailure(Exception exception) {
        remoteViews.setImageViewResource(R.id.widget_image, R.drawable.icon_na);
        remoteViews.setTextViewText(R.id.widget_locale, "");
        remoteViews.setTextViewText(R.id.widget_condition, "Falha ao atualizar!");

        for (int widgetId : allWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}