package dev.fhtw.oode.weatherapp;

import dev.fhtw.oode.weatherapp.client.OpenWeatherClient;
import dev.fhtw.oode.weatherapp.client.OpenWeatherConfigReader;
import dev.fhtw.oode.weatherapp.model.WeatherEntity;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;

public class WeatherApplication extends Application {
    @Override
    public void start(Stage mainstage) throws IOException {


        FXMLLoader mainstage_fxmlLoader = new FXMLLoader(WeatherApplication.class.getResource("frm_weatherappmain.fxml"));
        Scene main_scene = new Scene(mainstage_fxmlLoader.load(), 800,400);
        mainstage.setTitle("WeatherX");
        mainstage.setScene(main_scene);
        mainstage.show();

    }

    public static void main(String[] args) throws InterruptedException {
        launch();
    }
}