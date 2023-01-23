package dev.fhtw.oode.weatherapp.controller;

import dev.fhtw.oode.weatherapp.client.OpenWeatherConfigReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class frm_configurateController {

    @FXML
    private Button btn_save;
    @FXML
    private Button btn_close;
    @FXML
    private Label lbl_location;
    @FXML
    private Label lbl_apiKey;
    @FXML
    private Label lbl_configTitle;
    @FXML
    private RadioButton rbtn_metric;
    @FXML
    private RadioButton rbtn_imperial;
    @FXML
    private TextField tf_longitude;
    private String longitude;
    @FXML
    private TextField tf_latitude;
    private String latitude;
    @FXML
    private TextField tf_apiKey;
    private String apiKey;
    @FXML
    private TextField tf_interval;
    private String interval;
    @FXML
    private Label lbl_perMin;
    @FXML
    private Label lbl_updateInterval;

    /**
     * When the button "Save" is clicked in the configuration window, everything from the textfields will be stored as a property
     * @param event c
     */
    @FXML
    void btn_saveButtonClicked(ActionEvent event) {
        String unit = rbtn_metric.isSelected() ? "metric" : "imperial";
        longitude = tf_longitude.getText();
        latitude = tf_latitude.getText();
        apiKey = tf_apiKey.getText();
        interval = tf_interval.getText();

        try {
            FileWriter writer = new FileWriter("src/app.config");
            OpenWeatherConfigReader owcr = new OpenWeatherConfigReader("src/app.config");
            Properties prop = owcr.getProperties();
            prop.setProperty("openweather_api_key", apiKey);
            prop.setProperty("openweather_longitude", longitude);
            prop.setProperty("openweather_latitude", latitude);
            prop.setProperty("openweather_updateInterval", interval);
            prop.setProperty("openweather_api_hostname", "api.openweathermap.org");
            prop.setProperty("openweather_units", unit);

            prop.store(writer, "Properties updated!");
            btn_closeButtonClicked(event);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void rbtn_metric_Action(ActionEvent event) {

    }
    @FXML
    void rbtn_imperial_Action(ActionEvent event) {

    }

    public void btn_closeButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }
}
