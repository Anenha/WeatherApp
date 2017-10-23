package com.anenha.weather.app.viewController;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anenha.weather.R;
import com.anenha.weather.app.entity.TodayEntity;
import com.anenha.weather.app.model.Channel;
import com.anenha.weather.app.model.Forecast;
import com.anenha.weather.app.provider.WeatherServiceCallback;
import com.anenha.weather.app.provider.YahooWeatherService;
import com.anenha.weather.app.utils.Prefs;
import com.anenha.weather.app.viewController.adapter.HomeForecastAdapter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements WeatherServiceCallback {

    @BindView(R.id.loading) LinearLayout loading;
    @BindView(R.id.WeatherIconImageView) ImageView WeatherIconImageView;
    @BindView(R.id.TemperatureTextView) TextView TemperatureTextView;
    @BindView(R.id.LocationTextView) TextView locationTextView;
    @BindView(R.id.ConditionTextView) TextView ConditionTextView;
    @BindView(R.id.sunrisetextView) TextView SunriseText;
    @BindView(R.id.sunsettextView) TextView SunsetText;
    @BindView(R.id.humiditytextView) TextView HumidityText;
    @BindView(R.id.pressuretextView) TextView PressureText;
    @BindView(R.id.forecast_recycler) RecyclerView forecastRecycler;
    @BindView(R.id.PevisaotextView) TextView previsaoTextView;
    @BindView(R.id.textView14) TextView day_date_today;
    @BindView(R.id.leftarrow) ImageView leftRecyclerArrow;
    @BindView(R.id.rightarrow) ImageView rightRecyclerArrow;
    @BindView(R.id.fab) FloatingActionButton fab;

    private Channel channel;
    private YahooWeatherService yahooService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this, this.findViewById(android.R.id.content));

        setActionBar();

        yahooService = new YahooWeatherService(this);

        final String city;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("REFRESH_CITY")){
            city = getIntent().getExtras().getString("REFRESH_CITY");
        } else {
            city = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString(getString(R.string.pref_city_key), getString(R.string.pref_default_display_city));
        }

        refreshWeather(city);
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL); //habilita a pesquisa assim que o usuario digitar no search dialog
        handleIntent(getIntent());

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            refreshWeather(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showAddCityDialog(); }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                refreshWeather(query);
                return true;
            }
        });

        searchView.setOnSearchClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more_info:
                goToMoreInfo();
                break;
            case R.id.action_favorites:
                openFavorites();
                break;
            case R.id.action_settings:
                openSettings();
                break;
            default: break;
        }
        return true;
    }

    @Override
    public void serviceSuccess(Channel channel) {
        this.channel = channel;
        loading.setVisibility(View.GONE);

//        ContentHomeBinding viewBinding = DataBindingUtil.setContentView(this, R.layout.content_home);
//        viewBinding.setToday(new TodayEntity(getApplicationContext(), channel));

        TodayEntity te = new TodayEntity(getApplicationContext(), channel);
        WeatherIconImageView.setImageDrawable(te.getImage());
        HumidityText.setText(te.getHumidity());
        PressureText.setText(te.getPressure());
        locationTextView.setText(te.getLocal());
        day_date_today.setText(te.getDate());
        TemperatureTextView.setText(te.getTempNow());
        SunriseText.setText(te.getSunrise());
        SunsetText.setText(te.getSunset());
        ConditionTextView.setText(te.getCondition());

        final List<Forecast> forecast = channel.getItem().getForecast();
        if(channel.getItem().getCondition().getDate().split(",")[0].equalsIgnoreCase(forecast.get(0).getDay())){
            forecast.remove(0);
        }

        forecastRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final HomeForecastAdapter adapter = new HomeForecastAdapter(getApplicationContext(), forecast);
        forecastRecycler.setAdapter(adapter);
    }

    @Override
    public void serviceFailure(Exception exception) {
        loading.setVisibility(View.GONE);
        Snackbar.make(getCurrentFocus(), exception.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void refreshWeather(String local){
        loading.setVisibility(View.VISIBLE);
        yahooService.refreshWeather(local);
    }

    private void showAddCityDialog(){
        Prefs.addCityDialog(this, new Prefs.PrefsCallback() {
            @Override
            public void onAddCity(String city) {
                Snackbar.make(getCurrentFocus(),getString(R.string.added_favorite_cty_snack, city), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void openFavorites(){
        Intent i = new Intent(getApplicationContext(), FavoritesActivity.class);
        startActivity(i);
    }

    private void openSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void goToMoreInfo(){
        AlertDialog dialog  = new AlertDialog.Builder(this)
                .setTitle(R.string.leave_app_dialog_title)
                .setMessage(R.string.leave_app_dialog_message)
                .setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String city = channel.getLink().split("city-")[1];
                        Intent i = new Intent(getApplicationContext(), MoreInformationActivity.class);
                        i.putExtra("CITY_URL", city);
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create();
        dialog.show();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            refreshWeather(query);
        }
    }

}
