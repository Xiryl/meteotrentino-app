package it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Giorni {

    @SerializedName("nomeGiorno")
    @Expose
    private String nomeGiorno;
    @SerializedName("giorno")
    @Expose
    private String giorno;
    @SerializedName("fasce")
    @Expose
    private List<Fasce> fasce = null;

    public String getNomeGiorno() {
        return nomeGiorno;
    }

    public void setNomeGiorno(String nomeGiorno) {
        this.nomeGiorno = nomeGiorno;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public List<Fasce> getFasce() {
        return fasce;
    }

    public void setFasce(List<Fasce> fasce) {
        this.fasce = fasce;
    }

}