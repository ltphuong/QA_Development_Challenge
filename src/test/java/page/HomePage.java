package page;

import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.response.Response;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;
import util.common.ApiHelper;
import util.common.DateTimeHelper;
import util.common.ShareState;
import util.common.Utilities;
import util.constant.GlobalConstant;
import util.constant.HomePageConstant;
import util.constant.SearchCityConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScenarioScoped
public class HomePage extends BasePage {

    @FindBy(how = How.XPATH, using = "//div[@id='desktop-menu']//input[@placeholder='Weather in your city']")
    public WebElement txt_SearchCity;

    @FindBy(how = How.XPATH, using = "//div[h3[text()='8-day forecast']]//li")
    public List<WebElement> lst_DailyForeCast;

    @Inject
    SearchWeatherResultPage searchWeatherResultPage;

    private ShareState shareState;

    @Inject
    public HomePage(ShareState shareState) {
        this.shareState = shareState;
    }

    /*
     * Search weather in your city
     */
    public void searchWeatherInYourCity(String city) {
        waitForPageLoad();
        // Input city to search text box
        txt_SearchCity.sendKeys(city);

        // Send key Enter to search
        txt_SearchCity.sendKeys(Keys.ENTER);
    }

    /*
     * Send One call API to get 8-day forecast
     */
    public List<Map<String, String>> sendOneCallApiRequest(String city, String unit){

        // Send request to get Geographical coordinate
        Response searchWeatherResponse = ApiHelper.searchWeatherInYourCity(city, unit);
        HashMap<String,Object> geoCoordsMap = (HashMap) searchWeatherResultPage.getGeoCoordsFromResponseData(searchWeatherResponse);

        // Get request to get 8 day forecast
        List<Map<String, String>> eightDayForeCastResponse = getEightDayForeCastFromAPI(
                geoCoordsMap.get(SearchCityConstant.GEO_COORDS_LON).toString(),
                geoCoordsMap.get(SearchCityConstant.GEO_COORDS_LAT).toString(),
                unit);

        return eightDayForeCastResponse;
    }

    /*
     * Verify 8 day weather forecast displayed matching with Api response
     */
    public void verify8DayWeatherForecast() {
        // Get weather info on UI
        List<HashMap<String, String>> eightDayForeCastOnUI = getEightDayForeCastOnUI();

        // Get 8-day forecast from response
        List<Map<String, String>> eightDayForeCastResponse = (List) this.shareState.customAttributes.get("eightDayForeCastResponse");

        Assert.assertEquals(eightDayForeCastOnUI, eightDayForeCastResponse);
    }

    /*
     * Get weather info on UI
     */
    public List<HashMap<String, String>> getEightDayForeCastOnUI() {
        waitForPageLoad();
        List<HashMap<String, String>> result = new ArrayList<>();
        for (WebElement ele : lst_DailyForeCast) {
            HashMap<String, String> temp = new HashMap<>();
            String date = getElementText(ele, "./span");
            String temperature = getElementText(ele, "./div/div/span");
            String description = getElementText(ele, ".//span[@class='sub']");

            temp.put(HomePageConstant.DATE, date);
            temp.put(HomePageConstant.TEMPERATURE, temperature.substring(0 , temperature.length()-2));
            temp.put(HomePageConstant.DESCRIPTION, description);
            result.add(temp);
        }

        return result;
    }

    /*
     * Get 8 day forecast from Api response
     */
    public List<Map<String, String>> getEightDayForeCastFromAPI(String lon, String lat, String unit){
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        // Get all weather Info
        Response response =  ApiHelper.getCurrentAndForecastWeatherData(lon, lat, unit);

        // Get time zone
        String timeZone = response.jsonPath().getString("timezone");

        //Get 8-day forecast
        List<Map<String,Object>> eightDayForeCast = response.jsonPath().getList("daily");
        for (Map item: eightDayForeCast){
            Map<String,String> temp = new HashMap();

            //Get unixTime
            Long unixTime = Long.valueOf(item.get("dt").toString());
            String formattedDate = DateTimeHelper.convertDateFromUnixTime(unixTime, timeZone, GlobalConstant.DATE_TIME_FORMAT);
            temp.put(HomePageConstant.DATE, formattedDate);

            //get min temperature
            String minTemp = ((HashMap) item.get("temp")).get("min").toString();
            minTemp = Utilities.scaleNumber(minTemp, 0).toString();

            //get max temperature
            String maxTemp = ((HashMap) item.get("temp")).get("max").toString();
            maxTemp = Utilities.scaleNumber(maxTemp, 0).toString();;
            temp.put(HomePageConstant.TEMPERATURE, maxTemp + " / " + minTemp);

            //Get weather description
            String weatherDescription = ((HashMap) ((List) item.get("weather")).get(0)).get("description").toString();
            temp.put(HomePageConstant.DESCRIPTION, weatherDescription);

            result.add(temp);
        }

        return result;
    }
}
