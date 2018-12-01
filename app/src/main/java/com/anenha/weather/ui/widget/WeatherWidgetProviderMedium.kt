package com.anenha.weather.ui.widget

import com.anenha.weather.R
import com.anenha.weather.entity.TodayEntity

class WeatherWidgetProviderMedium : BaseWidgetProvider() {

    override fun initComponent() {
        setLayout(R.layout.widget_medium, R.layout.widget_medium_transparent)
        setThisWidgetClass(WeatherWidgetProviderMedium::class.java)
    }

    override fun weatherServiceSuccess(te: TodayEntity) {
        remoteViews!!.setImageViewResource(R.id.widget_image, te.imageResource!!)
        remoteViews!!.setTextViewText(R.id.widget_locale, te.getLocal(false))
        remoteViews!!.setTextViewText(R.id.widget_condition, te.condition)

        val temp = te.tempNow.substring(0, te.tempNow.length - 1)
        remoteViews!!.setTextViewText(R.id.widget_temp_now, temp)

        remoteViews!!.setTextViewText(R.id.widget_temp_high, te.tempHigh)
        remoteViews!!.setTextViewText(R.id.widget_high_low, te.tempLow)
        remoteViews!!.setTextViewText(R.id.widget_update_label, context!!.getString(R.string.app_update_date, refreshDate))
    }
}