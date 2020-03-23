package it.chiarani.meteotrentino.api.AvalancheModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SnowpackStructureComment {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("languageCode")
    @Expose
    private String languageCode;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

}
