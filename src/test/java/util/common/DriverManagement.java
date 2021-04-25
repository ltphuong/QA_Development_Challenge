package util.common;

import com.google.inject.Provides;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.util.Strings;

import java.io.IOException;
import java.net.URL;

public class DriverManagement {
    private static DriverManagement instance= null;
    private static ThreadLocal<WebDriver> webDriver = new InheritableThreadLocal<>();

    /**
     * Initialize constructor
     */
    private DriverManagement(){
    }

    /**
     * Get driver instance
     * @return instance
     */
    public static DriverManagement getDriverManagerInstance(){
        if(instance == null){
            instance = new DriverManagement();
        }
        return instance;
    }

    /**
     * Initialize list of browsers
     */
    private enum BROWSER {
        CHROME, FIREFOX
    }

    /**
     * Initialize WebDriver
     * @throws IOException
     */
    public void initWebDriver() throws IOException {
        String executionMode = System.getProperty("AUTOMATION.EXECUTION_MODE",
                FilePropertiesHelper.getProperty("properties/ProjectInformation.properties",
                        "AUTOMATION.EXECUTION_MODE"));
        String browserType =  System.getProperty("AUTOMATION.BROWSER_TYPE",
                FilePropertiesHelper.getProperty("properties/ProjectInformation.properties",
                        "AUTOMATION.BROWSER_TYPE"));

        BROWSER browser = BROWSER.valueOf(browserType.toUpperCase());
        if(executionMode.toLowerCase().equals("local")){
            if (browser == BROWSER.FIREFOX) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                FilePropertiesHelper.setProperties("properties/Firefox.properties");
                System.getProperties().keySet().forEach(
                        sys -> {
                            if (sys.toString().contains("Firefox.options")) {
                                firefoxOptions.addArguments(System.getProperty(sys.toString()));
                            }
                        }
                );
                WebDriverManager.firefoxdriver().setup();
                setDriver(new FirefoxDriver(firefoxOptions));
            }
            else if (browser == BROWSER.CHROME) {
                ChromeOptions chromeOptions = new ChromeOptions();
                FilePropertiesHelper.setProperties("properties/Chrome.properties");
                System.getProperties().keySet().forEach(
                        sys -> {
                            if (sys.toString().contains("chrome.options")) {
                                chromeOptions.addArguments(System.getProperty(sys.toString()));
                            }
                        }
                );
                WebDriverManager.chromedriver().setup();
                setDriver(new ChromeDriver(chromeOptions));
            }
            else {
                throw new RuntimeException("Please enter correct browser name");
            }
        }
        else {
            String remoteHubUrl = System.getProperty("AUTOMATION.REMOTE_HUB_URL",
                    FilePropertiesHelper.getProperty("properties/ProjectInformation.properties",
                            "AUTOMATION.REMOTE_HUB_URL"));
            DesiredCapabilities capabilities;
            if(!Strings.isNullOrEmpty(remoteHubUrl)){
                if (browser == BROWSER.FIREFOX) {
                    capabilities = DesiredCapabilities.firefox();
                    capabilities.setJavascriptEnabled(true);
                    setDriver(new RemoteWebDriver(new URL(remoteHubUrl), capabilities));
                }
                else if (browser == BROWSER.CHROME) {
                    capabilities = DesiredCapabilities.chrome();
                    capabilities.setJavascriptEnabled(true);
                    setDriver(new RemoteWebDriver(new URL(remoteHubUrl), capabilities));
                }
                else {
                    throw new RuntimeException("Please enter correct browser name");
                }
            }
        }
    }

    /**
     * Remove driver after execute test case
     */
    public void terminateWebDriver() {
        try {
            // Get OS name
            String osName = System.getProperty("os.name");

            String cmd = "";

            // close browser
            if(getDriver() != null) {
                getDriver().quit();
            }

            // close service (browser driver)
            if(getDriver().toString().contains("chrome")) {
                if(osName.toLowerCase().contains("mac")) {
                    cmd = "pkill chromedriver";
                }
                else if(osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq chromedriver*\"";
                }
            }
            else if(getDriver().toString().contains("internetexplorer")) {
                if(osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq IEDriverSever*\"";
                }
            }
            else if(getDriver().toString().contains("firefox")) {
                if(osName.toLowerCase().contains("mac")) {
                    cmd = "pkill geckodriver";
                }
                else if(osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq geckodriver*\"";
                }
            }
            else if(getDriver().toString().contains("edge")) {
                if(osName.toLowerCase().contains("mac")) {
                    cmd = "pkill edgedriver";
                }
                else if(osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq edgedriver*\"";
                }
            }

            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        }
        catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }

        webDriver.remove();
    }

    /**
     * Set WebDriver
     * @param driver
     */
    public void setDriver(WebDriver driver) {
        webDriver.set(driver);
    }

    /**
     * Get WebDriver
     * @return webDriver
     */
    @Provides
    public WebDriver getDriver(){
        return webDriver.get();
    }
}
