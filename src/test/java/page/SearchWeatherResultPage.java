package page;

import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.response.Response;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;
import util.common.ShareState;
import util.common.Utilities;
import util.constant.SearchCityConstant;

import java.util.HashMap;
import java.util.List;

@ScenarioScoped
public class SearchWeatherResultPage extends BasePage {

    @FindBy(how = How.XPATH, using = "//table//a[contains(@href,'city')]")
    public WebElement lnk_City;

    @FindBy(how = How.XPATH, using = "//table//i")
    public WebElement lbl_WeatherDescription;

    @FindBy(how = How.XPATH, using = "//table//span[contains(@class,'badge-info')]/parent::p")
    public WebElement lbl_WeatherInfo;

    @FindBy(how = How.XPATH, using = "//table//p[2]")
    public WebElement lbl_GeoCoords;

    @Inject
    HomePage homePage;

    public ShareState shareState;

    @Inject
    public SearchWeatherResultPage(ShareState shareState) {
        this.shareState = shareState;
    }

    /*
     * Click on city link
     */
    public void clickOnCityLink() {
        lnk_City.click();
        waitForAllElementsVisible(homePage.lst_DailyForeCast);
    }

    /*
     * Verify your city name displayed correctly
     */
    public void verifyTheCityDisplayed(String city){
        // Get response data
        Response response = (Response) this.shareState.customAttributes.get("mapWeatherResponse");

        // Verify city name displayed matching with API response data
        Assert.assertEquals(lnk_City.getText(), getCityFromResponseData(response));
    }

    /*
     * Verify the weather description displayed correctly
     * eg: Moderate breeze, broken clouds, clear sky
     */
    public void verifyTheWeatherDescription(){
        // Get response data
        Response response = (Response) this.shareState.customAttributes.get("mapWeatherResponse");

        // Verify weather description
        Assert.assertEquals(lbl_WeatherDescription.getText(), getWeatherDescriptionFromResponseData(response));
    }

    /*
     * Verify the weather information displayed correctly
     * eg: 10.8°С temperature from 9.4 to 12 °С, wind 1.54 m/s. clouds 1 %, 1017 hpa
     */
    public void verifyWeatherInfo(String pattern) {
       // Get response data
        Response response = (Response) this.shareState.customAttributes.get("mapWeatherResponse");

       // Verify weather information
        HashMap<String,String> mapWeatherInfo = getWeatherInfoFromResponseData(response);
        String expectedWeatherInfo = String.format(pattern,
               mapWeatherInfo.get(SearchCityConstant.TEMPERATURE),
               mapWeatherInfo.get(SearchCityConstant.TEMPERATURE_MIN),
               mapWeatherInfo.get(SearchCityConstant.TEMPERATURE_MAX),
               mapWeatherInfo.get(SearchCityConstant.WIND_SPEED),
               mapWeatherInfo.get(SearchCityConstant.CLOUD),
               mapWeatherInfo.get(SearchCityConstant.PRESSURE));
        System.out.println(expectedWeatherInfo);
        Assert.assertEquals(lbl_WeatherInfo.getText(), expectedWeatherInfo);
    }

    /*
     * Verify the Geo coords displayed correctly
     * eg: Geo coords [40.7143, -74.006]
     */
    public void verifyTheGeoCoords(){
        // Get response data
        Response response = (Response) this.shareState.customAttributes.get("mapWeatherResponse");

        // Verify Geo Coords
        HashMap<String,String> mapGeoCoords = getGeoCoordsFromResponseData(response);
        String expectedGeoCoords = String.format("Geo coords [%s, %s]",
                mapGeoCoords.get(SearchCityConstant.GEO_COORDS_LAT),
                mapGeoCoords.get(SearchCityConstant.GEO_COORDS_LON));

        Assert.assertEquals(lbl_GeoCoords.getText(), expectedGeoCoords);
    }

    /*
     * Get your city name from response data
     */
    private String getCityFromResponseData(Response response){
        HashMap<String,Object> mapWeatherResponse= (HashMap) response.jsonPath().getList("list").get(0);
        String city_name = mapWeatherResponse.get("name").toString();
        String country = ((HashMap) mapWeatherResponse.get("sys")).get("country").toString();
        return  city_name + ", " + country;
    }

    /*
     * Get weather information from response data
     */
    private HashMap<String,String> getWeatherInfoFromResponseData(Response response){
        HashMap<String,String> result = new HashMap<>();

        //get temperature from API Response
        HashMap<String,Object> mapWeatherResponse= (HashMap) response.jsonPath().getList("list").get(0);
        String temp = Utilities.scaleNumber(((HashMap) mapWeatherResponse.get("main")).get("temp").toString(), 1).toString();
        result.put(SearchCityConstant.TEMPERATURE, temp);

        //get min temperature from API Response
        String minTemp = Utilities.scaleNumber(((HashMap) mapWeatherResponse.get("main")).get("temp_min").toString(), 1).toString();
        result.put(SearchCityConstant.TEMPERATURE_MIN, minTemp);

        //get max temperature from API Response
        String maxTemp = Utilities.scaleNumber(((HashMap) mapWeatherResponse.get("main")).get("temp_max").toString(), 1).toString();
        result.put(SearchCityConstant.TEMPERATURE_MAX, maxTemp);

        //Get wind speed from API Response
        String wind_speed = ((HashMap) mapWeatherResponse.get("wind")).get("speed").toString();
        result.put(SearchCityConstant.WIND_SPEED, wind_speed);

        //Get cloud % from API Response
        String cloud = ((HashMap) mapWeatherResponse.get("clouds")).get("all").toString();
        result.put(SearchCityConstant.CLOUD, cloud);

        //get pressure from API Response
        String pressure = ((HashMap) mapWeatherResponse.get("main")).get("pressure").toString();
        result.put(SearchCityConstant.PRESSURE, pressure);

        return result;
    }

    /*
     * Get Geo coords from response data
     */
    public HashMap<String,String> getGeoCoordsFromResponseData(Response response){
        HashMap<String,String> result = new HashMap<>();

        //get lat from API Response
        HashMap<String,Object> mapWeatherResponse= (HashMap) response.jsonPath().getList("list").get(0);
        String lat = ((HashMap) mapWeatherResponse.get("coord")).get("lat").toString();
        result.put(SearchCityConstant.GEO_COORDS_LAT, lat);

        //get lon from API Response
        String lon = ((HashMap) mapWeatherResponse.get("coord")).get("lon").toString();
        result.put(SearchCityConstant.GEO_COORDS_LON, lon);

        return result;
    }

    /*
     * Get weather description from response data
     */
    private String getWeatherDescriptionFromResponseData(Response response){
        HashMap<String,Object> mapWeatherResponse= (HashMap) response.jsonPath().getList("list").get(0);
        String weatherDescription =  ((HashMap) ((List) mapWeatherResponse.get("weather")).get(0)).get("description").toString();
        return weatherDescription;
    }
}
