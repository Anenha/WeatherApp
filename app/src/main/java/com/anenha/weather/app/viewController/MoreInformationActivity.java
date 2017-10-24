package com.anenha.weather.app.viewController;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.anenha.weather.R;

/**
 * Created by ajnen on 14/10/2017.
 */

public class MoreInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);

        final String city =  getIntent().getExtras().getString("CITY_URL");

        WebView webView = (WebView) findViewById(R.id.more_info_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        //set cookies
        CookieManager.getInstance().removeSessionCookies(null);
        String tempUnit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.pref_temperature_key), "1");;
        tempUnit = tempUnit.equalsIgnoreCase("1") ? "metric" : "imperial";
        CookieManager.getInstance().setCookie(".yahoo.com", "weather=unit%3D" + tempUnit);

        webView.loadUrl(getString(R.string.yahoo_weather_url) + city);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
