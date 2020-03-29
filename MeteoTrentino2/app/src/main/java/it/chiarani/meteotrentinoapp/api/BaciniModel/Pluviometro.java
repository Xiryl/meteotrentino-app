package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pluviometro {

    @SerializedName("api_pluviometro")
    @Expose
    private String apiPluviometro;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiPluviometro() {
        return apiPluviometro;
    }

    public void setApiPluviometro(String apiPluviometro) {
        this.apiPluviometro = apiPluviometro;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}
