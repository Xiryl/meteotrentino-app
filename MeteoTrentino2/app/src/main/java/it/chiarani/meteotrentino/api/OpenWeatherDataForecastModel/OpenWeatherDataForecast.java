package it.chiarani.meteotrentino.api.OpenWeatherDataForecastModel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "OpenWeatherDataForecast")
public class OpenWeatherDataForecast {

    @PrimaryKey(autoGenerate = true)
    private int id_p_key;

    @SerializedName("coord")
    @Expose
    private OpenWeatherDataCoords coord;
    @SerializedName("weather")
    @Expose
    private List<OpenWeatherDataWeather> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private OpenWeatherDataMain main;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("wind")
    @Expose
    private OpenWeatherDataWind wind;
    @SerializedName("clouds")
    @Expose
    private OpenWeatherDataClouds clouds;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private OpenWeatherDataSys sys;
    @SerializedName("timezone")
    @Expose
    private Integer timezone;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;

    public OpenWeatherDataCoords getCoord() {
        return coord;
    }

    public void setCoord(OpenWeatherDataCoords coord) {
        this.coord = coord;
    }

    public List<OpenWeatherDataWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<OpenWeatherDataWeather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public OpenWeatherDataMain getMain() {
        return main;
    }

    public void setMain(OpenWeatherDataMain main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public OpenWeatherDataWind getWind() {
        return wind;
    }

    public void setWind(OpenWeatherDataWind wind) {
        this.wind = wind;
    }

    public OpenWeatherDataClouds getClouds() {
        return clouds;
    }

    public void setClouds(OpenWeatherDataClouds clouds) {
        this.clouds = clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public OpenWeatherDataSys getSys() {
        return sys;
    }

    public void setSys(OpenWeatherDataSys sys) {
        this.sys = sys;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public int getId_p_key() {
        return id_p_key;
    }

    public void setId_p_key(int id_p_key) {
        this.id_p_key = id_p_key;
    }
}