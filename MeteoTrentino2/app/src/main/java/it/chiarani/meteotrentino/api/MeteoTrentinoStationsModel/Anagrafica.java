package it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "anagrafica")
public class Anagrafica
{
    @Element(name = "inizio")
    private String inizio;

    @Element(name = "latitudine")
    private String latitudine;

    @Element(name = "fine", required = false)
    private String fine;

    @Element(name = "quota")
    private String quota;

    @Element(name = "est")
    private String est;

    @Element(name = "north")
    private String north;

    @Element(name = "codice")
    private String codice;

    @Element(name = "nome")
    private String nome;

    @Element(name = "nomebreve")
    private String nomebreve;

    @Element(name = "longitudine")
    private String longitudine;

    public String getInizio ()
    {
        return inizio;
    }

    public void setInizio (String inizio)
    {
        this.inizio = inizio;
    }

    public String getLatitudine ()
    {
        return latitudine;
    }

    public void setLatitudine (String latitudine)
    {
        this.latitudine = latitudine;
    }

    public String getFine ()
    {
        return fine;
    }

    public void setFine (String fine)
    {
        this.fine = fine;
    }

    public String getQuota ()
    {
        return quota;
    }

    public void setQuota (String quota)
    {
        this.quota = quota;
    }

    public String getEst ()
    {
        return est;
    }

    public void setEst (String est)
    {
        this.est = est;
    }

    public String getNorth ()
    {
        return north;
    }

    public void setNorth (String north)
    {
        this.north = north;
    }

    public String getCodice ()
    {
        return codice;
    }

    public void setCodice (String codice)
    {
        this.codice = codice;
    }

    public String getNome ()
    {
        return nome;
    }

    public void setNome (String nome)
    {
        this.nome = nome;
    }

    public String getNomebreve ()
    {
        return nomebreve;
    }

    public void setNomebreve (String nomebreve)
    {
        this.nomebreve = nomebreve;
    }

    public String getLongitudine ()
    {
        return longitudine;
    }

    public void setLongitudine (String longitudine)
    {
        this.longitudine = longitudine;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [inizio = "+inizio+", latitudine = "+latitudine+", fine = "+fine+", quota = "+quota+", est = "+est+", north = "+north+", codice = "+codice+", nome = "+nome+", nomebreve = "+nomebreve+", longitudine = "+longitudine+"]";
    }
}
