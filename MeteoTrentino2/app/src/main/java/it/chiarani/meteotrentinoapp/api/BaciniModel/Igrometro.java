package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Igrometro {

    @SerializedName("api_igrometro")
    @Expose
    private String apiIgrometro;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiIgrometro() {
        return apiIgrometro;
    }

    public void setApiIgrometro(String apiIgrometro) {
        this.apiIgrometro = apiIgrometro;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}
