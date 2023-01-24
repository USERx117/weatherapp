package dev.fhtw.oode.weatherapp.model;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;
import java.util.concurrent.Callable;

public class Configuration {

    private String openweather_api_key;
    private String positionstack_api_key;
    private double longitude;
    private double latitude;
    private double update_interval;
    private String units;

    public String getUnits() {
        return units;
    }

    public Configuration() {
    }

    public String getOpenweather_api_key() {
        return openweather_api_key;
    }

    public void setOpenweather_api_key(String openweather_api_key) {
        this.openweather_api_key = openweather_api_key;
    }

    public String getPositionstack_api_key() {
        return positionstack_api_key;
    }

    public void setPositionstack_api_key(String positionstack_api_key) {
        this.positionstack_api_key = positionstack_api_key;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getUpdate_interval() {
        return update_interval;
    }

    public void setUpdate_interval(double update_interval) {
        this.update_interval = update_interval;
    }

    public String isUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int update_config(String property_label, String new_prop_value)
    {
        Configuration update_config = new Configuration();

        try {
            String configFilePath = "src/app.config";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);

            prop.setProperty(property_label, new_prop_value);
            prop.store(new FileWriter(configFilePath), "");

        } catch (Exception e)
        {
            return 1;
        }

        return 0;
    }


    public static Configuration get_configuration()
    {
        Configuration load_config = new Configuration();

        try {
        String configFilePath = "src/app.config";
        FileInputStream propsInput = new FileInputStream(configFilePath);
        Properties prop = new Properties();
        prop.load(propsInput);

        /**
         * Checking if the value is in the properties file - if not its is not going to be added to the Configuration
         * class that is returned;
         */
        if(prop.getProperty("openweather_api_key") != null){
            String test = prop.getProperty("openweather_api_key");
            load_config.setOpenweather_api_key(prop.getProperty("openweather_api_key"));
        }
        if(prop.getProperty("positionstack_api_key") != null) {
            load_config.setPositionstack_api_key(prop.getProperty("positionstack_api_key"));
        }
        if(prop.getProperty("openweather_longitude") != null) {
            load_config.setLongitude(Double.parseDouble(prop.getProperty("openweather_longitude")));
        }
        if(prop.getProperty("openweather_latitude") != null) {
            load_config.setLatitude(Double.parseDouble(prop.getProperty("openweather_latitude")));
        }
        if(prop.getProperty("openweather_updateInterval") != null) {
            load_config.setUpdate_interval(Double.parseDouble(prop.getProperty("openweather_updateInterval")));
        }
        if(prop.getProperty("openweather_units") != null) {
            load_config.setUnits(prop.getProperty("openweather_units"));
        }

    } catch (Exception e)
        {
            return null;
        }
        return load_config;
    }

}
