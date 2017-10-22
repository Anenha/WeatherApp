package com.anenha.weather.app.viewController.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anenha.weather.R;
import com.anenha.weather.app.entity.FavoritesEntity;
import com.anenha.weather.app.entity.TodayEntity;
import com.anenha.weather.app.viewController.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.anenha.weather.R.id.parent;

/**
 * Created by ajnen on 16/10/2017.
 */

public class FavoritesCityAdapter extends RecyclerView.Adapter<FavoritesCityAdapter.ViewHolder> {
    private Context context;
    private List<String> cities;
    private List<String> removePositions;
    private boolean editMode;
    private FavoritesEntity fe;

    public FavoritesCityAdapter(final Context context, final List<String> cities, FavoritesEntity fe, final boolean editMode) {
        this.context = context;
        this.cities = cities;
        this.fe = fe;
        this.editMode = editMode;
        removePositions = new ArrayList<>();
    }

    public void setEditMode(final boolean editMode){
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
        removePositions = new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<String> getCities() {
        return cities;
    }

    public List<String> getRemoves() { return removePositions; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.favorite_checkbox) CheckBox checkBox;
        @BindView(R.id.favorite_item_title) TextView city;
        @BindView(R.id.favorite_item_subtitle) TextView subtitle;
        @BindView(R.id.favorite_item_condition) TextView condition;
        @BindView(R.id.favorite_item_temp_now) TextView tempNow;
        @BindView(R.id.favorite_item_temp_high_low) TextView tempHighLow;
        @BindView(R.id.favorite_item_image) ImageView image;
        @BindView(R.id.favorite_item_container) LinearLayout container;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public FavoritesCityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.checkBox.setChecked(false);
        holder.checkBox.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);

        holder.city.setText(cities.get(position));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeActivity.class);
                i.putExtra("REFRESH_CITY", cities.get(position));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    removePositions.add(cities.get(position));
                    Log.e("REMOVER",cities.get(position));
                } else if (!removePositions.isEmpty() && removePositions.contains(cities.get(position))){
                    removePositions.remove(cities.get(position));
                }
            }
        });

        if(fe != null){
            final TodayEntity te = fe.getFavorite().get(position).getTodayEntity();
            holder.subtitle.setText(te.getLocal());
            holder.image.setImageDrawable(te.getImage());
            holder.tempNow.setText(te.getTempNow());
            holder.tempHighLow.setText(te.getTempHigh() + "\n" + te.getTempLow());
            holder.condition.setText(te.getCondition());
        } else {
            holder.subtitle.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}

