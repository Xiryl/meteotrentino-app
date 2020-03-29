package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Idrometro {

    @SerializedName("api_idrometro")
    @Expose
    private String apiIdrometro;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiIdrometro() {
        return apiIdrometro;
    }

    public void setApiIdrometro(String apiIdrometro) {
        this.apiIdrometro = apiIdrometro;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}