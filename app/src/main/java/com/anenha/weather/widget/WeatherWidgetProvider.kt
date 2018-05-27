package com.anenha.weather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

import com.anenha.weather.R
import com.anenha.weather.app.entity.TodayEntity
import com.anenha.weather.app.model.Channel
import com.anenha.weather.app.provider.yahooWeather.WeatherServiceCallback
import com.anenha.weather.app.provider.yahooWeather.YahooWeatherService
import com.anenha.weather.app.utils.Prefs
import com.anenha.weather.app.viewController.HomeActivity

import java.sql.Timestamp
import java.text.DateFormat
import java.text.FieldPosition
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class WeatherWidgetProvider : AppWidgetProvider(), WeatherServiceCallback {
    private var context: Context? = null
    private var remoteViews: RemoteViews? = null
    private var appWidgetManager: AppWidgetManager? = null
    private var allWidgetIds: IntArray? = null
    private var refreshDate: String? = null
    private val df = SimpleDateFormat("HH:mm")

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("WIDGET", "ADDED!")
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        Log.d("WIDGET", "UPDATED!")

        this.context = context
        this.appWidgetManager = appWidgetManager

        val weatherService = YahooWeatherService(this)
        weatherService.refreshWeather(Prefs.getInitialCity(context))

        // Get all ids
        val thisWidget = ComponentName(context, WeatherWidgetProvider::class.java)
        allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        for (widgetId in allWidgetIds!!) {
            remoteViews = RemoteViews(context.packageName, R.layout.layout_widget)
            remoteViews!!.setTextViewText(R.id.widget_update_label, context.getString(R.string.app_update_label))

            setOnClickUpdate(appWidgetIds)
            setOnClickStartActivity()

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    private fun setOnClickUpdate(appWidgetIds: IntArray) {
        // Register an onClickListener
        val intent = Intent(context, WeatherWidgetProvider::class.java)

        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

        val pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews!!.setOnClickPendingIntent(R.id.widget_sync_container, pendingIntent)
    }

    private fun setOnClickStartActivity() {
        val intent = Intent(context, HomeActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        remoteViews!!.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
    }

    override fun serviceSuccess(channel: Channel) {
        refreshDate = df.format(Calendar.getInstance().time)
        TodayEntity(context!!, channel, object : TodayEntity.TodayCallback {
            override fun onCreate(te: TodayEntity) {
                remoteViews!!.setImageViewResource(R.id.widget_image, te.imageResource!!)
                remoteViews!!.setTextViewText(R.id.widget_locale, te.getLocal(false))
                remoteViews!!.setTextViewText(R.id.widget_condition, te.condition)

                remoteViews!!.setTextViewText(R.id.widget_temp_now, te.tempNow)

                val highLow = te.tempHigh.substring(0, te.tempHigh.length - 1) +
                        "/" + te.tempLow.substring(0, te.tempLow.length - 1)
                remoteViews!!.setTextViewText(R.id.widget_high_low, highLow)
                remoteViews!!.setTextViewText(R.id.widget_update_label,
                        context!!.getString(R.string.app_update_date,
                                refreshDate))

                for (widgetId in allWidgetIds!!) {
                    appWidgetManager!!.updateAppWidget(widgetId, remoteViews)
                }
            }
        })
    }

    override fun serviceFailure(exception: Exception) {
        remoteViews!!.setTextViewText(R.id.widget_update_label,
                context!!.getString(R.string.app_update_date, refreshDate))

        for (widgetId in allWidgetIds!!) {
            appWidgetManager!!.updateAppWidget(widgetId, remoteViews)
        }
    }
}