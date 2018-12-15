package com.anenha.weather.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.anenha.weather.R
import com.anenha.weather.databinding.FragmentWeatherBinding
import com.anenha.weather.entity.TodayEntity
import com.anenha.weather.model.ChannelModel
import com.anenha.weather.model.ForecastModel
import com.anenha.weather.repository.yahooWeather.WeatherServiceCallback
import com.anenha.weather.repository.yahooWeather.YahooWeatherService
import com.anenha.weather.ui.adapter.HomeForecastAdapter
import com.anenha.weather.utils.AppConst
import com.anenha.weather.utils.Prefs
import com.anenha.weather.utils.Router
import kotlinx.android.synthetic.main.fragment_weather.*
import java.util.*


class WeatherFragment: Fragment(), WeatherServiceCallback {
    private lateinit var appContext: Context
    private var channel: ChannelModel? = null
    private var yahooService: YahooWeatherService? = null
    private lateinit var binding: FragmentWeatherBinding
    private var mCallback: LoadingListener? = null

    interface LoadingListener {
        fun showLoading(show: Boolean)
    }

    fun setLoadingListener(activity: LoadingListener) {
        mCallback = activity
    }

    companion object {
        fun newInstance(city: String?): WeatherFragment {
            val fragment = WeatherFragment()

            if(city != null){
                val args = Bundle()
                args.putString(AppConst.CITY_TO_REFRESH, city)
                fragment.arguments = args
            }

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_weather, container, false)
        appContext = activity!!.applicationContext

        binding = DataBindingUtil.bind(view)!!
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yahooService = YahooWeatherService(this)
        swipeToRefresh.setOnRefreshListener { onResume() }
        swipeToRefresh.setColorSchemeResources(
                R.color.light_blue,
                R.color.colorAccent,
                R.color.colorPrimary)

        homeFab.setOnClickListener { showAddCityDialog() }

        moreInfoIcon.setOnClickListener {

            val city = channel!!.link.split("city-".toRegex()).dropLastWhile { mString -> mString.isEmpty() }.toTypedArray()[1]
            Router.goToMoreInfo(context!!, city) }
    }

    override fun onResume() {
        super.onResume()

        if (this.arguments != null && this.arguments!!.containsKey(AppConst.CITY_TO_REFRESH)) {
            val city = this.arguments!!.getString(AppConst.CITY_TO_REFRESH)
            getWeatherforCity(city)
        } else {
            if (Prefs.canUseGpsLocation(appContext)) {
                getWeatherForGps()
            } else {
                getWeatherforCity(Prefs.getInitialCity(appContext))
            }
        }
    }

    private fun getWeatherforCity(city: String){
        gpsLocationImage.visibility = View.GONE
        refreshWeather(city)
    }

    private fun getWeatherForGps(){
        gpsLocationImage.visibility = View.VISIBLE
        if(ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationProvider: String = LocationManager.GPS_PROVIDER // NETWORK_PROVIDER Or GPS_PROVIDER

            val lastKnownLocation: Location = locationManager.getLastKnownLocation(locationProvider)

            val geocoder = Geocoder(appContext, Locale.getDefault())

            val addresses = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)

            if(addresses.isNotEmpty()) {
                val city = addresses[0].subAdminArea
                val country = addresses[0].countryName

                Log.d("LAST_KNOWN_LOCATION", addresses[0].getAddressLine(0))

                refreshWeather("$city, $country")
            } else {
                gpsLocationImage.visibility = View.GONE
                refreshWeather(Prefs.getInitialCity(appContext))
            }

        } else {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    66)
        }
    }

    override fun serviceSuccess(channel: ChannelModel) {
        this.channel = channel

        TodayEntity(appContext, channel, object : TodayEntity.TodayCallback {
            override fun onCreate(te: TodayEntity) {
                loading( false)
                swipeToRefresh.isRefreshing = false
                loadTodayInfo(te)
            }
        })

        loadForecast()
    }

    private fun loadTodayInfo(te: TodayEntity) {
        binding.today = te
    }

    private fun loadForecast() {
        val forecast : MutableList<ForecastModel> = channel?.item?.forecast!!
        if (channel?.item?.condition?.date?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.get(0).equals(forecast[0].day, ignoreCase = true)) {
            forecast.removeAt(0)
        }

        homeForecastRecycler.layoutManager = LinearLayoutManager(appContext, LinearLayoutManager.HORIZONTAL, false)
        val adapter = HomeForecastAdapter(appContext, forecast)
        homeForecastRecycler.adapter = adapter
    }

    override fun serviceFailure(exception: Exception) {
        loading(false)
        Snackbar.make(activity!!.currentFocus, exception.localizedMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    private fun refreshWeather(local: String) {
        if(!swipeToRefresh.isRefreshing) {
            loading(true)
        }
        yahooService!!.refreshWeather(local)
    }

    private fun showAddCityDialog() {
        Prefs.addCityDialog(activity!!, activity!!.currentFocus, object : Prefs.PrefsCallback {
            override fun onAddCity(city: String) {
                Snackbar.make(activity!!.currentFocus, getString(R.string.added_favorite_cty_snack, city), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            66 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getWeatherForGps()
                } else {
                    Prefs.useGpsLocation(appContext, canUse = false)
                    getWeatherforCity(Prefs.getInitialCity(appContext))
                }
                return
            }
        }
    }

    fun loading(show: Boolean){
        mCallback?.showLoading(show)
    }

}