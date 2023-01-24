module dev.fhtw.oode.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.apache.commons.lang3;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires org.json;


    opens dev.fhtw.oode.weatherapp to javafx.fxml;
    exports dev.fhtw.oode.weatherapp;
    exports dev.fhtw.oode.weatherapp.controller;
    opens dev.fhtw.oode.weatherapp.controller to javafx.fxml;
    exports dev.fhtw.oode.weatherapp.model;
    opens dev.fhtw.oode.weatherapp.model to javafx.fxml;
}