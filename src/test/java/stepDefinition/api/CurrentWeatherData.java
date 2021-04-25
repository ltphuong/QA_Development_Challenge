package stepDefinition.api;

import com.google.inject.Inject;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import page.HomePage;
import page.SearchWeatherResultPage;
import util.common.ApiHelper;
import util.common.ShareState;

public class CurrentWeatherData {
    @Inject
    HomePage homePage;

    @Inject
    SearchWeatherResultPage searchWeatherResultPage;

    private ShareState shareState;

    @Inject
    public CurrentWeatherData(ShareState shareState) {
        this.shareState = shareState;
    }

    @Given("I am on Home page")
    public void iAmOnHomePage() { }

    @When("I search {string} city from header")
    public void iSearchCityFromHeader(String city) {
        this.shareState.customAttributes.put("city", city);
        homePage.searchWeatherInYourCity(city);
    }

    @When("I click on the city hyperlink")
    public void iClickOnTheCityHyperlink() throws InterruptedException {
        searchWeatherResultPage.clickOnCityLink();
    }

    @And("I send GET request to search {string} city with {string} units")
    public void iSendSearchCityApiRequest(String city, String units){
        Response response = ApiHelper.searchWeatherInYourCity(city, units);
        this.shareState.customAttributes.put("mapWeatherResponse", response);
    }

    @And("I can see the geographical coordinate matching with API response")
    public void iCanSeeTheGeoCoordsMatchingWithApiResponse(){
        searchWeatherResultPage.verifyTheGeoCoords();
    }

    @And("I can see the weather description matching with API response")
    public void iCanSeeTheWeatherDescriptionMatchingWithApiResponse(){
        searchWeatherResultPage.verifyTheWeatherDescription();
    }

    @And("I can see the weather information matching with API response with {string} pattern")
    public void iCanSeeTheWeatherInformationMatchingWithApiResponse(String pattern) {
        searchWeatherResultPage.verifyWeatherInfo(pattern);
    }

    @Then("I can see the {string} city matching with API response")
    public void iCanSeeTheCityMatchingWithApiResponse(String city){
        searchWeatherResultPage.verifyTheCityDisplayed(city);
    }
}
