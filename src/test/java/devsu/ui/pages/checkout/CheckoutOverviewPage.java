package devsu.ui.pages.checkout;

import devsu.core.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object for the SauceDemo checkout step-two page (order summary before finishing). */
public class CheckoutOverviewPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By summaryContainer = By.cssSelector("[data-test='checkout-summary-container']");
    private By cartItems = By.cssSelector("[data-test='inventory-item']");
    private By finishButton = By.cssSelector("[data-test='finish']");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getIntProperty("timeout")));
    }

    public boolean isCheckoutOverviewPageDisplayed() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(summaryContainer)).isDisplayed();
    }

    public int getItemCount() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItems)).size();
    }

    public void clickFinish() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(finishButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }
}
