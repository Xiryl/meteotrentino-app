package it.chiarani.meteotrentino.api.AvalancheModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Validity {

    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("until")
    @Expose
    private String until;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

}