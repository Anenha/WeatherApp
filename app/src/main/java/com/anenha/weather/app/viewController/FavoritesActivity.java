package com.anenha.weather.app.viewController;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anenha.weather.R;
import com.anenha.weather.app.entity.FavoritesEntity;
import com.anenha.weather.app.utils.Prefs;
import com.anenha.weather.app.viewController.adapter.FavoritesCityAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity {

    @BindView(R.id.favorites_recycler) RecyclerView favoritesRcycler;
    @BindView(R.id.favorite_empty_state) TextView emptyState;
    private FavoritesCityAdapter adapter;
    private boolean editMode = false;
    private List<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this, this.findViewById(android.R.id.content));

        setSupportActionBar( (Toolbar) findViewById(R.id.toolbar) );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyState.setText(getString(R.string.app_loading));

        Prefs.updateFavorites(getApplicationContext(), Prefs.getFavorites(getApplicationContext()), new Prefs.UpdateCallback() {
            @Override
            public void onUpdate(FavoritesEntity fe) {
                reloadView(fe);
            }
        });

    }

    private void reloadView(FavoritesEntity fe){
        cities = Prefs.getFavorites(getApplicationContext());
        if(cities.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            emptyState.setText(R.string.favorite_empty_msg);
            emptyState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { addCity(); }
            });
            favoritesRcycler.setVisibility(View.GONE);
        }
        else {
            emptyState.setVisibility(View.GONE);
            favoritesRcycler.setVisibility(View.VISIBLE);
            favoritesRcycler.setLayoutManager(new LinearLayoutManager(this));
            adapter = new FavoritesCityAdapter(this, cities, fe, editMode);
            favoritesRcycler.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: onBackPressed(); break;
            case R.id.action_add: addCity(); break;
            case R.id.action_remove: removeCities(item); break;
            default: break;
        }
        return true;
    }

    private void addCity(){
        Prefs.addCityDialog(this, new Prefs.PrefsCallback() {
            @Override
            public void onAddCity(String city, FavoritesEntity fe) {
                reloadView(fe);
            }
        });
    }

    private void removeCities(MenuItem menuItem){
        editMode = !editMode;
        adapter.setEditMode(editMode);

        if(editMode){
            menuItem.setIcon(getDrawable(R.drawable.ic_check_circle_24dp));
        } else {
            menuItem.setIcon(getDrawable(R.drawable.ic_delete_24dp));
        }

        if(!adapter.getRemoves().isEmpty()){
            for(String city : adapter.getRemoves()){
                cities.remove(city);
            }

            Prefs.updateFavorites(getApplicationContext(), cities, new Prefs.UpdateCallback() {
                @Override
                public void onUpdate(FavoritesEntity fe) {
                    reloadView(fe);
                }
            });
        }
    }
}
