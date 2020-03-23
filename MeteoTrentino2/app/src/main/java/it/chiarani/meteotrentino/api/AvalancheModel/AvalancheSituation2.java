package it.chiarani.meteotrentino.api.AvalancheModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvalancheSituation2 {

    @SerializedName("elevationLow")
    @Expose
    private Integer elevationLow;

    public Integer getElevationLow() {
        return elevationLow;
    }

    public void setElevationLow(Integer elevationLow) {
        this.elevationLow = elevationLow;
    }

}
