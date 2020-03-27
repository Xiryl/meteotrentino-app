package it.chiarani.meteotrentino.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nivometro {

    @SerializedName("api_nivometro")
    @Expose
    private String apiNivometro;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiNivometro() {
        return apiNivometro;
    }

    public void setApiNivometro(String apiNivometro) {
        this.apiNivometro = apiNivometro;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}
