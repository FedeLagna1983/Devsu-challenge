package devsu.ui.hooks;

import devsu.core.driver.DriverFactory;
import devsu.core.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/** Cucumber lifecycle hooks: initialises the WebDriver before each scenario and quits it after, capturing a screenshot on failure. */
public class Hooks {

    @Before
    public void setUp() {
        DriverFactory.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {
            ScreenshotUtils.takeScreenshot(
                    DriverFactory.getDriver(),
                    scenario.getName()
            );
        }

        DriverFactory.quitDriver();
    }
}
