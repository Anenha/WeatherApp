package com.anenha.weather.ui.widget

import com.anenha.weather.R
import com.anenha.weather.entity.TodayEntity

class WeatherWidgetProviderSmall : BaseWidgetProvider() {

    override fun initComponent() {
        setLayout(R.layout.widget_small, R.layout.widget_small_transparent)
        setThisWidgetClass(WeatherWidgetProviderSmall::class.java)
    }

    override fun weatherServiceSuccess(te: TodayEntity) {
        remoteViews!!.setImageViewResource(R.id.widget_image, te.imageResource!!)
        remoteViews!!.setTextViewText(R.id.widget_locale, te.getCity())
        remoteViews!!.setTextViewText(R.id.widget_condition, te.condition)

        remoteViews!!.setTextViewText(R.id.widget_temp_now, te.tempNow)

        remoteViews!!.setTextViewText(R.id.widget_update_label, context!!.getString(R.string.app_update_date, refreshDate))
    }

}