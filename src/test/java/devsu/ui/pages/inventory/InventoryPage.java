package devsu.ui.pages.inventory;

import devsu.core.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page Object for the SauceDemo inventory (products) page. */
public class InventoryPage {

    private WebDriverWait wait;

    private By addToCartProductOne = By.id("add-to-cart-sauce-labs-backpack");
    private By addToCartProductTwo = By.id("add-to-cart-sauce-labs-bike-light");
    private By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private By cartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public InventoryPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getIntProperty("timeout")));
    }

    public void addProductOneToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartProductOne)).click();
    }

    public void addProductTwoToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartProductTwo)).click();
    }

    public String getCartBadgeText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    public void clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }
}
