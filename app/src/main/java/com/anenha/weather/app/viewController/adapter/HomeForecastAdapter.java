package com.anenha.weather.app.viewController.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anenha.weather.R;
import com.anenha.weather.app.entity.ForecastEntity;
import com.anenha.weather.app.model.Forecast;

import java.util.List;

import static com.android.databinding.library.baseAdapters.BR.model;

/**
 * Created by ajnen on 12/10/2017.
 */

public class HomeForecastAdapter  extends RecyclerView.Adapter<HomeForecastAdapter.ViewHolder> {
    private Context context;
    private List<Forecast> forecast;

    public HomeForecastAdapter(final Context context, final List<Forecast> forecast) {
        this.context = context;
        this.forecast = forecast;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        ViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        ViewDataBinding getBinding() {
            return binding;
        }
    }

    @Override
    public HomeForecastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getBinding().setVariable(model, new ForecastEntity(context, forecast.get(position)));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }
}
