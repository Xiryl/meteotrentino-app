package it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "precipitazioni")
public class Precipitazioni
{
    @ElementList(name = "precipitazioni", inline = true)
    private List<Precipitazione> precipitazione;

    public List<Precipitazione> getPrecipitazione ()
    {
        return precipitazione;
    }

    public void setPrecipitazione (List<Precipitazione> precipitazione)
    {
        this.precipitazione = precipitazione;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [precipitazione = "+precipitazione+"]";
    }
}
