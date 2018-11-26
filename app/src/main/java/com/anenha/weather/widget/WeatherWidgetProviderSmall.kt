package com.anenha.weather.widget

import android.Manifest
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.RemoteViews

import com.anenha.weather.R
import com.anenha.weather.app.entity.TodayEntity
import com.anenha.weather.app.model.ChannelModel
import com.anenha.weather.app.repository.yahooWeather.WeatherServiceCallback
import com.anenha.weather.app.repository.yahooWeather.YahooWeatherService
import com.anenha.weather.app.utils.Prefs
import com.anenha.weather.app.ui.HomeActivity

import java.text.SimpleDateFormat
import java.util.*

class WeatherWidgetProviderSmall : AppWidgetProvider(), WeatherServiceCallback {
    private var context: Context? = null
    private var remoteViews: RemoteViews? = null
    private var appWidgetManager: AppWidgetManager? = null
    private var allWidgetIds: IntArray? = null
    private var refreshDate: String? = null
    private var useGps: Boolean = false
    private val df = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("WIDGET", "ADDED!")
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        Log.d("WIDGET", "UPDATED!")

        this.context = context
        this.appWidgetManager = appWidgetManager

        // Get all ids
        val thisWidget = ComponentName(context, WeatherWidgetProviderSmall::class.java)
        allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        val resId = if(Prefs.isTransparentMode(context)){
            R.layout.layout_widget_small_transparent
        } else {
            R.layout.layout_widget_small
        }

        for (widgetId in allWidgetIds!!) {
            remoteViews = RemoteViews(context.packageName, resId)
            remoteViews!!.setTextViewText(R.id.widget_update_label, context.getString(R.string.app_update_label))

            setOnClickUpdate(appWidgetIds)
            setOnClickStartActivity()

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }

        callWeatherService(context)
    }

    private fun callWeatherService(context: Context){
        val weatherService = YahooWeatherService(this)

        remoteViews!!.setViewVisibility(R.id.widget_gps_image_view, View.GONE)
        useGps = Prefs.canUseGpsLocation(context)
        var city = Prefs.getInitialCity(context)

        if (useGps && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationProvider: String = LocationManager.GPS_PROVIDER

            val lastKnownLocation: Location = locationManager.getLastKnownLocation(locationProvider)
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)

            if(addresses.isNotEmpty()) {
                val locationCity = addresses[0].subAdminArea
                val locationCountry = addresses[0].countryName
                city = "$locationCity, $locationCountry"
                remoteViews!!.setViewVisibility(R.id.widget_gps_image_view, View.VISIBLE)
            }
        }

        weatherService.refreshWeather(city)
    }

    private fun setOnClickUpdate(appWidgetIds: IntArray) {
        // Register an onClickListener
        val intent = Intent(context, WeatherWidgetProviderSmall::class.java)

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

    override fun serviceSuccess(channel: ChannelModel) {
        refreshDate = df.format(Calendar.getInstance().time)
        TodayEntity(context!!, channel, object : TodayEntity.TodayCallback {
            override fun onCreate(te: TodayEntity) {
                remoteViews!!.setImageViewResource(R.id.widget_image, te.imageResource!!)
                remoteViews!!.setTextViewText(R.id.widget_locale, te.getCity())
                remoteViews!!.setTextViewText(R.id.widget_condition, te.condition)

                remoteViews!!.setTextViewText(R.id.widget_temp_now, te.tempNow)

                remoteViews!!.setTextViewText(R.id.widget_update_label, context!!.getString(R.string.app_update_date, refreshDate))

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