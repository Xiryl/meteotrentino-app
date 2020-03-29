package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TemperaturaAria {

    @SerializedName("api_temperatura_aria")
    @Expose
    private String apiTemperaturaAria;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiTemperaturaAria() {
        return apiTemperaturaAria;
    }

    public void setApiTemperaturaAria(String apiTemperaturaAria) {
        this.apiTemperaturaAria = apiTemperaturaAria;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}
