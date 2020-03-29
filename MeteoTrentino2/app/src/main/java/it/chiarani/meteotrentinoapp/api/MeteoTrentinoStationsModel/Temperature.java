package it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "temperature")
public class Temperature
{
    @ElementList(name = "temperatura_aria",  inline = true)
    private List<TemperaturaAria> temperaturaAria;

    public List<TemperaturaAria> temperaturaAria ()
    {
        return temperaturaAria;
    }

    public void SettemperaturaAria (List<TemperaturaAria> temperaturaAria)
    {
        this.temperaturaAria = temperaturaAria;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [temperatura_aria = "+temperaturaAria+"]";
    }
}
