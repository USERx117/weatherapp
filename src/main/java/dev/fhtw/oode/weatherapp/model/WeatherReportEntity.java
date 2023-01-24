package dev.fhtw.oode.weatherapp.model;

import java.util.ArrayList;
import java.util.List;

public class WeatherReportEntity {

    /**
     * current WeatherEntity
     */
    private WeatherEntity current;

    /**
     * List of Forecast Entities
     */
    private ArrayList<WeatherEntity> forecast;

    /**
     * List of min and max temperatures
     */
    private List<String> forecastMinAndMaxTemperatures;

    public List<String> getForecastMinAndMaxTemperatures() {
        return forecastMinAndMaxTemperatures;
    }

    public void setForecastMinAndMaxTemperatures(List<String> forecastMinAndMaxTemperatures) {
        this.forecastMinAndMaxTemperatures = forecastMinAndMaxTemperatures;
    }

    public WeatherEntity getCurrent() {
        return current;
    }

    public void setCurrent(WeatherEntity current) {
        this.current = current;
    }

    public ArrayList<WeatherEntity> getForecast() {
        return forecast;
    }

    public void setForecast(ArrayList<WeatherEntity> forecast) {
        this.forecast = forecast;
    }

}
