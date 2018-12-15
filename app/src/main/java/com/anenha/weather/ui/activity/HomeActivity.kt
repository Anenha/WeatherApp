package com.anenha.weather.ui.activity

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.anenha.weather.R
import com.anenha.weather.ui.fragment.WeatherFragment
import com.anenha.weather.utils.AppConst
import com.anenha.weather.utils.Router
import kotlinx.android.synthetic.main.layout_loading.*

class HomeActivity : AppCompatActivity(), WeatherFragment.LoadingListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setActionBar()
        loading.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        if(intent.extras != null && intent.extras.containsKey(AppConst.CITY_TO_REFRESH)){
            startWeatherFragnent(intent.extras.getString(AppConst.CITY_TO_REFRESH))
        }
    }

    private fun setActionBar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL) //start search after user type on search dialog
        handleIntent(intent)

        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            startWeatherFragnent(query)
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //hide keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

                startWeatherFragnent(query)
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> Router.goToFavorites(this)
            R.id.action_settings -> Router.goToSettings(this)
            else -> { }
        }
        return true
    }


    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            startWeatherFragnent(query)
        }
    }

    private fun startWeatherFragnent(city: String?){
        Router.goToCityWeather(supportFragmentManager, city)
    }

    override
    fun onAttachFragment(fragment: Fragment) {
        if (fragment is WeatherFragment) {
            fragment.setLoadingListener(this)
        }
    }

    override
    fun showLoading(show: Boolean){
        if(show){
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
        }
    }

}
