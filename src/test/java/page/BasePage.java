package page;

import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.common.DriverManagement;
import util.common.FilePropertiesHelper;
import util.constant.GlobalConstant;

import java.util.List;

@ScenarioScoped
public class BasePage {
    private String timeout;
    {
        try {
            timeout = System.getProperty("AUTOMATION.DEFAULT_MEDIUM_TIMEOUT",
                        FilePropertiesHelper.getProperty("properties/ProjectInformation.properties",
                                "AUTOMATION.DEFAULT_TIMEOUT"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WebDriver driver = DriverManagement.getDriverManagerInstance().getDriver();
    public WebDriverWait explicitWait = new WebDriverWait(driver,
            Integer.parseInt(timeout));
    public BasePage() {
        PageFactory.initElements(driver, this);
    }

    public By getByXpath(String locator) {
        return By.xpath(locator);
    }

    public WebElement getElementFromParent(WebElement parent, String locator) {
        return parent.findElement(getByXpath(locator));
    }

    public String getElementText(WebElement parent, String locator) {
        return getElementFromParent(parent, locator).getText();
    }

    /*
     * Wait for page load successfully
     */
    public void waitForPageLoad() {
        this.pause(GlobalConstant.SHORT_TIME_WAIT);
        ExpectedCondition<Boolean> pageLoadCompleted = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver _driver) {
                return ((JavascriptExecutor) _driver).executeScript("return document.readyState")
                        .toString()
                        .equalsIgnoreCase("complete");
            }
        };
        explicitWait.until(pageLoadCompleted);
    }

    /*
     * Wait for some seconds
     */
    public void pause(long time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Wait for all elements visible
     */
    public void waitForAllElementsVisible(List<WebElement> elements){
        explicitWait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }
}
