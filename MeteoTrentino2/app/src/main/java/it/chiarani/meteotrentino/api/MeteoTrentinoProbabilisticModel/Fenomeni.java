package it.chiarani.meteotrentino.api.MeteoTrentinoProbabilisticModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fenomeni {

    @SerializedName("fenomeno")
    @Expose
    private String fenomeno;
    @SerializedName("valore")
    @Expose
    private Integer valore;
    @SerializedName("probabilita")
    @Expose
    private String probabilita;

    public String getFenomeno() {
        return fenomeno;
    }

    public void setFenomeno(String fenomeno) {
        this.fenomeno = fenomeno;
    }

    public Integer getValore() {
        return valore;
    }

    public void setValore(Integer valore) {
        this.valore = valore;
    }

    public String getProbabilita() {
        return probabilita;
    }

    public void setProbabilita(String probabilita) {
        this.probabilita = probabilita;
    }

}
