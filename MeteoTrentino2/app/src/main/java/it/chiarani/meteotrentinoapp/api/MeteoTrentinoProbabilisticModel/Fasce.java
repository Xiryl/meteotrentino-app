package it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fasce {

    @SerializedName("fascia")
    @Expose
    private String fascia;
    @SerializedName("fasciaOre")
    @Expose
    private String fasciaOre;
    @SerializedName("fenomeni")
    @Expose
    private List<Fenomeni> fenomeni = null;

    public String getFascia() {
        return fascia;
    }

    public void setFascia(String fascia) {
        this.fascia = fascia;
    }

    public String getFasciaOre() {
        return fasciaOre;
    }

    public void setFasciaOre(String fasciaOre) {
        this.fasciaOre = fasciaOre;
    }

    public List<Fenomeni> getFenomeni() {
        return fenomeni;
    }

    public void setFenomeni(List<Fenomeni> fenomeni) {
        this.fenomeni = fenomeni;
    }

}
