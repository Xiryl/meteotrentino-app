package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Radiometro {

    @SerializedName("api_radiometro")
    @Expose
    private String apiRadiometro;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiRadiometro() {
        return apiRadiometro;
    }

    public void setApiRadiometro(String apiRadiometro) {
        this.apiRadiometro = apiRadiometro;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}
