package devsu.ui.pages.checkout;

import devsu.core.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object for the SauceDemo checkout step-one page (personal information form). */
public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By firstNameInput = By.cssSelector("[data-test='firstName']");
    private By lastNameInput = By.cssSelector("[data-test='lastName']");
    private By zipCodeInput = By.cssSelector("[data-test='postalCode']");
    private By continueButton = By.cssSelector("[data-test='continue']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getIntProperty("timeout")));
    }

    public boolean isCheckoutPageDisplayed() {
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).isDisplayed();
    }

    public void enterFirstName(String firstName) {
        setReactInputValue(firstNameInput, firstName);
    }

    public void enterLastName(String lastName) {
        setReactInputValue(lastNameInput, lastName);
    }

    public void enterZipCode(String zipCode) {
        setReactInputValue(zipCodeInput, zipCode);
    }

    public void clickContinue() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    // Uses the native HTMLInputElement setter to trigger React's onChange synthetic event
    private void setReactInputValue(By locator, String value) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(locator));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
            input, value
        );
    }
}
