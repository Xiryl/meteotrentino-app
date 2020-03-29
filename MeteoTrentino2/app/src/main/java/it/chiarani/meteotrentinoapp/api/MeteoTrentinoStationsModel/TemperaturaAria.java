package it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "temperatura_aria")
public class TemperaturaAria
{
    @Element(name = "data")
    private String data;

    @Attribute(name = "UM")
    private String UM;

    @Element(name = "temperatura")
    private String temperatura;

    public String getData ()
    {
        return data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    public String getUM ()
    {
        return UM;
    }

    public void setUM (String UM)
    {
        this.UM = UM;
    }

    public String getTemperatura ()
    {
        return temperatura;
    }

    public void setTemperatura (String temperatura)
    {
        this.temperatura = temperatura;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", UM = "+UM+", temperatura = "+temperatura+"]";
    }
}

