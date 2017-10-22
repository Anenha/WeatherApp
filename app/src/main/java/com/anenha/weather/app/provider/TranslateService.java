package com.anenha.weather.app.provider;

import android.os.AsyncTask;

/**
 * Created by ajnen on 12/10/2017.
 */

public class TranslateService {

    public void translatePT_EN(final String text2translate, final TranslateCallback callback){
        class bgStuff extends AsyncTask<Void, Void, Void> {

            String translatedText = "";

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    translatedText = translation(text2translate);
                } catch (Exception e) {
                    callback.onFailure(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                callback.onSucess(translatedText);
            }

        }

        new bgStuff().execute();
    }

    public void translateEN_PT(final String text2translate, final TranslateCallback callback){
        class bgStuff extends AsyncTask<Void, Void, Void> {

            String translatedText = "";

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    translatedText = translationEN_PT(text2translate);
                } catch (Exception e) {
                    callback.onFailure(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                callback.onSucess(translatedText);
            }

        }

        new bgStuff().execute();
    }

    public String translation(String text) throws Exception {
//        Translate.setClientId("AJS");
//        Translate.setClientSecret("9VujCdcno1QoDtaI+E85");
//
//        return Translate.execute(text, Language.PORTUGUESE, Language.ENGLISH);

        return text;
    }

    public String translationEN_PT(String text) throws Exception{
//        Translate.setClientId("AJS");
//        Translate.setClientSecret("9VujCdcno1QoDtaI+E85");
//
//        return Translate.execute(text,Language.ENGLISH,Language.PORTUGUESE);

        return text;
    }
}
