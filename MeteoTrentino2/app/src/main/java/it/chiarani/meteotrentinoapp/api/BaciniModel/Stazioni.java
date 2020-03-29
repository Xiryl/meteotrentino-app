package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stazioni {

    @SerializedName("nome_stazione")
    @Expose
    private String nomeStazione;
    @SerializedName("sensori")
    @Expose
    private Sensori sensori;
    @SerializedName("ente_proprietario")
    @Expose
    private String enteProprietario;
    @SerializedName("api_stazione")
    @Expose
    private String apiStazione;

    public String getNomeStazione() {
        return nomeStazione;
    }

    public void setNomeStazione(String nomeStazione) {
        this.nomeStazione = nomeStazione;
    }

    public Sensori getSensori() {
        return sensori;
    }

    public void setSensori(Sensori sensori) {
        this.sensori = sensori;
    }

    public String getEnteProprietario() {
        return enteProprietario;
    }

    public void setEnteProprietario(String enteProprietario) {
        this.enteProprietario = enteProprietario;
    }

    public String getApiStazione() {
        return apiStazione;
    }

    public void setApiStazione(String apiStazione) {
        this.apiStazione = apiStazione;
    }

}
