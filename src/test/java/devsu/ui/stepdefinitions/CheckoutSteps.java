package devsu.ui.stepdefinitions;

import devsu.core.driver.DriverFactory;
import devsu.ui.pages.cart.CartPage;
import devsu.ui.pages.checkout.CheckoutCompletePage;
import devsu.ui.pages.checkout.CheckoutOverviewPage;
import devsu.ui.pages.checkout.CheckoutPage;
import devsu.ui.pages.inventory.InventoryPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for the complete E2E purchase flow.
 * Steps must execute in order: inventory → cart → checkout → overview → complete.
 */
public class CheckoutSteps {

    private static final int EXPECTED_PRODUCT_COUNT = 2;

    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage checkoutOverviewPage;
    private CheckoutCompletePage checkoutCompletePage;

    @And("the user adds two products to the cart from the Inventory")
    public void theUserAddsTwoProductsToTheCartFromTheInventory() {
        inventoryPage = new InventoryPage(DriverFactory.getDriver());

        inventoryPage.addProductOneToCart();
        inventoryPage.addProductTwoToCart();

        Assertions.assertEquals(
                String.valueOf(EXPECTED_PRODUCT_COUNT),
                inventoryPage.getCartBadgeText(),
                "Cart badge should show " + EXPECTED_PRODUCT_COUNT + " products after adding both items"
        );
    }

    @And("the user opens the cart from the Inventory")
    public void theUserOpensTheCartFromTheInventory() {
        inventoryPage.clickCartIcon();

        cartPage = new CartPage(DriverFactory.getDriver());

        Assertions.assertTrue(
                cartPage.isCartPageDisplayed(),
                "Cart page is not displayed correctly"
        );

        Assertions.assertEquals(
                EXPECTED_PRODUCT_COUNT,
                cartPage.getItemCount(),
                "Cart should contain exactly " + EXPECTED_PRODUCT_COUNT + " products"
        );
    }

    @When("the user proceeds to checkout")
    public void theUserProceedsToCheckout() {
        cartPage.clickCheckout();

        checkoutPage = new CheckoutPage(DriverFactory.getDriver());

        Assertions.assertTrue(
                checkoutPage.isCheckoutPageDisplayed(),
                "Checkout page is not displayed correctly"
        );
    }

    @And("the user fills the checkout form with first name {string}, last name {string} and zip code {string}")
    public void theUserFillsTheCheckoutForm(String firstName, String lastName, String zipCode) {
        checkoutPage.enterFirstName(firstName);
        checkoutPage.enterLastName(lastName);
        checkoutPage.enterZipCode(zipCode);
        checkoutPage.clickContinue();

        checkoutOverviewPage = new CheckoutOverviewPage(DriverFactory.getDriver());

        Assertions.assertTrue(
                checkoutOverviewPage.isCheckoutOverviewPageDisplayed(),
                "Checkout overview page is not displayed correctly"
        );

        Assertions.assertEquals(
                EXPECTED_PRODUCT_COUNT,
                checkoutOverviewPage.getItemCount(),
                "Checkout overview should show exactly " + EXPECTED_PRODUCT_COUNT + " products"
        );
    }

    @And("the user finishes the purchase")
    public void theUserFinishesThePurchase() {
        checkoutOverviewPage.clickFinish();

        checkoutCompletePage = new CheckoutCompletePage(DriverFactory.getDriver());

        Assertions.assertTrue(
                checkoutCompletePage.isCheckoutCompletePageDisplayed(),
                "Checkout complete page is not displayed correctly"
        );
    }

    @Then("the order confirmation {string} should be displayed")
    public void theOrderConfirmationShouldBeDisplayed(String expectedHeader) {
        Assertions.assertEquals(
                expectedHeader,
                checkoutCompletePage.getConfirmationHeader(),
                "Order confirmation header does not match the expected message"
        );
    }
}
