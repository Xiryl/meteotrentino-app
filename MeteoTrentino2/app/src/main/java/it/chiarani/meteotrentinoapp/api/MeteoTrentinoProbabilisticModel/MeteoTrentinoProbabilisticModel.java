package it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeteoTrentinoProbabilisticModel {

    @SerializedName("dataPubblicazione")
    @Expose
    private String dataPubblicazione;
    @SerializedName("giorni")
    @Expose
    private List<Giorni> giorni = null;

    public String getDataPubblicazione() {
        return dataPubblicazione;
    }

    public void setDataPubblicazione(String dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    public List<Giorni> getGiorni() {
        return giorni;
    }

    public void setGiorni(List<Giorni> giorni) {
        this.giorni = giorni;
    }

}