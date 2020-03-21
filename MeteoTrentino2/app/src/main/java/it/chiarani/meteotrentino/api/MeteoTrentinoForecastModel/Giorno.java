package it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Giorno {
    @SerializedName("idPrevisioneGiorno")
    @Expose
    private Integer idPrevisioneGiorno;
    @SerializedName("giorno")
    @Expose
    private String giorno;
    @SerializedName("idIcona")
    @Expose
    private Integer idIcona;
    @SerializedName("icona")
    @Expose
    private String icona;
    @SerializedName("descIcona")
    @Expose
    private String descIcona;
    @SerializedName("testoGiorno")
    @Expose
    private String testoGiorno;
    @SerializedName("tMinGiorno")
    @Expose
    private Integer tMinGiorno;
    @SerializedName("tMaxGiorno")
    @Expose
    private Integer tMaxGiorno;
    @SerializedName("fasce")
    @Expose
    private List<Fascia> fasce = null;

    public Integer getIdPrevisioneGiorno() {
        return idPrevisioneGiorno;
    }

    public void setIdPrevisioneGiorno(Integer idPrevisioneGiorno) {
        this.idPrevisioneGiorno = idPrevisioneGiorno;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public Integer getIdIcona() {
        return idIcona;
    }

    public void setIdIcona(Integer idIcona) {
        this.idIcona = idIcona;
    }

    public String getIcona() {
        return icona;
    }

    public void setIcona(String icona) {
        this.icona = icona;
    }

    public String getDescIcona() {
        return descIcona;
    }

    public void setDescIcona(String descIcona) {
        this.descIcona = descIcona;
    }

    public String getTestoGiorno() {
        return testoGiorno;
    }

    public void setTestoGiorno(String testoGiorno) {
        this.testoGiorno = testoGiorno;
    }

    public Integer getTMinGiorno() {
        return tMinGiorno;
    }

    public void setTMinGiorno(Integer tMinGiorno) {
        this.tMinGiorno = tMinGiorno;
    }

    public Integer getTMaxGiorno() {
        return tMaxGiorno;
    }

    public void setTMaxGiorno(Integer tMaxGiorno) {
        this.tMaxGiorno = tMaxGiorno;
    }

    public List<Fascia> getFasce() {
        return fasce;
    }

    public void setFasce(List<Fascia> fasce) {
        this.fasce = fasce;
    }
}
