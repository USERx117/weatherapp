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
import java.io.ObjectInputFilter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.JsonMappingException;


public class frm_configurateController {

    public void initialize()
    {
        Configuration new_config = Configuration.get_configuration();

        tb_locationAPIKey.setText(new_config.getPositionstack_api_key());
        tb_weatherAPIKey.setText(new_config.getOpenweather_api_key());
        tb_uprate_mm.setText(String.valueOf(new_config.getUpdate_interval()));

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

        try {
            ObservableList<String> ob_list = FXCollections.observableArrayList();
            LocationService loc_serv = new LocationService(tf_serachLocation, lv_Locations);
            loc_serv.setSerachString(tf_serachLocation.getText().toString());
            loc_serv.start();

        } catch (Exception e) {

        }

    }

    @FXML
    void bt_selectLocationButtonClicked(MouseEvent event) {

        String location = lv_Locations.getSelectionModel().getSelectedItem();
        Double new_lat = Double.parseDouble(location.substring(location.indexOf("Lat:") + 4, location.indexOf("|", location.indexOf("Lat:"))));
        Double new_lng = Double.parseDouble(location.substring(location.indexOf("Lng:") + 4, location.length()));

        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_latitude", String.valueOf(new_lat));
        update_conf.update_config("openweather_longitude", String.valueOf(new_lng));
    }

    @FXML
    void bt_update_uprateButtonClicked(MouseEvent event) {
        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_updateInterval", tb_uprate_mm.getText());
    }

    @FXML
    void bt_updatelocationAPIKey_ButtonClicked(MouseEvent event) {
        Configuration update_conf = new Configuration();
        update_conf.update_config("positionstack_api_key", tb_locationAPIKey.getText());
    }

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

    @FXML
    void rb_imperialunits_Action(ActionEvent event) {

        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_units", "imperial");
    }

    @FXML
    void rb_metricunits_Action(ActionEvent event) {
        Configuration update_conf = new Configuration();
        update_conf.update_config("openweather_units", "metric");
    }

    @FXML
    void tb_uprate_mm_Action(ActionEvent event) {

    }

    private class LocationService extends Service<ObservableList<String>> {
        private String serachString;

        public String getSerachString() {
            return serachString;
        }

        public void setSerachString(String serachString) {
            this.serachString = serachString;
        }

        private LocationService(TextField tb_searchString, ListView<String> lv_Locations)
        {
            setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    setSerachString(tb_searchString.getText());
                }
            });
        }

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


    public static class APILocationService {

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
            ;

            return locob_list;
        }

        public String getLocationsJsonString(String searchLocation) throws IOException {
            StringBuilder API_return = new StringBuilder();
            String bufferString = "";

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

