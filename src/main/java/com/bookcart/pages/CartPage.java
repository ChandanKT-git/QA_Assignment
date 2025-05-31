package com.bookcart.pages;

import com.bookcart.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.logging.Logger;

public class CartPage extends BaseTest {
    private static final Logger logger = Logger.getLogger(CartPage.class.getName());

    // Locators
    @FindBy(xpath = "//button[contains(@class, 'mat-flat-button') and contains(@class, 'mat-primary')]//span[contains(., 'Checkout') or contains(., 'Proceed to Checkout')]")
    private WebElement checkoutButton;
    
    @FindBy(xpath = "//mat-card-title[contains(@class, 'mat-card-title') and contains(., 'Shopping cart')]")
    private WebElement cartTitle;

    // Constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }
    
    // Page methods
    public boolean isCartPageDisplayed() {
        try {
            boolean displayed = isElementDisplayed(cartTitle, "Cart Title");
            if (displayed) {
                logger.info("Cart page is displayed");
            } else {
                logger.warning("Cart page is not displayed");
            }
            return displayed;
        } catch (Exception e) {
            logger.warning("Error checking if cart page is displayed: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isProductInCart(String productName) {
        String productXPath = String.format(
            "//div[contains(@class, 'item-name')]//a[contains(normalize-space(), '%s')]", 
            productName);
            
        try {
            WebElement product = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(productXPath)));
            logger.info("Product found in cart: " + productName);
            return product.isDisplayed();
        } catch (Exception e) {
            logger.warning("Product not found in cart: " + productName);
            return false;
        }
    }
    
    public void clickCheckoutButton() {
        logger.info("Clicking on Checkout button");
        try {
            click(checkoutButton, "Checkout Button");
            waitForPageLoad();
        } catch (Exception e) {
            logger.severe("Failed to click checkout button: " + e.getMessage());
            takeScreenshot("checkout_button_error");
            throw e;
        }
    }
}
