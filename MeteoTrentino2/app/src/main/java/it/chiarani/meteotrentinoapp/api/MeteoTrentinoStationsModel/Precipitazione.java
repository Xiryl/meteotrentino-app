package it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "precipitazione")
public class Precipitazione
{
    @Element(name = "data")
    private String data;

    @Attribute(name = "UM")
    private String UM;

    @Element(name = "pioggia")
    private String pioggia;

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

    public String getPioggia ()
    {
        return pioggia;
    }

    public void setPioggia (String pioggia)
    {
        this.pioggia = pioggia;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", UM = "+UM+", pioggia = "+pioggia+"]";
    }
}


