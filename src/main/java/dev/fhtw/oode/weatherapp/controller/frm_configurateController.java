package dev.fhtw.oode.weatherapp.controller;

import dev.fhtw.oode.weatherapp.model.Configuration;
import dev.fhtw.oode.weatherapp.model.Location;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dev.fhtw.oode.weatherapp.model.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.JsonMappingException;


public class frm_configurateController {


    /***
     * The form is loaded and populated with the values found in the config file (app.config in src)
      */
    public void initialize()
    {
        /***
         * A new instance of the Configuration class is instantitated to hold the values obtained from the config file
         * Afterwards the form textfields are populated with the values;
         */
        Configuration new_config = Configuration.get_configuration();

        tb_locationAPIKey.setText(new_config.getPositionstack_api_key());
        tb_weatherAPIKey.setText(new_config.getOpenweather_api_key());
        tb_uprate_mm.setText(String.valueOf(new_config.getUpdate_interval()));

        /***
         * if the unit found is imperial the respecitve radio button is set.
         * In any other case the unit is set to metric;
         */
        if(new_config.getUnits().equals("imperial"))
        {
            rb_imperialunits.setSelected(true);
        } else
        {
            rb_metricunits.setSelected(true);
        }

    }

    @FXML
    private Button bt_searchLocation;

    @FXML
    private Button bt_selectLocation;

    @FXML
    private Button bt_update_uprate;

    @FXML
    private Button bt_updatelocationAPIKey;

    @FXML
    private Button bt_updateweatherAPIKey;

    @FXML
    private FontAwesomeIconView icn_search;

    @FXML
    private FontAwesomeIconView icn_update_rate;

    @FXML
    private Label lb_locsetup_log;

    @FXML
    private ListView<String> lv_Locations;

    @FXML
    private RadioButton rb_imperialunits;

    @FXML
    private RadioButton rb_metricunits;

    @FXML
    private TextField tb_locationAPIKey;

    @FXML
    private TextField tb_uprate_mm;

    @FXML
    private TextField tb_weatherAPIKey;

    @FXML
    private TextField tf_serachLocation;

    @FXML
    void bt_searchLocationButtonClicked(MouseEvent event) {

        /***
         * The Location-Search box is checked for values before the serach for
         * locations is started. Is the box empty a status update is issued via
         * the lable Log line in the config window
         */

        if(!tf_serachLocation.getText().isBlank()) {
            try {
                /***
                 * A new instance of LocationService is created and the Textbox tf_serachLocation
                 * and ListView<String> lv_Locations are handed over as paramaters. The LocationSerice
                 * class extends the Service<> Class and thus runs the operations required in a seperate
                 * thread to the GUI. Thus the object the need to be manipulated, i.e. the tf_serachLocation
                 * to obtain the Location Search String and lv_Locations to load the found Locations in.
                 * The LocationService is started to load the found locations in the listView in the form.
                 * The SerachString is set for the instance to be accessible for proteced methods.
                 */
                LocationService loc_serv = new LocationService(tf_serachLocation, lv_Locations);
                loc_serv.setSerachString(tf_serachLocation.getText().toString());
                loc_serv.start();

            } catch (Exception e) {

            }
        } else {
            lb_locsetup_log.setText("Please enter a city/location");
        }
    }

    /***
     * The bt_selectLocationButtonClicked function updates the selected location lat and long in the
     * config file app.configuration. It segregates the Strings from the ObservableList for Lat and Long
     * Values and forwards it to an instance of the Configuraiton class to update the values in the
     * config file.
     * @param event
     */
    @FXML
    void bt_selectLocationButtonClicked(MouseEvent event) {
        String location = lv_Locations.getSelectionModel().getSelectedItem();

        if(location != null) {
            Double new_lat = Double.parseDouble(location.substring(location.indexOf("Lat:") + 4, location.indexOf("|", location.indexOf("Lat:"))));
            Double new_lng = Double.parseDouble(location.substring(location.indexOf("Lng:") + 4, location.length()));

            Configuration update_conf = new Configuration();
            update_conf.update_config("openweather_latitude", String.valueOf(new_lat));
            update_conf.update_config("openweather_longitude", String.valueOf(new_lng));
        } else
        {
            lb_locsetup_log.setText("No Location selected");
        }
    }

    /**
     * bt_update_uprateButtonClicked creates an instance of the Configuration class to update
     * the update interval value in the config file.
     * @param event
     */
    @FXML
    void bt_update_uprateButtonClicked(MouseEvent event) {
        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_updateInterval", tb_uprate_mm.getText());
    }

    /***
     * bt_updatelocationAPIKey_ButtonClicked creates an instance of the Configuration class to update
     * the update positionstack_api_key value in the config file.
     * @param event
     */
    @FXML
    void bt_updatelocationAPIKey_ButtonClicked(MouseEvent event) {
        Configuration update_conf = new Configuration();
        update_conf.update_config("positionstack_api_key", tb_locationAPIKey.getText());
    }

    /***
     * bt_updateweatherAPIKey_ButtonClicked creates an instance of the Configuration class to update
     * the update openweather_api_key value in the config file.
     * @param event
     */
    @FXML
    void bt_updateweatherAPIKey_ButtonClicked(MouseEvent event) {
        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_api_key", tb_weatherAPIKey.getText());
    }

    @FXML
    void icn_searchButtonClicked(MouseEvent event) {

    }

    @FXML
    void icn_update_rateButtonClicked(MouseEvent event) {

    }

    /***
     * rb_imperialunits_Action creates an instance of the Configuration class to update
     * the update openweather_units value in the config file for imperial units
     * @param event
     */
    @FXML
    void rb_imperialunits_Action(ActionEvent event) {

        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_units", "imperial");
    }

    /***
     * rb_metricunits_Action creates an instance of the Configuration class to update
     * the update openweather_units value in the config file for metric units
     * @param event
     */
    @FXML
    void rb_metricunits_Action(ActionEvent event) {
        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_units", "metric");
    }

    @FXML
    void tb_uprate_mm_Action(ActionEvent event) {

    }

    /***
     * The LocationService provides the basic interface for the PositionStack API.
     * The class holds the only value searchString to perform a task in a single background thread
     */
    private class LocationService extends Service<ObservableList<String>> {
        private String serachString;

        public String getSerachString() {
            return serachString;
        }

        public void setSerachString(String serachString) {
            this.serachString = serachString;
        }

        /***
         * LocationService provides the basic structure to update the ListView<String> of locations in the config form
         * @param tb_searchString the textfield from which the to serach for location string is taken
         * @param lv_Locations the ListView the found location results are populated with
         */
        private LocationService(TextField tb_searchString, ListView<String> lv_Locations)
        {
            setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    setSerachString(tb_searchString.getText());
                }
            });
        }

        /***
         * The task run in a seperate thread in order to allow the GUI to work unobstructed
         * The task instantiates the APILocationService class to fetch a GET-Request to the
         * location database API. The results are safed in an OberservableList to be used
         * to populate the ListView in the config frm.
         * @return The function directly populates the ListView in the config frm but
         * returns an ObservableList to be further used in future features, i.e. showing
         * temperatures of multiple cites in alternating intervals.
         */
        @Override
        protected Task<ObservableList<String>> createTask()
        {
            String serachStng = this.getSerachString();
            return new Task<ObservableList<String>>()
            {
                @Override
                protected ObservableList<String> call() throws Exception {
                    APILocationService loc_serv = new APILocationService();
                    ObservableList<String> loc_list = FXCollections.observableArrayList();
                    loc_list = loc_serv.getObservabelLocationList(serachStng);
                    lv_Locations.setItems(loc_list);
                    return loc_list;
                }
            };
        }
    }


    /***
     * The APILocationService class provides the direct interface for the PositionStack API
     *
     */
    public static class APILocationService {

        /***
         * getObservabelLocationList return an ObservableList from a JSON String obtained from the
         * Positon Stack API. The funciton creates from the JSON-String a List of the Location class
         * and matches the found values via the Function mapLocationstoList (i.e. an ObjectMapper).
         * Afterwards the function creates a List<String> and poplutes it with the values form
         * the List.
         * @param serachLocation serachLocation String
         * @return An ObservabelList of Strings in the format Location Label | Location Administrative Area | Location Latitute | Location Longitue
         * @throws Exception
         */
        public ObservableList<String> getObservabelLocationList(String serachLocation) throws Exception {
            List<Location> local_loc_list = new ArrayList<Location>();
            ObservableList<String> locob_list = FXCollections.observableArrayList();

            try {
                local_loc_list = mapLocationstoList(getLocationsJsonString(serachLocation));

                for (Location temp : local_loc_list) {
                    locob_list.add(temp.getLabel() + " | " + temp.getAdministrative_area() + " | Lat:" + temp.getLatitude() + " | Lng:" + temp.getLongitude());
                }

            } catch (Exception e) {
                return null;
            }

            return locob_list;
        }

        /***
         * getLocationsJsonString used the LocationSerachString to issue an GET request to the
         * PositionStack API and uses the API-Key from the application configurfation file.
         * @param searchLocation the location name to be serach for
         * @return Returns a JSON in the format of a String
         * @throws IOException
         */
        public String getLocationsJsonString(String searchLocation) throws IOException {
            StringBuilder API_return = new StringBuilder();
            String bufferString;

            try {

                Configuration curr_config = new Configuration();
                curr_config = Configuration.get_configuration();

                StringBuilder request_string = new StringBuilder();
                request_string.append("http://api.positionstack.com/v1/forward?access_key=");
                request_string.append(curr_config.getPositionstack_api_key());
                request_string.append("&query=");
                request_string.append(searchLocation);
                request_string.append("&output=json");

                URL url = new URL(request_string.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    while ((bufferString = br.readLine()) != null) {
                        API_return.append(bufferString);
                    }
                    conn.disconnect();
                    return API_return.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /***
         * mapLocationstoList takes in a JSON-String obtained from the PositionStack API
         * and returns a List of Locations;
         * @param jsonString The JSON-String received from the PositionStack API, i.e. the locations found
         *                   by the API in the full format;
         * @return A list of locations in the format of the Location class
         */
        public List<Location> mapLocationstoList(String jsonString) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String con_string = jsonString.substring(jsonString.indexOf("["), jsonString.lastIndexOf("]") + 1);
                List<Location> loc_List = objectMapper.readValue(con_string, new TypeReference<List<Location>>() {
                });
                return loc_List;
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

