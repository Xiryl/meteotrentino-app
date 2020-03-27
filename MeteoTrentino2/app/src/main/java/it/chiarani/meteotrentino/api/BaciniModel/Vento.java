package it.chiarani.meteotrentino.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vento {

    @SerializedName("api_vento")
    @Expose
    private String apiVento;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiVento() {
        return apiVento;
    }

    public void setApiVento(String apiVento) {
        this.apiVento = apiVento;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}