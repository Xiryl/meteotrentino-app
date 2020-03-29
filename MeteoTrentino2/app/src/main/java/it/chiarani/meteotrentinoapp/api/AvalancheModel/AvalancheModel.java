package it.chiarani.meteotrentinoapp.api.AvalancheModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvalancheModel {

    @SerializedName("elevation")
    @Expose
    private Integer elevation;
    @SerializedName("tendencyCommentTextcat")
    @Expose
    private String tendencyCommentTextcat;
    @SerializedName("regions")
    @Expose
    private List<String> regions = null;
    @SerializedName("tendency")
    @Expose
    private String tendency;
    @SerializedName("forenoon")
    @Expose
    private Forenoon forenoon;
    @SerializedName("snowpackStructureHighlights")
    @Expose
    private List<SnowpackStructureHighlight> snowpackStructureHighlights = null;
    @SerializedName("hasElevationDependency")
    @Expose
    private Boolean hasElevationDependency;
    @SerializedName("avActivityHighlightsTextcat")
    @Expose
    private String avActivityHighlightsTextcat;
    @SerializedName("hasDaytimeDependency")
    @Expose
    private Boolean hasDaytimeDependency;
    @SerializedName("avActivityCommentTextcat")
    @Expose
    private String avActivityCommentTextcat;
    @SerializedName("avActivityComment")
    @Expose
    private List<AvActivityComment> avActivityComment = null;
    @SerializedName("tendencyComment")
    @Expose
    private List<TendencyComment> tendencyComment = null;
    @SerializedName("dangerPattern1")
    @Expose
    private String dangerPattern1;
    @SerializedName("avActivityHighlights")
    @Expose
    private List<AvActivityHighlight> avActivityHighlights = null;
    @SerializedName("snowpackStructureComment")
    @Expose
    private List<SnowpackStructureComment> snowpackStructureComment = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("validity")
    @Expose
    private Validity validity;
    @SerializedName("snowpackStructureCommentTextcat")
    @Expose
    private String snowpackStructureCommentTextcat;
    @SerializedName("publicationDate")
    @Expose
    private String publicationDate;
    @SerializedName("dangerPattern2")
    @Expose
    private String dangerPattern2;

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public String getTendencyCommentTextcat() {
        return tendencyCommentTextcat;
    }

    public void setTendencyCommentTextcat(String tendencyCommentTextcat) {
        this.tendencyCommentTextcat = tendencyCommentTextcat;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public String getTendency() {
        return tendency;
    }

    public void setTendency(String tendency) {
        this.tendency = tendency;
    }

    public Forenoon getForenoon() {
        return forenoon;
    }

    public void setForenoon(Forenoon forenoon) {
        this.forenoon = forenoon;
    }

    public List<SnowpackStructureHighlight> getSnowpackStructureHighlights() {
        return snowpackStructureHighlights;
    }

    public void setSnowpackStructureHighlights(List<SnowpackStructureHighlight> snowpackStructureHighlights) {
        this.snowpackStructureHighlights = snowpackStructureHighlights;
    }

    public Boolean getHasElevationDependency() {
        return hasElevationDependency;
    }

    public void setHasElevationDependency(Boolean hasElevationDependency) {
        this.hasElevationDependency = hasElevationDependency;
    }

    public String getAvActivityHighlightsTextcat() {
        return avActivityHighlightsTextcat;
    }

    public void setAvActivityHighlightsTextcat(String avActivityHighlightsTextcat) {
        this.avActivityHighlightsTextcat = avActivityHighlightsTextcat;
    }

    public Boolean getHasDaytimeDependency() {
        return hasDaytimeDependency;
    }

    public void setHasDaytimeDependency(Boolean hasDaytimeDependency) {
        this.hasDaytimeDependency = hasDaytimeDependency;
    }

    public String getAvActivityCommentTextcat() {
        return avActivityCommentTextcat;
    }

    public void setAvActivityCommentTextcat(String avActivityCommentTextcat) {
        this.avActivityCommentTextcat = avActivityCommentTextcat;
    }

    public List<AvActivityComment> getAvActivityComment() {
        return avActivityComment;
    }

    public void setAvActivityComment(List<AvActivityComment> avActivityComment) {
        this.avActivityComment = avActivityComment;
    }

    public List<TendencyComment> getTendencyComment() {
        return tendencyComment;
    }

    public void setTendencyComment(List<TendencyComment> tendencyComment) {
        this.tendencyComment = tendencyComment;
    }

    public String getDangerPattern1() {
        return dangerPattern1;
    }

    public void setDangerPattern1(String dangerPattern1) {
        this.dangerPattern1 = dangerPattern1;
    }

    public List<AvActivityHighlight> getAvActivityHighlights() {
        return avActivityHighlights;
    }

    public void setAvActivityHighlights(List<AvActivityHighlight> avActivityHighlights) {
        this.avActivityHighlights = avActivityHighlights;
    }

    public List<SnowpackStructureComment> getSnowpackStructureComment() {
        return snowpackStructureComment;
    }

    public void setSnowpackStructureComment(List<SnowpackStructureComment> snowpackStructureComment) {
        this.snowpackStructureComment = snowpackStructureComment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public String getSnowpackStructureCommentTextcat() {
        return snowpackStructureCommentTextcat;
    }

    public void setSnowpackStructureCommentTextcat(String snowpackStructureCommentTextcat) {
        this.snowpackStructureCommentTextcat = snowpackStructureCommentTextcat;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDangerPattern2() {
        return dangerPattern2;
    }

    public void setDangerPattern2(String dangerPattern2) {
        this.dangerPattern2 = dangerPattern2;
    }

}