package com.anenha.weather.app.ui

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.anenha.weather.R
import com.anenha.weather.app.entity.TodayEntity
import com.anenha.weather.app.model.ChannelModel
import com.anenha.weather.app.model.ForecastModel
import com.anenha.weather.app.repository.yahooWeather.WeatherServiceCallback
import com.anenha.weather.app.repository.yahooWeather.YahooWeatherService
import com.anenha.weather.app.utils.Prefs
import com.anenha.weather.app.ui.adapter.HomeForecastAdapter
import com.anenha.weather.app.utils.AppConst

import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.layout_loading.*
import android.location.Geocoder
import android.location.Location
import java.util.*
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log


class HomeActivity : AppCompatActivity(), WeatherServiceCallback {

    private var channel: ChannelModel? = null
    private var yahooService: YahooWeatherService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setActionBar()
        yahooService = YahooWeatherService(this)
        swipeToRefresh.setOnRefreshListener { onResume() }
        swipeToRefresh.setColorSchemeResources(
                R.color.light_blue,
                R.color.colorAccent,
                R.color.colorPrimary)
    }

    override fun onResume() {
        super.onResume()

        if (intent.extras != null && intent.extras!!.containsKey(AppConst.CITY_TO_REFRESH)) {
            val city = intent.extras!!.getString(AppConst.CITY_TO_REFRESH)
            getWeatherforCity(city)
        } else {
            if (Prefs.canUseGpsLocation(this)) {
                getWeatherForGps()
            } else {
                getWeatherforCity(Prefs.getInitialCity(this))
            }
        }
    }

    private fun getWeatherforCity(city: String){
        gpsLocationImage.visibility = View.GONE
        refreshWeather(city)
    }

    private fun getWeatherForGps(){
        gpsLocationImage.visibility = View.VISIBLE
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationProvider: String = LocationManager.GPS_PROVIDER // NETWORK_PROVIDER Or GPS_PROVIDER

            val lastKnownLocation: Location = locationManager.getLastKnownLocation(locationProvider)

            val geocoder = Geocoder(applicationContext, Locale.getDefault())

            val addresses = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)

            if(addresses.isNotEmpty()) {
                val city = addresses[0].subAdminArea
                val country = addresses[0].countryName

                Log.d("LAST_KNOWN_LOCATION", addresses[0].getAddressLine(0))

                refreshWeather("$city, $country")
            } else {
                gpsLocationImage.visibility = View.GONE
                refreshWeather(Prefs.getInitialCity(this))
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    66)
        }
    }

    private fun setActionBar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL) //habilita a pesquisa assim que o usuario digitar no search dialog
        handleIntent(intent)

        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            refreshWeather(query)
        }
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        homeFab.setOnClickListener { showAddCityDialog() }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //hide keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

                refreshWeather(query)
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_more_info -> goToMoreInfo()
            R.id.action_favorites -> openFavorites()
            R.id.action_settings -> openSettings()
            else -> {
            }
        }
        return true
    }

    override fun serviceSuccess(channel: ChannelModel) {
        this.channel = channel

        TodayEntity(applicationContext, channel, object : TodayEntity.TodayCallback {
            override fun onCreate(te: TodayEntity) {
                loading.visibility = View.GONE
                swipeToRefresh.isRefreshing = false
                loadTodayInfo(te)
            }
        })

        loadForecast()
    }

    private fun loadTodayInfo(te: TodayEntity) {
        weatherIconImageView.setImageDrawable(te.image)
        humiditytextView.text = te.humidity
        pressuretextView.text = te.pressure
        locationTextView.text = te.getLocal(false)
        fullDateTodayTextView.text = te.date
        temperatureTextView.text = te.tempNow
        sunrisetextView.text = te.sunrise
        sunsettextView.text = te.sunset
        conditionTextView.text = te.condition
    }

    private fun loadForecast() {
        val forecast : MutableList<ForecastModel> = channel?.item?.forecast!!
        if (channel?.item?.condition?.date?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.get(0).equals(forecast[0].day, ignoreCase = true)) {
            forecast.removeAt(0)
        }

        homeForecastRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = HomeForecastAdapter(applicationContext, forecast)
        homeForecastRecycler.adapter = adapter
    }

    override fun serviceFailure(exception: Exception) {
        loading.visibility = View.GONE
        Snackbar.make(currentFocus!!, exception.localizedMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    private fun refreshWeather(local: String) {
        if(!swipeToRefresh.isRefreshing) {
            loading.visibility = View.VISIBLE
        }
        yahooService!!.refreshWeather(local)
    }

    private fun showAddCityDialog() {
        Prefs.addCityDialog(this, object : Prefs.PrefsCallback {
            override fun onAddCity(city: String) {
                Snackbar.make(currentFocus!!, getString(R.string.added_favorite_cty_snack, city), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        })
    }

    private fun openFavorites() {
        val i = Intent(applicationContext, FavoritesActivity::class.java)
        startActivity(i)
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun goToMoreInfo() {
        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.leave_app_dialog_title)
                .setMessage(R.string.leave_app_dialog_message)
                .setPositiveButton(R.string.app_ok) { _, _ ->
                    val city = channel!!.link.split("city-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    val i = Intent(applicationContext, MoreInformationActivity::class.java)
                    i.putExtra(AppConst.KEY_CITY, city)
                    startActivity(i)
                }
                .setNegativeButton(R.string.app_cancel) { _, _ -> }
                .create()
        dialog.show()
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            refreshWeather(query)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            66 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getWeatherForGps()
                } else {
                    Prefs.useGpsLocation(this, canUse = false)
                    getWeatherforCity(Prefs.getInitialCity(this))
                }
                return
            }
        }
    }

}
