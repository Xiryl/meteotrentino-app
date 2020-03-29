package it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "MeteoTrentinoForecast")
public class MeteoTrentinoForecast {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("fonte-da-citare")
    @Expose
    private String fonteDaCitare;
    @SerializedName("codice-ipa-titolare")
    @Expose
    private String codiceIpaTitolare;
    @SerializedName("nome-titolare")
    @Expose
    private String nomeTitolare;
    @SerializedName("codice-ipa-editore")
    @Expose
    private String codiceIpaEditore;
    @SerializedName("nome-editore")
    @Expose
    private String nomeEditore;
    @SerializedName("dataPubblicazione")
    @Expose
    private String dataPubblicazione;
    @SerializedName("idPrevisione")
    @Expose
    private Integer idPrevisione;

    @SerializedName("evoluzione")
    @Expose
    private String evoluzione;
    @SerializedName("evoluzioneBreve")
    @Expose
    private String evoluzioneBreve;

    /*
    @SerializedName("AllerteList")
    @Expose
    private List<Object> allerteList = null;*/

    @SerializedName("previsione")
    @Expose
    private List<Previsione> previsione = null;

    public String getFonteDaCitare() {
        return fonteDaCitare;
    }

    public void setFonteDaCitare(String fonteDaCitare) {
        this.fonteDaCitare = fonteDaCitare;
    }

    public String getCodiceIpaTitolare() {
        return codiceIpaTitolare;
    }

    public void setCodiceIpaTitolare(String codiceIpaTitolare) {
        this.codiceIpaTitolare = codiceIpaTitolare;
    }

    public String getNomeTitolare() {
        return nomeTitolare;
    }

    public void setNomeTitolare(String nomeTitolare) {
        this.nomeTitolare = nomeTitolare;
    }

    public String getCodiceIpaEditore() {
        return codiceIpaEditore;
    }

    public void setCodiceIpaEditore(String codiceIpaEditore) {
        this.codiceIpaEditore = codiceIpaEditore;
    }

    public String getNomeEditore() {
        return nomeEditore;
    }

    public void setNomeEditore(String nomeEditore) {
        this.nomeEditore = nomeEditore;
    }

    public String getDataPubblicazione() {
        return dataPubblicazione;
    }

    public void setDataPubblicazione(String dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    public Integer getIdPrevisione() {
        return idPrevisione;
    }

    public void setIdPrevisione(Integer idPrevisione) {
        this.idPrevisione = idPrevisione;
    }

    public String getEvoluzione() {
        return evoluzione;
    }

    public void setEvoluzione(String evoluzione) {
        this.evoluzione = evoluzione;
    }

    public String getEvoluzioneBreve() {
        return evoluzioneBreve;
    }

    public void setEvoluzioneBreve(String evoluzioneBreve) {
        this.evoluzioneBreve = evoluzioneBreve;
    }

    /*
    public List<Object> getAllerteList() {
        return allerteList;
    }

    public void setAllerteList(List<Object> allerteList) {
        this.allerteList = allerteList;
    }*/

    public List<Previsione> getPrevisione() {
        return previsione;
    }

    public void setPrevisione(List<Previsione> previsione) {
        this.previsione = previsione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
