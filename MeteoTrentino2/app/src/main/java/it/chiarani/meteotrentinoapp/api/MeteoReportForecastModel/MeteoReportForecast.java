package it.chiarani.meteotrentinoapp.api.MeteoReportForecastModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MeteoReportForecast {
    @SerializedName("180")
    @Expose
    private Map<String, MeteoReportForecastEntry> hourly;

    @SerializedName("1440")
    @Expose
    private Map<String, MeteoReportForecastEntry> daily;

    public Map<String, MeteoReportForecastEntry> getHourly() {
        return hourly;
    }

    public void setHourly(Map<String, MeteoReportForecastEntry> hourly) {
        this.hourly = hourly;
    }

    public Map<String, MeteoReportForecastEntry> getDaily() {
        return daily;
    }

    public void setDaily(Map<String, MeteoReportForecastEntry> daily) {
        this.daily = daily;
    }
}
