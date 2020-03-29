package it.chiarani.meteotrentinoapp.api.AvalancheModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvalancheSituation1 {

    @SerializedName("elevationLow")
    @Expose
    private Integer elevationLow;
    @SerializedName("aspects")
    @Expose
    private List<String> aspects = null;
    @SerializedName("avalancheSituation")
    @Expose
    private String avalancheSituation;

    public Integer getElevationLow() {
        return elevationLow;
    }

    public void setElevationLow(Integer elevationLow) {
        this.elevationLow = elevationLow;
    }

    public List<String> getAspects() {
        return aspects;
    }

    public void setAspects(List<String> aspects) {
        this.aspects = aspects;
    }

    public String getAvalancheSituation() {
        return avalancheSituation;
    }

    public void setAvalancheSituation(String avalancheSituation) {
        this.avalancheSituation = avalancheSituation;
    }

}