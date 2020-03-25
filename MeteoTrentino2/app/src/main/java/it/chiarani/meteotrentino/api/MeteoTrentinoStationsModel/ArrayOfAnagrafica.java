package it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ArrayOfAnagrafica")
public class ArrayOfAnagrafica
{
    @Element(name = "xmlns", required = false)
    private String xmlns;

    @ElementList(name = "anagrafica", inline = true, required = false)
    private List<Anagrafica> anagrafica;

    public String getXmlns ()
    {
        return xmlns;
    }

    public void setXmlns (String xmlns)
    {
        this.xmlns = xmlns;
    }

    public List<Anagrafica> getAnagrafica ()
    {
        return anagrafica;
    }

    public void setAnagrafica (List<Anagrafica> anagrafica)
    {
        this.anagrafica = anagrafica;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [xmlns = "+xmlns+", anagrafica = "+anagrafica+"]";
    }
}

