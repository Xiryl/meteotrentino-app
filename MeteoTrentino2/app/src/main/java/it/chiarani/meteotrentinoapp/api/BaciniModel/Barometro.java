package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Barometro {

    @SerializedName("api_barometro")
    @Expose
    private String apiBarometro;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiBarometro() {
        return apiBarometro;
    }

    public void setApiBarometro(String apiBarometro) {
        this.apiBarometro = apiBarometro;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}
