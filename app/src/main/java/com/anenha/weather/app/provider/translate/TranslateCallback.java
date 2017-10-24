package com.anenha.weather.app.provider.translate;

/**
 * Created by ajnen on 12/10/2017.
 */

public interface TranslateCallback {
    void onSucess(String translatedText);
    void onFailure(Exception exception);
}
