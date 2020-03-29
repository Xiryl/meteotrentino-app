package it.chiarani.meteotrentinoapp.api.BaciniModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Portata {

    @SerializedName("api_portata")
    @Expose
    private String apiPortata;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiPortata() {
        return apiPortata;
    }

    public void setApiPortata(String apiPortata) {
        this.apiPortata = apiPortata;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}