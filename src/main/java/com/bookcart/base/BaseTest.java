package com.bookcart.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = Logger.getLogger(BaseTest.class.getName());
    private static final String SCREENSHOT_DIR = "screenshots";

    @BeforeSuite
    public void beforeSuite() {
        // Create screenshots directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
        } catch (IOException e) {
            logger.warning("Failed to create screenshots directory: " + e.getMessage());
        }
    }

    @BeforeMethod
    public void setUp(ITestResult result) {
        // Setup logging
        setupLogger();
        logger.info("Starting test: " + result.getMethod().getMethodName());
        
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        
        // Initialize ChromeDriver
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Navigate to the BookCart website
        navigateTo("https://bookcart.azurewebsites.net/");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.severe("Test failed: " + result.getThrowable().getMessage());
            takeScreenshot(result.getMethod().getMethodName() + "_FAILED");
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warning("Test was skipped: " + result.getMethod().getMethodName());
        } else {
            logger.info("Test passed: " + result.getMethod().getMethodName());
        }

        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }

    // Utility Methods
    protected void navigateTo(String url) {
        logger.info("Navigating to: " + url);
        driver.get(url);
        waitForPageLoad();
    }

    protected void waitForPageLoad() {
        try {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            logger.warning("Page load wait interrupted: " + e.getMessage());
        }
    }

    protected void click(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            logger.info("Clicked on: " + elementName);
        } catch (Exception e) {
            String errorMsg = "Failed to click on " + elementName + ": " + e.getMessage();
            logger.severe(errorMsg);
            takeScreenshot("click_error_" + elementName);
            throw new NoSuchElementException(errorMsg, e);
        }
    }

    protected void type(WebElement element, String text, String elementName) {
        try {
            element.clear();
            element.sendKeys(text);
            logger.info("Typed '" + text + "' into " + elementName);
        } catch (Exception e) {
            String errorMsg = "Failed to type into " + elementName + ": " + e.getMessage();
            logger.severe(errorMsg);
            takeScreenshot("type_error_" + elementName);
            throw new NoSuchElementException(errorMsg, e);
        }
    }

    protected boolean isElementDisplayed(WebElement element, String elementName) {
        try {
            boolean displayed = wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
            logger.info(elementName + " is " + (displayed ? "displayed" : "not displayed"));
            return displayed;
        } catch (Exception e) {
            logger.warning(elementName + " is not displayed: " + e.getMessage());
            return false;
        }
    }

    private void setupLogger() {
        try {
            // Reset logger handlers to avoid duplicate logs
            Logger rootLogger = Logger.getLogger("");
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            // Create console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);

            // Create file handler
            String logFileName = "test_execution_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                ".log";
            FileHandler fileHandler = new FileHandler(logFileName);
            fileHandler.setLevel(Level.ALL);

            // Create formatter
            SimpleFormatter formatter = new SimpleFormatter();
            consoleHandler.setFormatter(formatter);
            fileHandler.setFormatter(formatter);

            // Add handlers to logger
            logger.addHandler(consoleHandler);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to setup logger: " + e.getMessage());
        }
    }

    protected void takeScreenshot(String testName) {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String screenshotPath = String.format("%s/%s_%s.png", SCREENSHOT_DIR, testName, timestamp);
            Files.copy(screenshotFile.toPath(), Paths.get(screenshotPath));
            logger.info("Screenshot saved to: " + screenshotPath);
        } catch (Exception e) {
            logger.warning("Failed to take screenshot: " + e.getMessage());
        }
    }
}
