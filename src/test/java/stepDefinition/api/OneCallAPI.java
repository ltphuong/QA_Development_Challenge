package stepDefinition.api;

import com.google.inject.Inject;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import page.HomePage;
import util.common.ShareState;

import java.util.List;
import java.util.Map;

public class OneCallAPI {

    @Inject
    HomePage homePage;

    private ShareState shareState;

    @Inject
    public OneCallAPI(ShareState shareState) {
        this.shareState = shareState;
    }

    @And("I send GET request to 8-Day Weather Forecast with {string} city and {string} units")
    public void iSendOneCallApiRequest(String city, String units){
        List<Map<String, String>> eightDayForeCastResponse = homePage.sendOneCallApiRequest(city, units);
        this.shareState.customAttributes.put("eightDayForeCastResponse", eightDayForeCastResponse);
    }

    @Then("I can see 8-Day Weather Forecast matching with API response")
    public void iCanSeeCorrect8DayWeatherForecast() {
        homePage.verify8DayWeatherForecast();
    }
}
