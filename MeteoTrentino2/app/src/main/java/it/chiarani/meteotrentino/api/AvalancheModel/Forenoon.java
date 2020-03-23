package it.chiarani.meteotrentino.api.AvalancheModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Forenoon {

    @SerializedName("dangerRatingBelow")
    @Expose
    private String dangerRatingBelow;
    @SerializedName("avalancheSituation2")
    @Expose
    private AvalancheSituation2 avalancheSituation2;
    @SerializedName("avalancheSituation1")
    @Expose
    private AvalancheSituation1 avalancheSituation1;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("dangerRatingAbove")
    @Expose
    private String dangerRatingAbove;

    public String getDangerRatingBelow() {
        return dangerRatingBelow;
    }

    public void setDangerRatingBelow(String dangerRatingBelow) {
        this.dangerRatingBelow = dangerRatingBelow;
    }

    public AvalancheSituation2 getAvalancheSituation2() {
        return avalancheSituation2;
    }

    public void setAvalancheSituation2(AvalancheSituation2 avalancheSituation2) {
        this.avalancheSituation2 = avalancheSituation2;
    }

    public AvalancheSituation1 getAvalancheSituation1() {
        return avalancheSituation1;
    }

    public void setAvalancheSituation1(AvalancheSituation1 avalancheSituation1) {
        this.avalancheSituation1 = avalancheSituation1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDangerRatingAbove() {
        return dangerRatingAbove;
    }

    public void setDangerRatingAbove(String dangerRatingAbove) {
        this.dangerRatingAbove = dangerRatingAbove;
    }

}
