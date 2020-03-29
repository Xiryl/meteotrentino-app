package it.chiarani.meteotrentinoapp.api.BaciniModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bacini {

    @SerializedName("nomeBacino")
    @Expose
    private String nomeBacino;
    @SerializedName("stazioni")
    @Expose
    private List<Stazioni> stazioni = null;

    public String getNomeBacino() {
        return nomeBacino;
    }

    public void setNomeBacino(String nomeBacino) {
        this.nomeBacino = nomeBacino;
    }

    public List<Stazioni> getStazioni() {
        return stazioni;
    }

    public void setStazioni(List<Stazioni> stazioni) {
        this.stazioni = stazioni;
    }

}