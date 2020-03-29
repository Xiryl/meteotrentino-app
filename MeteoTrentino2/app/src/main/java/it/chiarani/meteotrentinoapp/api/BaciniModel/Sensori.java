package it.chiarani.meteotrentinoapp.api.BaciniModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sensori {

    @SerializedName("pluviometro")
    @Expose
    private Pluviometro pluviometro;
    @SerializedName("idrometro")
    @Expose
    private Idrometro idrometro;
    @SerializedName("temperatura_aria")
    @Expose
    private TemperaturaAria temperaturaAria;
    @SerializedName("igrometro")
    @Expose
    private Igrometro igrometro;
    @SerializedName("nivometro")
    @Expose
    private Nivometro nivometro;
    @SerializedName("barometro")
    @Expose
    private Barometro barometro;
    @SerializedName("radiometro")
    @Expose
    private Radiometro radiometro;
    @SerializedName("vento")
    @Expose
    private Vento vento;
    @SerializedName("direzione_vento")
    @Expose
    private DirezioneVento direzioneVento;
    @SerializedName("portata")
    @Expose
    private Portata portata;

    public Pluviometro getPluviometro() {
        return pluviometro;
    }

    public void setPluviometro(Pluviometro pluviometro) {
        this.pluviometro = pluviometro;
    }

    public Idrometro getIdrometro() {
        return idrometro;
    }

    public void setIdrometro(Idrometro idrometro) {
        this.idrometro = idrometro;
    }

    public TemperaturaAria getTemperaturaAria() {
        return temperaturaAria;
    }

    public void setTemperaturaAria(TemperaturaAria temperaturaAria) {
        this.temperaturaAria = temperaturaAria;
    }

    public Igrometro getIgrometro() {
        return igrometro;
    }

    public void setIgrometro(Igrometro igrometro) {
        this.igrometro = igrometro;
    }

    public Nivometro getNivometro() {
        return nivometro;
    }

    public void setNivometro(Nivometro nivometro) {
        this.nivometro = nivometro;
    }

    public Barometro getBarometro() {
        return barometro;
    }

    public void setBarometro(Barometro barometro) {
        this.barometro = barometro;
    }

    public Radiometro getRadiometro() {
        return radiometro;
    }

    public void setRadiometro(Radiometro radiometro) {
        this.radiometro = radiometro;
    }

    public Vento getVento() {
        return vento;
    }

    public void setVento(Vento vento) {
        this.vento = vento;
    }

    public DirezioneVento getDirezioneVento() {
        return direzioneVento;
    }

    public void setDirezioneVento(DirezioneVento direzioneVento) {
        this.direzioneVento = direzioneVento;
    }

    public Portata getPortata() {
        return portata;
    }

    public void setPortata(Portata portata) {
        this.portata = portata;
    }

}
