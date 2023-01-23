package dev.fhtw.oode.weatherapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties({"type", "name", "number", "postal_code", "street", "confidence", "region", "region_code", "county", "locality", "neighbourhood", "country", "country_code", "continent"})

public class Location {
    @JsonProperty("label")
    public String getLabel() {
        return this.label; }
    public void setLabel(String label) {
        this.label = label; }
    String label;

    @JsonProperty("latitude")
    public double getLatitude() {
        return this.latitude; }
    public void setLatitude(double latitude) {
        this.latitude = latitude; }
    double latitude;

    @JsonProperty("longitude")
    public double getLongitude() {
        return this.longitude; }
    public void setLongitude(double longitude) {
        this.longitude = longitude; }
    double longitude;

    @JsonProperty("administrative_area")
    public String getAdministrative_area() {
        return this.administrative_area; }
    public void setAdministrative_area(String administrative_area) {
        this.administrative_area = administrative_area; }
    String administrative_area;

}
