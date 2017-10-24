package com.anenha.weather.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ana.j.saragossa on 10/24/17.
 */

public class TranslateResponseModel {
    @SerializedName("extract")
    @Expose
    private Extract extract;
    @SerializedName("originalResponse")
    @Expose
    private String originalResponse;

    public Extract getExtract() {
        return extract;
    }

    public void setExtract(Extract extract) {
        this.extract = extract;
    }

    public String getOriginalResponse() {
        return originalResponse;
    }

    public void setOriginalResponse(String originalResponse) {
        this.originalResponse = originalResponse;
    }
}
