package com.bookcart.pages;

import com.bookcart.base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.logging.Logger;

public class CheckoutPage extends BaseTest {
    private static final Logger logger = Logger.getLogger(CheckoutPage.class.getName());

    // Locators
    @FindBy(xpath = "//mat-card-title[contains(@class, 'mat-card-title') and contains(., 'Login')]")
    private WebElement loginTitle;
    
    @FindBy(xpath = "//input[@formcontrolname='username' or @placeholder='Username']")
    private WebElement usernameField;
    
    @FindBy(xpath = "//input[@formcontrolname='password' or @type='password']")
    private WebElement passwordField;

    // Constructor
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }
    
    // Page methods
    public boolean isLoginPageDisplayed() {
        try {
            boolean displayed = isElementDisplayed(loginTitle, "Login Title");
            if (displayed) {
                logger.info("Login page is displayed");
            } else {
                logger.warning("Login page is not displayed");
            }
            return displayed;
        } catch (Exception e) {
            logger.warning("Error checking if login page is displayed: " + e.getMessage());
            return false;
        }
    }
    
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: " + title);
        return title;
    }
}
