package it.chiarani.meteotrentino.api.AvalancheModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SnowpackStructureHighlight {

    @SerializedName("languageCode")
    @Expose
    private String languageCode;

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

}