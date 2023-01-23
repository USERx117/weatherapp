package dev.fhtw.oode.weatherapp.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class OpenWeatherConfigReader{

    private String configPath;

    public OpenWeatherConfigReader(String configPath) {
        this.configPath = configPath;
    }

    /**
     * Reads the properties from a specific filepath (e.g /src/app.config)
     * @return the Property of one line
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Properties getProperties() throws FileNotFoundException, IOException {
        Properties prop = new Properties();

        FileInputStream fis = new FileInputStream(this.configPath);
        prop.load(fis);

        return prop;
    }

}
