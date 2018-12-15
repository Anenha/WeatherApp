package com.anenha.weather.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View

import com.anenha.weather.R
import com.anenha.weather.entity.FavoritesEntity
import com.anenha.weather.utils.Prefs
import com.anenha.weather.ui.adapter.FavoritesCityAdapter

import kotlinx.android.synthetic.main.content_favorites.*

class FavoritesActivity : AppCompatActivity() {

    private var adapter: FavoritesCityAdapter? = null
    private var editMode = false
    private var fe: FavoritesEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setSupportActionBar(findViewById<View>(R.id.toolbar) as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getWeather()
    }

    private fun getWeather() {
        isLoading(true)
        Prefs.getFavoritesWeather(applicationContext, object : Prefs.WeatherCallback {
            override fun onUpdate(fe: FavoritesEntity?) {
                reloadView(fe)
            }
        })
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            favoriteEmptyState.visibility = View.VISIBLE
            favoriteEmptyState.text = getString(R.string.app_loading)
            favoritesRcycler.visibility = View.GONE
        } else {
            favoriteEmptyState.visibility = View.GONE
            favoritesRcycler!!.visibility = View.VISIBLE
        }
    }

    private fun reloadView(fe: FavoritesEntity?) {
        this.fe = fe
        if (fe == null) {
            favoriteEmptyState.visibility = View.VISIBLE
            favoriteEmptyState.setText(R.string.favorite_empty_msg)
            favoriteEmptyState.setOnClickListener { addCity() }
            favoritesRcycler!!.visibility = View.GONE
        } else {
            isLoading(false)
            this.fe!!.favorite!!.sort()
            favoritesRcycler!!.layoutManager = LinearLayoutManager(this)
            adapter = FavoritesCityAdapter(this, fe, editMode)
            favoritesRcycler!!.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorites, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_add -> addCity()
            R.id.action_remove -> removeCities(item)
            else -> {
            }
        }
        return true
    }

    private fun addCity() {
        Prefs.addCityDialog(this, currentFocus, object : Prefs.PrefsCallback {
            override fun onAddCity(city: String) {
                getWeather()
            }
        })
    }

    private fun removeCities(menuItem: MenuItem) {
        editMode = !editMode
        adapter!!.setEditMode(editMode)

        if (editMode) {
            menuItem.icon = getDrawable(R.drawable.ic_check_circle)
        } else {
            menuItem.icon = getDrawable(R.drawable.ic_delete)
        }

        if (!adapter!!.removes.isEmpty()) {
            for (city in adapter!!.removes) {
                for (i in 0 until fe!!.cities.size) {
                    if (city.equals(fe!!.cities[i], ignoreCase = true)) {
                        fe!!.cities.removeAt(i)
                        fe!!.favorite!!.removeAt(i)
                    }
                }
            }

            Prefs.updateFavorites(applicationContext, fe!!.cities)
            reloadView(fe)
        }
    }
}
