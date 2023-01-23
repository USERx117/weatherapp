package dev.fhtw.oode.weatherapp.model;

public class WeatherForecastEntityAll {

    /**
     * current date of forecast entity
     */
    private String date;

    /**
     * current min temperature
     */
    private String temp_min;

    /**
     * current max temperature
     */
    private String temp_max;

    public WeatherForecastEntityAll(String date, String temp_min, String temp_max) {
        this.date = date;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "WeatherForecastEntityAll{" +
                "date='" + date + '\'' +
                ", temp_min='" + temp_min + '\'' +
                ", temp_max='" + temp_max + '\'' +
                '}';
    }
}
