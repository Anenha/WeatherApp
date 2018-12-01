package com.anenha.weather.repository.translate

import com.anenha.weather.model.TranslateResponseModel
import com.anenha.weather.repository.Endpoint
import com.google.gson.GsonBuilder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ajnen on 12/10/2017.
 */

class TranslateService(private val callback: TranslateCallback) : Callback<TranslateResponseModel> {
    private val endpoint: Endpoint

    init {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(Endpoint.TRANSLATE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        endpoint = retrofit.create(Endpoint::class.java)
    }

    fun translate(text: String) {
        val call = endpoint.translate(text, "pt", "en")
        call.enqueue(this)
    }

    override fun onResponse(call: Call<TranslateResponseModel>, response: Response<TranslateResponseModel>) {
        if (response.isSuccessful) {
            val translateResponseModel = response.body()
            callback.onSucess(translateResponseModel!!.extract!!.translation!!)

        } else {
            callback.onFailure(Exception())
        }
    }

    override fun onFailure(call: Call<TranslateResponseModel>, t: Throwable) {
        callback.onFailure(Exception())
    }
}
