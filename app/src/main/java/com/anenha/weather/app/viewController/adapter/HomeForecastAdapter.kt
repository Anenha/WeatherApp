package com.anenha.weather.app.viewController.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anenha.weather.BR

import com.anenha.weather.R
import com.anenha.weather.app.entity.ForecastEntity
import com.anenha.weather.app.model.Forecast

/**
 * Created by ajnen on 12/10/2017.
 */

class HomeForecastAdapter(private val context: Context, private val forecast: List<Forecast>) : RecyclerView.Adapter<HomeForecastAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeForecastAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.setVariable(BR.model, ForecastEntity(context, forecast[position]))
        holder.binding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return forecast.size
    }
}
