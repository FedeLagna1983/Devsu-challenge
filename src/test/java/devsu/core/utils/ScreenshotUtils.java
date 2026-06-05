package devsu.core.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Captures browser screenshots on test failure and saves them under {@code screenshots/}. */
public class ScreenshotUtils {

    public static String takeScreenshot(WebDriver driver, String scenarioName) {
        if (!(driver instanceof TakesScreenshot)) {
            throw new RuntimeException("Driver does not support screenshots: " + driver.getClass().getName());
        }

        Path screenshotsDir = Paths.get(System.getProperty("user.dir"), "screenshots");

        try {
            Files.createDirectories(screenshotsDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create screenshots directory", e);
        }

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String cleanScenarioName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");

        Path destinationPath = screenshotsDir.resolve(cleanScenarioName + "_" + timestamp + ".png");

        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            Files.copy(sourceFile.toPath(), destinationPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not save screenshot: " + destinationPath, e);
        }

        return destinationPath.toString();
    }
}
