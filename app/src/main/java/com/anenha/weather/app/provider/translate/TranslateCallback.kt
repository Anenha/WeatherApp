package com.anenha.weather.app.provider.translate

/**
 * Created by ajnen on 12/10/2017.
 */

interface TranslateCallback {
    fun onSucess(translatedText: String)
    fun onFailure(exception: Exception)
}
