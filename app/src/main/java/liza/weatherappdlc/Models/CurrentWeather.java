
package liza.weatherappdlc.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentWeather {

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private WeatherMain main;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }
    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }


    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
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


}
