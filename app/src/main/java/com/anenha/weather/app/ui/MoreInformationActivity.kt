package com.anenha.weather.app.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

import com.anenha.weather.R
import com.anenha.weather.app.utils.AppConst

/**
 * Created by ajnen on 14/10/2017.
 */

class MoreInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_information)

        val city = intent.extras!!.getString(AppConst.KEY_CITY)

        val webView = findViewById<View>(R.id.more_info_webview) as WebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()
        //set cookies
        CookieManager.getInstance().removeSessionCookies(null)
        var tempUnit = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString(getString(R.string.pref_temperature_key), "1")
        tempUnit = if (tempUnit!!.equals("1", ignoreCase = true)) "metric" else "imperial"
        CookieManager.getInstance().setCookie(".yahoo.com", "weather=unit%3D$tempUnit")

        webView.loadUrl(getString(R.string.yahoo_weather_url) + city!!)
    }

    override fun onBackPressed() {
        finish()
    }

    internal class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
