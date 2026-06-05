package devsu.ui.runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/ui")
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,
        value = "devsu.ui.stepdefinitions,devsu.ui.hooks"
)
@ConfigurationParameter(
        key = Constants.PLUGIN_PROPERTY_NAME,
        value = "pretty, html:reports/ui/cucumber-report.html, json:reports/ui/cucumber-report.json"
)
public class UiTestRunner {
}
