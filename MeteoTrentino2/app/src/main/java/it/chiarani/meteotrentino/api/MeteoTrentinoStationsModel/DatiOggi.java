package it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "datiOggi")
public class DatiOggi
{
    @Element(name = "rain")
    private String rain;

    @Element(name = "xmlns", required = false)
    private String xmlns;

    @Element(name = "umidita_relativa", required = false)
    private String umidita_relativa;

    @Element(name = "data")
    private String data;

    @Element(name = "tmax", required = false)
    private String tmax;

    @Element(name = "tmin", required = false)
    private String tmin;

    @Element(name = "temperature")
    private Temperature temperature;

    @Element(name = "precipitazioni")
    private Precipitazioni precipitazioni;

    @Element(name = "venti", required = false)
    private String venti;

    @Element(name = "radiazione", required = false)
    private String radiazione;

    public String getRain ()
    {
        return rain;
    }

    public void setRain (String rain)
    {
        this.rain = rain;
    }

    public String getXmlns ()
    {
        return xmlns;
    }

    public void setXmlns (String xmlns)
    {
        this.xmlns = xmlns;
    }

    public String getUmidita_relativa ()
    {
        return umidita_relativa;
    }

    public void setUmidita_relativa (String umidita_relativa)
    {
        this.umidita_relativa = umidita_relativa;
    }

    public String getData ()
    {
        return data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    public String getTmax ()
    {
        return tmax;
    }

    public void setTmax (String tmax)
    {
        this.tmax = tmax;
    }

    public String getTmin ()
    {
        return tmin;
    }

    public void setTmin (String tmin)
    {
        this.tmin = tmin;
    }

    public Temperature getTemperature ()
    {
        return temperature;
    }

    public void setTemperature (Temperature temperature)
    {
        this.temperature = temperature;
    }

    public Precipitazioni getPrecipitazioni ()
    {
        return precipitazioni;
    }

    public void setPrecipitazioni (Precipitazioni precipitazioni)
    {
        this.precipitazioni = precipitazioni;
    }

    public String getVenti ()
    {
        return venti;
    }

    public void setVenti (String venti)
    {
        this.venti = venti;
    }

    public String getRadiazione ()
    {
        return radiazione;
    }

    public void setRadiazione (String radiazione)
    {
        this.radiazione = radiazione;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rain = "+rain+", xmlns = "+xmlns+", umidita_relativa = "+umidita_relativa+", data = "+data+", tmax = "+tmax+", tmin = "+tmin+", temperature = "+temperature+", precipitazioni = "+precipitazioni+", venti = "+venti+", radiazione = "+radiazione+"]";
    }
}
