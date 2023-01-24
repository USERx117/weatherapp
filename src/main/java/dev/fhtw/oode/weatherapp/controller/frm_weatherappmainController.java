package dev.fhtw.oode.weatherapp.controller;

import dev.fhtw.oode.weatherapp.WeatherApplication;
import dev.fhtw.oode.weatherapp.client.OpenWeatherClient;
import dev.fhtw.oode.weatherapp.client.OpenWeatherConfigReader;
import dev.fhtw.oode.weatherapp.model.Configuration;
import dev.fhtw.oode.weatherapp.model.WeatherEntity;

import dev.fhtw.oode.weatherapp.model.WeatherReportEntity;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class frm_weatherappmainController implements Initializable {

    private WeatherEntity weatherData;
    private List<String> reportData;

    @FXML
    private Label lbl_currTemp;
    @FXML
    private Label lbl_currLocation;
    @FXML
    private Label lbl_currDate;
    @FXML
    private Label lbl_currPressure;
    @FXML
    private Label lbl_currHigh;
    @FXML
    private Label lbl_currLow;
    @FXML
    private Label lbl_foreDateDayName1;
    @FXML
    private Label lbl_foreDateDayName2;
    @FXML
    private Label lbl_foreDateDayName3;
    @FXML
    private Label lbl_foreDateDayName4;
    @FXML
    private Label lbl_foreDateDayName5;
    @FXML
    private Label lbl_foreD1High;
    @FXML
    private Label lbl_foreD1Low;
    @FXML
    private Label lbl_foreD2High;
    @FXML
    private Label lbl_foreD2Low;
    @FXML
    private Label lbl_foreD3High;
    @FXML
    private Label lbl_foreD3Low;
    @FXML
    private Label lbl_foreD4High;
    @FXML
    private Label lbl_foreD4Low;
    @FXML
    private Label lbl_foreD5High;
    @FXML
    private Label lbl_foreD5Low;
    @FXML
    private Label lbl_title;
    @FXML
    private Button bt_Configure;
    @FXML
    private Button bt_Update;

    private long updateIntervalMinutes = 1;

    private long updateIntervalMinutes = 1;


    /**
     *
     * @param event configure button event handler
     */
    @FXML
    void bt_Configure_Clicked(MouseEvent event) {

        try {
            Stage stage_location = new Stage();
            FXMLLoader locationscene_fxmlLoader = new FXMLLoader(WeatherApplication.class.getResource("frm_configurate.fxml"));

            Scene scene_location = new Scene(locationscene_fxmlLoader.load(), 800,600);
            stage_location.setTitle("Location Finder");
            stage_location.setScene(scene_location);
            stage_location.show();

        } catch (IOException ignored) {

        }
    }

    /**
     *
     * @param event updateButton Event Handler
     */
    @FXML
    void bt_Update_Clicked(MouseEvent event) {
        initialize(null, null);
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(updateIntervalMinutes), e -> {
            initialize(null, null);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     *
     * @param url
     * @param resourceBundle
     * initialize method is called on startup to fetch weather data and write data to the labels of the window
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchWeatherData();


        Configuration curr_config = new Configuration();
        curr_config = Configuration.get_configuration();
        String unit_string = "C";

        if(curr_config.getUnits().equals("imperial"))
        {
            unit_string = "F";
        }

        this.lbl_currTemp.setText(bigDecimal(this.weatherData.getTemperature())+"°" + unit_string);
        this.lbl_currDate.setText(dateConvert(this.weatherData.getDate()));

        this.lbl_foreDateDayName1.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 1));
        this.lbl_foreDateDayName2.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 2));
        this.lbl_foreDateDayName3.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 3));
        this.lbl_foreDateDayName4.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 4));
        this.lbl_foreDateDayName5.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 5));

        this.lbl_currLocation.setText(this.weatherData.getLocation());

        this.lbl_currPressure.setText(this.weatherData.getPressure()+"mbar");
        this.lbl_currHigh.setText(bigDecimal(this.weatherData.getTemp_max())+"°" + unit_string);
        this.lbl_currLow.setText(bigDecimal(this.weatherData.getTemp_min())+"°" + unit_string);
        this.lbl_foreD1High.setText(bigDecimal(this.reportData.get(1))+"°" + unit_string);
        this.lbl_foreD1Low.setText(bigDecimal(this.reportData.get(0))+"°" + unit_string);
        //LocalDateTime current = LocalDateTime.parse(this.weatherData.getDate());
        //this.lbl_foreDateDayName1.setText(String.valueOf(LocalDateTime.from(current.toInstant(null)).plusDays(1)));
        this.lbl_foreD2High.setText(bigDecimal(this.reportData.get(3))+"°" + unit_string);
        this.lbl_foreD2Low.setText(bigDecimal(this.reportData.get(2))+"°" + unit_string);
        this.lbl_foreD3High.setText(bigDecimal(this.reportData.get(5))+"°" + unit_string);
        this.lbl_foreD3Low.setText(bigDecimal(this.reportData.get(4))+"°" + unit_string);
        this.lbl_foreD4High.setText(bigDecimal(this.reportData.get(7))+"°" + unit_string);
        this.lbl_foreD4Low.setText(bigDecimal(this.reportData.get(6))+"°" + unit_string);
        this.lbl_foreD5High.setText(bigDecimal(this.reportData.get(9))+"°" + unit_string);
        this.lbl_foreD5Low.setText(bigDecimal(this.reportData.get(8))+"°" + unit_string);
        //this.lbl_foreD1Low.setText(bigDecimal(this.reportData.getForecast().get(0).getMinAndMaxTemps().get(0)));
        //System.out.println(weatherData);
        //System.out.println(reportData);

        this.lbl_foreDateDayName1.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 1));
        this.lbl_foreDateDayName2.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 2));
        this.lbl_foreDateDayName3.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 3));
        this.lbl_foreDateDayName4.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 4));
        this.lbl_foreDateDayName5.setText(dateConvertFormatDayMonthPlusOffset(this.weatherData.getDate(), 5));


    }

    /**
     * uses the OpenWeatherConfigReader to read values from app.config
     * and fetches current weather data as well as min and max temperatures for forecast
     */
    private void fetchWeatherData() {
        try {
            OpenWeatherConfigReader owcr = new OpenWeatherConfigReader("src/app.config");
            Properties prop = owcr.getProperties();
            OpenWeatherClient owc = new OpenWeatherClient(
                    prop.getProperty("openweather_api_key"),
                    prop.getProperty("openweather_api_hostname"),
                    prop.getProperty("openweather_units"),
                    prop.getProperty("openweather_longitude"),
                    prop.getProperty("openweather_latitude"));
            updateIntervalMinutes = Long.parseLong(prop.getProperty("openweather_updateInterval"));
            this.weatherData = owc.connect().getCurrent();
            this.reportData = owc.connect().getForecastMinAndMaxTemperatures();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helperfunction, getting a string of a big value, converts it to BigDecimal and rounds it down
     * @param decimal String of the big number
     * @return String-value of the rounded value
     */
    private String bigDecimal(String decimal) {
        BigDecimal d = new BigDecimal(decimal).setScale(0, RoundingMode.DOWN);
        return String.valueOf(d);
    }

    /**
     * Gets a string of a date in milliseconds, converts it to long, adds 3-zeros and returns a formated date as string
     * @param currentDate String of the date in milliseconds
     * @return String of formatted date
     */
    private String dateConvert(String currentDate) {
        long converted = Long.parseLong(currentDate + "000");
        DateFormat obj = new SimpleDateFormat("E dd.MM.yyyy");
        Date res = new Date(converted);
        return obj.format(res);
    }

    /**
     * Gets a string of a date in milliseconds, converts it, formats it and offsets it by a given day
     * @param currentDate String of the date in milliseconds
     * @param offset How many days to offset the given date
     * @return String of formatted and offset date
     */
    private String dateConvertFormatDayMonthPlusOffset(String currentDate, long offset) {
        if (offset == 0){
            offset = 1;
        }
        long converted = Long.parseLong(currentDate + "000");
        converted += offset * 86400000;
        DateFormat obj = new SimpleDateFormat("E dd.MM");
        Date res = new Date(converted);
        return obj.format(res);
    }

    public void bt_ConfigureButtonClicked(ActionEvent actionEvent) {
    }

    public void bt_UpdateButtonClicked(ActionEvent actionEvent) {

    }
}
