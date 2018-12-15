package com.anenha.weather.utils

import android.app.Activity
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import com.anenha.weather.R
import com.anenha.weather.ui.activity.FavoritesActivity
import com.anenha.weather.ui.activity.MoreInformationActivity
import com.anenha.weather.ui.activity.SettingsActivity
import com.anenha.weather.ui.fragment.WeatherFragment

class Router {
    companion object {
        fun goToCityWeather(supportFragmentManager: FragmentManager, city: String?){
            val fragment = WeatherFragment.newInstance(city)

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(fragment.tag + city)
                    .commit()
        }

        fun goToMoreInfo(context: Context, city: String) {
            val dialog = AlertDialog.Builder(context)
                    .setTitle(R.string.leave_app_dialog_title)
                    .setMessage(R.string.leave_app_dialog_message)
                    .setPositiveButton(R.string.app_ok) { _, _ ->
                        val i = Intent(context, MoreInformationActivity::class.java)
                        i.putExtra(AppConst.KEY_CITY, city)
                        context.startActivity(i)
                    }
                    .setNegativeButton(R.string.app_cancel) { _, _ -> }
                    .create()
            dialog.show()
        }

        fun goToFavorites(context: Context) {
            val i = Intent(context, FavoritesActivity::class.java)
            context.startActivity(i)
        }

        fun goToSettings(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}