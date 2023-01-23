package dev.fhtw.oode.weatherapp.client;

import dev.fhtw.oode.weatherapp.model.WeatherEntity;
import dev.fhtw.oode.weatherapp.model.WeatherForecastEntityAll;
import dev.fhtw.oode.weatherapp.model.WeatherReportEntity;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class OpenWeatherClient {

    private String threadName;

    private String apiKey;

    private String hostname;

    private String latitude;

    private String longitude;

    private String units;

    private WeatherReportEntity weatherReport;

    public OpenWeatherClient(String threadName) {
        this.threadName = threadName;
    }

    public OpenWeatherClient(String apiKey, String hostname, String units, String longitude, String latitude) {
        this.apiKey = apiKey;
        this.hostname = hostname;
        this.units = units;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public WeatherReportEntity connect() {
        return connect(this.latitude, this.longitude);
    }
    public WeatherReportEntity connect(String lat, String lon){

        StringBuilder headerCurrent=new StringBuilder();
        headerCurrent.append("GET /data/2.5/weather?lat=");
        headerCurrent.append(lat);
        headerCurrent.append("&lon=");
        headerCurrent.append(lon);
        headerCurrent.append("&appid=");
        headerCurrent.append(apiKey);
        headerCurrent.append("&units=");
        headerCurrent.append(units);
        headerCurrent.append(" HTTP/1.1\r\n");
        headerCurrent.append("Connection: close\r\n");
        headerCurrent.append("Host: ");
        headerCurrent.append(this.hostname+"\r\n");
        headerCurrent.append("\r\n");
        System.out.println("headerCurrent from connect: ");
        System.out.println(headerCurrent.toString());

        StringBuilder headerForecast=new StringBuilder();
        headerForecast.append("GET /data/2.5/forecast?lat=");
        headerForecast.append(lat);
        headerForecast.append("&lon=");
        headerForecast.append(lon);
        headerForecast.append("&appid=");
        headerForecast.append(apiKey);
        headerForecast.append("&units=");
        headerForecast.append(units);
        headerForecast.append(" HTTP/1.1\r\n");
        headerForecast.append("Connection: close\r\n");
        headerForecast.append("Host: ");
        headerForecast.append(this.hostname+"\r\n");
        headerForecast.append("\r\n");
        System.out.println("headerForecast from connect: ");
        System.out.println(headerForecast.toString());

        try{
            this.weatherReport = new WeatherReportEntity();
            this.weatherReport.setCurrent(fetchCurrentWeather(headerCurrent.toString()));
            this.weatherReport.setForecast(fetchForecastWeather(headerForecast.toString()));
            List<String> dayOne = this.weatherReport.getForecast().get(0).getMinAndMaxTemps();
            List<String> dayTwo = this.weatherReport.getForecast().get(1).getMinAndMaxTemps();
            List<String> dayThree = this.weatherReport.getForecast().get(2).getMinAndMaxTemps();
            List<String> dayFour = this.weatherReport.getForecast().get(3).getMinAndMaxTemps();
            List<String> dayFive = this.weatherReport.getForecast().get(4).getMinAndMaxTemps();

            List<String> allMinAndMaxValue = new ArrayList<>();
            Stream.of(dayOne, dayTwo, dayThree, dayFour, dayFive).forEach(allMinAndMaxValue::addAll);

            this.weatherReport.setForecastMinAndMaxTemperatures(allMinAndMaxValue);


            //this.weatherReport.setForecastMinAndMaxTemperatures(List.of("12", "13"));
            //this.weatherReport.setForecastMinAndMaxTemperatures(fetchForecastWeather(headerForecast.toString()).get(0).getMinAndMaxTemps());
            //this.weatherReport.setForecastMinAndMaxTemperatures();
            return this.weatherReport;
        }catch(IOException e){
            e.printStackTrace();
            return new WeatherReportEntity();
        }
    }



    private WeatherEntity fetchCurrentWeather(String header) throws IOException {

        try (final Socket socket = new Socket(hostname,80)) {
            final String request = header;

            final OutputStream outputStream = socket.getOutputStream();
            outputStream.write(request.getBytes(StandardCharsets.UTF_8));

            final InputStream inputStream = socket.getInputStream();
            final String response = readAsString(inputStream);
            //System.out.println(response);
            outputStream.close();
            inputStream.close();
            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

            WeatherEntity weatherEntity = new WeatherEntity(
                    jsonObject.getJSONObject("main").get("temp").toString(),
                    jsonObject.get("name").toString(),
                    jsonObject.getJSONObject("main").get("pressure").toString(),
                    jsonObject.getJSONObject("main").get("temp_min").toString(),
                    jsonObject.getJSONObject("main").get("temp_max").toString(),
                    jsonObject.get("dt").toString()
                    );
            System.out.println(weatherEntity.toString());
            return weatherEntity;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private ArrayList<WeatherEntity> fetchForecastWeather(String header) throws IOException {

        try (final Socket socket = new Socket(hostname,80)) {
            final String request = header;

            final OutputStream outputStream = socket.getOutputStream();
            outputStream.write(request.getBytes(StandardCharsets.UTF_8));

            final InputStream inputStream = socket.getInputStream();
            final String response = readAsString(inputStream);
            //System.out.println(response);
            outputStream.close();
            inputStream.close();
            JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
            ArrayList<WeatherEntity> result = new ArrayList<WeatherEntity>();

            Iterator<String>keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    for(int i=0; i<jsonObject.getJSONArray("list").length(); i++) {
                        //System.out.println(jsonObject.getJSONArray("list").getJSONObject(i).get("dt"));
                        //checkDate(jsonObject.getJSONArray("list").getJSONObject(i).get("dt").toString());
                        if(checkDate(jsonObject.getJSONArray("list").getJSONObject(i).get("dt").toString())) {
                            WeatherEntity weatherEntity = new WeatherEntity(
                                    jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp").toString(),
                                    "",
                                    jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("pressure").toString(),
                                    jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_min").toString(),
                                    jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_max").toString(),
                                    jsonObject.getJSONArray("list").getJSONObject(i).get("dt").toString());
                            result.add(weatherEntity);
                        }
                    }
                }
            }
            List<WeatherForecastEntityAll> minMaxEntities = getWeatherEntityByDay(response);
            List<WeatherForecastEntityAll> dayOneForecastEntity = findInGroupsOfEight(minMaxEntities);
            List<WeatherForecastEntityAll> dayTwoForecastEntity = findInGroupsOfEight(minMaxEntities);
            List<WeatherForecastEntityAll> dayThreeForecastEntity = findInGroupsOfEight(minMaxEntities);
            List<WeatherForecastEntityAll> dayFourForecastEntity = findInGroupsOfEight(minMaxEntities);
            List<WeatherForecastEntityAll> dayFiveForecastEntity = findInGroupsOfEight(minMaxEntities);

            List<String> dayOneMinMaxTemperatures = getMinAndMaxTemp(dayOneForecastEntity);
            List<String> dayTwoMinMaxTemperatures = getMinAndMaxTemp(dayTwoForecastEntity);
            List<String> dayThreeMinMaxTemperatures = getMinAndMaxTemp(dayThreeForecastEntity);
            List<String> dayFourMinMaxTemperatures = getMinAndMaxTemp(dayFourForecastEntity);
            List<String> dayFiveMinMaxTemperatures = getMinAndMaxTemp(dayFiveForecastEntity);

            //List<String> allMinAndMaxValue = new ArrayList<>();
            //Stream.of(dayOneMinMaxTemperatures, dayTwoMinMaxTemperatures, dayThreeMinMaxTemperatures, dayFourMinMaxTemperatures, dayFiveMinMaxTemperatures).forEach(allMinAndMaxValue::addAll);

            //result.get(0).setMinAndMaxTemps(allMinAndMaxValue);

//            WeatherReportEntity reportEntity = new WeatherReportEntity();
//            reportEntity.setForecastMinAndMaxTemperatures(dayOneMinMaxTemperatures);

            result.get(0).setMinAndMaxTemps(dayOneMinMaxTemperatures);
            result.get(1).setMinAndMaxTemps(dayTwoMinMaxTemperatures);
            result.get(2).setMinAndMaxTemps(dayThreeMinMaxTemperatures);
            result.get(3).setMinAndMaxTemps(dayFourMinMaxTemperatures);
            result.get(4).setMinAndMaxTemps(dayFiveMinMaxTemperatures);

            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }


    private List<WeatherForecastEntityAll> getWeatherEntityByDay (String response) {
        List<WeatherForecastEntityAll> weatherForecastEntities = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            weatherForecastEntities.add(new WeatherForecastEntityAll(
                    jsonObject.getJSONArray("list").getJSONObject(i).get("dt").toString(),
                    jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_min").toString(),
                    jsonObject.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_max").toString()
            ));
        }

        return weatherForecastEntities;
    }

    private List<WeatherForecastEntityAll> findInGroupsOfEight(List<WeatherForecastEntityAll> entities) {
        List<WeatherForecastEntityAll> result = entities.stream().limit(8).collect(Collectors.toList());
        result.forEach(entities::remove);
        return result;
    }


    private List<String> getMinAndMaxTemp(List<WeatherForecastEntityAll> dailyEntities) {
        List<String> minAndMaxTemperatures = new ArrayList<>();
        WeatherForecastEntityAll minTempEntity = dailyEntities.stream().min(Comparator.comparing(WeatherForecastEntityAll::getTemp_min)).orElseThrow(NoSuchElementException::new);
        WeatherForecastEntityAll maxTempEntity = dailyEntities.stream().max(Comparator.comparing(WeatherForecastEntityAll::getTemp_max)).orElseThrow(NoSuchElementException::new);


        minAndMaxTemperatures.add(minTempEntity.getTemp_min());
        minAndMaxTemperatures.add(maxTempEntity.getTemp_max());
        return minAndMaxTemperatures;
    }

    private static String readAsString(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toString(StandardCharsets.UTF_8.name());
    }

    private boolean checkDate(String dt) {
        String toCompare = dt + "000";
        Calendar calTodayMidnight = new GregorianCalendar();
        calTodayMidnight.setTime(new Date());
        calTodayMidnight.set(Calendar.HOUR_OF_DAY,1);
        calTodayMidnight.set(Calendar.MINUTE,0);
        calTodayMidnight.set(Calendar.SECOND,0);
        calTodayMidnight.set(Calendar.MILLISECOND,0);
        Date todayMidnight = calTodayMidnight.getTime();

        for(int i=1;i<=5;i++){
            if(toCompare.equals(Long.toString(DateUtils.addDays(todayMidnight,i).getTime()))) {
                return true;
            }
        }
    return false;
    }

}
