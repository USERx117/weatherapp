package dev.fhtw.oode.weatherapp.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigUpdater {

    public void writeToProperties(String key, String value) {

        File file = new File("src/test.config");

        try {

            FileReader reader = new FileReader(file);
            Properties p = new Properties();

            p.load(reader);
            p.setProperty(key, value);
            p.store(System.out, "Properties updated!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
