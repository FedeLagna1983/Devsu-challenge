package devsu.ui.pages.checkout;

import devsu.core.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object for the SauceDemo checkout complete page (order confirmation). */
public class CheckoutCompletePage {

    private WebDriverWait wait;

    private By completeContainer = By.cssSelector("[data-test='checkout-complete-container']");
    private By confirmationHeader = By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getIntProperty("timeout")));
    }

    public boolean isCheckoutCompletePageDisplayed() {
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeContainer)).isDisplayed();
    }

    public String getConfirmationHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationHeader)).getText();
    }
}
