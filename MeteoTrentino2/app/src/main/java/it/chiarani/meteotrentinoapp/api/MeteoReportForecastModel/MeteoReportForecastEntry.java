package it.chiarani.meteotrentinoapp.api.MeteoReportForecastModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeteoReportForecastEntry {
    @SerializedName("start")
    @Expose
    private String start;

    @SerializedName("end")
    @Expose
    private String end;

    @SerializedName("rain_fall")
    @Expose
    private Double rainFall;

    @SerializedName("wind_gust")
    @Expose
    private Double windGust;

    @SerializedName("fresh_snow")
    @Expose
    private Double freshSnow;

    @SerializedName("snow_level")
    @Expose
    private Integer snowLevel;

    @SerializedName("wind_speed")
    @Expose
    private Double windSpeed;

    @SerializedName("sky_condition")
    @Expose
    private String skyCondition;

    @SerializedName("freezing_level")
    @Expose
    private Integer freezingLevel;

    @SerializedName("wind_direction")
    @Expose
    private Integer windDirection;

    @SerializedName("rain_probability")
    @Expose
    private Integer rainProbability;

    @SerializedName("sunshine_duration")
    @Expose
    private Double sunshineDuration;

    @SerializedName("temperature_maximum")
    @Expose
    private Integer temperatureMaximum;

    @SerializedName("temperature_minimum")
    @Expose
    private Integer temperatureMinimum;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Double getRainFall() {
        return rainFall;
    }

    public void setRainFall(Double rainFall) {
        this.rainFall = rainFall;
    }

    public Double getWindGust() {
        return windGust;
    }

    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }

    public Double getFreshSnow() {
        return freshSnow;
    }

    public void setFreshSnow(Double freshSnow) {
        this.freshSnow = freshSnow;
    }

    public Integer getSnowLevel() {
        return snowLevel;
    }

    public void setSnowLevel(Integer snowLevel) {
        this.snowLevel = snowLevel;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getSkyCondition() {
        return skyCondition;
    }

    public void setSkyCondition(String skyCondition) {
        this.skyCondition = skyCondition;
    }

    public Integer getFreezingLevel() {
        return freezingLevel;
    }

    public void setFreezingLevel(Integer freezingLevel) {
        this.freezingLevel = freezingLevel;
    }

    public Integer getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Integer windDirection) {
        this.windDirection = windDirection;
    }

    public Integer getRainProbability() {
        return rainProbability;
    }

    public void setRainProbability(Integer rainProbability) {
        this.rainProbability = rainProbability;
    }

    public Double getSunshineDuration() {
        return sunshineDuration;
    }

    public void setSunshineDuration(Double sunshineDuration) {
        this.sunshineDuration = sunshineDuration;
    }

    public Integer getTemperatureMaximum() {
        return temperatureMaximum;
    }

    public void setTemperatureMaximum(Integer temperatureMaximum) {
        this.temperatureMaximum = temperatureMaximum;
    }

    public Integer getTemperatureMinimum() {
        return temperatureMinimum;
    }

    public void setTemperatureMinimum(Integer temperatureMinimum) {
        this.temperatureMinimum = temperatureMinimum;
    }
}
