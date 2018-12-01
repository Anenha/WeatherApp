package com.anenha.weather.ui.widget

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
import com.anenha.weather.entity.TodayEntity
import com.anenha.weather.model.ChannelModel
import com.anenha.weather.repository.yahooWeather.WeatherServiceCallback
import com.anenha.weather.repository.yahooWeather.YahooWeatherService
import com.anenha.weather.ui.activity.HomeActivity
import com.anenha.weather.utils.Prefs
import java.text.SimpleDateFormat
import java.util.*

open class BaseWidgetProvider : AppWidgetProvider(), WeatherServiceCallback {
    protected var context: Context? = null
    protected var remoteViews: RemoteViews? = null
    protected var appWidgetManager: AppWidgetManager? = null
    protected var allWidgetIds: IntArray? = null
    protected var refreshDate: String? = null
    private var useGps: Boolean = false
    private val df = SimpleDateFormat("HH:mm", Locale.getDefault())

    private var resId: Int = -1
    private var resIdTransparent: Int = -1
    private lateinit var thisWidget: ComponentName
    private lateinit var className: Class<*>


    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("WIDGET", "ADDED!")
    }

    fun setLayout(resId: Int, resIdTransparent: Int){
        this.resId = resId
        this.resIdTransparent = resIdTransparent
    }

    fun setThisWidgetClass(className: Class<*>){
        this.className = className
        thisWidget = ComponentName(context, className)
    }

    open fun initComponent(){}

    open fun weatherServiceSuccess(te: TodayEntity){}

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        Log.d("WIDGET", "UPDATED!")

        this.context = context
        this.appWidgetManager = appWidgetManager

        initComponent()

        // Get all ids
        allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        val resourceId = if(Prefs.isTransparentMode(context)){
            resIdTransparent
        } else {
           resId
        }

        for (widgetId in allWidgetIds!!) {
            remoteViews = RemoteViews(context.packageName, resourceId)
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
        val intent = Intent(context, className)

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

                weatherServiceSuccess(te)

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