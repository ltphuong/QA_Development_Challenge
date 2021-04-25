package util.common;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

public class ApiHelper {
    public static final String SEARCH_CITY_URL = "/find";
    public static final String CALL_CURRENT_WEATHER_DATA = "/weather";
    public static final String ONE_CALL_URL = "/onecall";
    public static String API_KEY;

    static {
        try {
            API_KEY = System.getProperty("SUT.API_KEY",
                    FilePropertiesHelper.getProperty("properties/ProjectInformation.properties", "SUT.API_KEY"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Response searchWeatherInYourCity(String city, String unit){
        RequestSpecification httpRequest = RestAssured.given()
                .queryParam("q", city)
                .queryParam("units", unit)
                .queryParam("appid", API_KEY);

        return (Response) httpRequest.get(SEARCH_CITY_URL);
    }

    public static Response getCurrentAndForecastWeatherData(String lon, String lat, String unit){
        RequestSpecification httpRequest = RestAssured.given()
                .queryParam("lon",lon)
                .queryParam("lat",lat)
                .queryParam("units", unit)
                .queryParam("appid",API_KEY);

        return (Response) httpRequest.get(ONE_CALL_URL);
    }
}
