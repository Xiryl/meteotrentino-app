package it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel;

import androidx.room.Entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "Previsione")
public class Previsione {
    @SerializedName("localita")
    @Expose
    private String localita;
    @SerializedName("quota")
    @Expose
    private Integer quota;
    @SerializedName("giorni")
    @Expose
    private List<Giorno> giorni = null;

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public List<Giorno> getGiorni() {
        return giorni;
    }

    public void setGiorni(List<Giorno> giorni) {
        this.giorni = giorni;
    }
}
