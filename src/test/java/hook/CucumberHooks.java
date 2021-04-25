package hook;

import com.google.inject.Inject;
import io.cucumber.java.*;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import util.common.DriverManagement;
import util.common.FilePropertiesHelper;
import util.common.ShareState;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CucumberHooks {
    private static final Logger log = LogManager.getLogger(CucumberHooks.class);
    private String timeout;

    {
        try {
            timeout = System.getProperty("AUTOMATION.DEFAULT_TIMEOUT",
                    FilePropertiesHelper.getProperty("properties/ProjectInformation.properties",
                            "AUTOMATION.DEFAULT_TIMEOUT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ShareState shareState;

    @Inject
    public CucumberHooks(ShareState shareState) throws IOException {
        this.shareState = shareState;
    }

    @Before()
    public void beforeEveryScenario(Scenario scenario) throws IOException {
        if(scenario.getSourceTagNames().contains("@UI")){
            beforeUIScenario();
        }
        if(scenario.getSourceTagNames().contains("@API")){
            beforeAPIScenario();
        }
    }

    public void beforeUIScenario() throws IOException {
        String url = System.getProperty("SUT.URL",
                FilePropertiesHelper.getProperty("properties/ProjectInformation.properties", "SUT.URL"));

        // Open browser
        DriverManagement.getDriverManagerInstance().initWebDriver();
        // Navigate to Login page
        DriverManagement.getDriverManagerInstance().getDriver().get(url);
        // Maximize the window
        DriverManagement.getDriverManagerInstance().getDriver().manage().window().maximize();
        // Set default time out
        DriverManagement.getDriverManagerInstance().getDriver().manage().
                timeouts().implicitlyWait(Integer.parseInt(timeout), TimeUnit.SECONDS);
    }

    public void beforeAPIScenario() throws IOException {
        String apiScheme = System.getProperty("SUT.API_SCHEME",
                FilePropertiesHelper.getProperty("properties/ProjectInformation.properties", "SUT.API_SCHEME"));
        String apiHost = System.getProperty("SUT.API_HOST",
                FilePropertiesHelper.getProperty("properties/ProjectInformation.properties", "SUT.API_HOST"));
        RestAssured.baseURI = apiScheme + "://" + apiHost;
    }

    @After("@UI")
    public void afterUIScenario(Scenario scenario) {
        if(scenario.isFailed()){
            try{
                log.info(scenario.getName() + " is Failed");
                WebDriver driver = DriverManagement.getDriverManagerInstance().getDriver();
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "screenshot");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        // Close browser
        DriverManagement.getDriverManagerInstance().terminateWebDriver();
    }
}
