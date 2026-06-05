package devsu.ui.pages.cart;

import devsu.core.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object for the SauceDemo cart page. */
public class CartPage {

    private WebDriverWait wait;

    private By pageTitle = By.cssSelector("[data-test='title']");
    private By cartItem = By.cssSelector("[data-test='inventory-item']");
    private By checkoutButton = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getIntProperty("timeout")));
    }

    public boolean isCartPageDisplayed() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle))
                .getText().equals("Your Cart");
    }

    public int getItemCount() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItem)).size();
    }

    public void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }
}
