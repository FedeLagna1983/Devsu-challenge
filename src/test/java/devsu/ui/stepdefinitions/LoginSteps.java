package devsu.ui.stepdefinitions;

import devsu.core.config.ConfigReader;
import devsu.core.driver.DriverFactory;
import devsu.ui.pages.login.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

/** Step definitions for the SauceDemo login steps. */
public class LoginSteps {

    private LoginPage loginPage;

    @Given("the user opens the SauceDemo login page")
    public void theUserOpensTheSauceDemoLoginPage() {
        DriverFactory.getDriver().get(ConfigReader.getProperty("baseUrl"));
        loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        loginPage.login(username, password);
    }
}
