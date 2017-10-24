package com.anenha.weather.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ana.j.saragossa on 10/24/17.
 */

public class Extract {
    @SerializedName("translation")
    @Expose
    private String translation;
    @SerializedName("actualQuery")
    @Expose
    private String actualQuery;
    @SerializedName("resultType")
    @Expose
    private String resultType;
    @SerializedName("transliteration")
    @Expose
    private String transliteration;
    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms;
    @SerializedName("sourceLanguage")
    @Expose
    private String sourceLanguage;

    public String getActualQuery() {
        return actualQuery;
    }

    public void setActualQuery(String actualQuery) {
        this.actualQuery = actualQuery;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }
}
