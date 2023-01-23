package dev.fhtw.oode.weatherapp.model;


import java.util.Date;
import java.util.List;

public class WeatherEntity {

    private String temperature;
    private String location;
    private String pressure;
    private String temp_min;
    private String temp_max;
    private String date;

    private List<String> minAndMaxTemps;

    public WeatherEntity(List<String> minAndMaxTemps) {
        this.minAndMaxTemps = minAndMaxTemps;
    }

    public WeatherEntity(String temperature, String location, String pressure, String temp_min, String temp_max, String date) {
        this.temperature = temperature;
        this.location = location;
        this.pressure = pressure;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.date = date;
        //this.minAndMaxTemps = getMinAndMaxTemps();
    }

    public WeatherEntity() {}

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getMinAndMaxTemps() {
        return minAndMaxTemps;
    }

    public void setMinAndMaxTemps(List<String> minAndMaxTemps) {
        this.minAndMaxTemps = minAndMaxTemps;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "temperature='" + temperature + '\'' +
                ", location='" + location + '\'' +
                ", pressure='" + pressure + '\'' +
                ", temp_min='" + temp_min + '\'' +
                ", temp_max='" + temp_max + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
