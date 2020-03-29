package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirezioneVento {

    @SerializedName("api_direzione_vento")
    @Expose
    private String apiDirezioneVento;
    @SerializedName("id_sensore")
    @Expose
    private Integer idSensore;

    public String getApiDirezioneVento() {
        return apiDirezioneVento;
    }

    public void setApiDirezioneVento(String apiDirezioneVento) {
        this.apiDirezioneVento = apiDirezioneVento;
    }

    public Integer getIdSensore() {
        return idSensore;
    }

    public void setIdSensore(Integer idSensore) {
        this.idSensore = idSensore;
    }

}
