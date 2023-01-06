package dev.fhtw.oode.weatherapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WeatherApplication extends Application {
    @Override
    public void start(Stage mainstage) throws IOException {

        FXMLLoader mainstage_fxmlLoader = new FXMLLoader(WeatherApplication.class.getResource("frm_weatherappmain.fxml"));
        Scene main_scene = new Scene(mainstage_fxmlLoader.load(), 800,400);
        mainstage.setTitle("Weather @ Location");
        mainstage.setScene(main_scene);
        mainstage.show();

        try {
            openWeather_API_request.API_call();
        }
        catch (Exception e)
        {

        }

    }

    public static void main(String[] args) {
        launch();
    }
}