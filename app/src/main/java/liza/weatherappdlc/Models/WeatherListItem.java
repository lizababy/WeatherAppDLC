
package liza.weatherappdlc.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherListItem {

    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("main")
    @Expose
    private WeatherMain weatherMain;
    @SerializedName("weather")
    @Expose
    private ArrayList<WeatherSub> weatherSub = null;
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;
    public ArrayList<WeatherSub> getWeatherSub() {
        return weatherSub;
    }

    public void setWeather(ArrayList<WeatherSub> weatherSub) {
        this.weatherSub = weatherSub;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public WeatherMain getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(WeatherMain weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

}
