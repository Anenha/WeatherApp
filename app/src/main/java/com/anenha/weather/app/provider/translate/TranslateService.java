package com.anenha.weather.app.provider.translate;

import com.anenha.weather.app.model.TranslateResponseModel;
import com.anenha.weather.app.provider.Endpoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ajnen on 12/10/2017.
 */

public class TranslateService implements Callback<TranslateResponseModel> {

    private TranslateCallback callback;

    public TranslateService (TranslateCallback callback){
        this.callback = callback;
    }

    public void translate(String text){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Endpoint.TRANSLATE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Endpoint endpoint = retrofit.create(Endpoint.class);

        Call<TranslateResponseModel> call = endpoint.translate(text, "pt", "en");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<TranslateResponseModel> call, Response<TranslateResponseModel> response) {
        if(response.isSuccessful()) {
            TranslateResponseModel translateResponseModel = response.body();
            callback.onSucess(translateResponseModel.getExtract().getTranslation());

        } else {
            callback.onFailure( new Exception() );
        }
    }

    @Override
    public void onFailure(Call<TranslateResponseModel> call, Throwable t) {
        callback.onFailure(new Exception());
    }
}
