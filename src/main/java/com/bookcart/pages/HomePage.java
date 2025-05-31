package com.bookcart.pages;

import com.bookcart.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.logging.Logger;

public class HomePage extends BaseTest {
    private static final Logger logger = Logger.getLogger(HomePage.class.getName());

    // Locators
    @FindBy(xpath = "//input[contains(@class, 'search-input') and @placeholder='Search books or authors']")
    private WebElement searchBox;
    
    @FindBy(xpath = "//button[contains(@class, 'search-btn') and @type='button' and .//*[local-name()='svg' or local-name()='mat-icon']]")
    private WebElement searchButton;
    
    @FindBy(xpath = "//button[contains(@class, 'mat-icon-button')]//mat-icon[contains(., 'shopping_cart')]")
    private WebElement cartIcon;
    
    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }
    
    // Page methods
    public void searchForProduct(String productName) {
        logger.info("Searching for product: " + productName);
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        type(searchBox, productName, "Search Box");
        click(searchButton, "Search Button");
    }
    
    public void addProductToCart(String productName) {
        logger.info("Adding product to cart: " + productName);
        String addToCartButtonXPath = String.format(
            "//a[contains(normalize-space(),'%s')]/ancestor::mat-card//button[contains(@class, 'mat-flat-button') and contains(., 'Add to Cart')]", 
            productName);
        
        try {
            WebElement addToCartButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(addToCartButtonXPath)));
            click(addToCartButton, "Add to Cart button for " + productName);
            
            // Wait for cart to update
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'cdk-overlay-backdrop')]")));
                
        } catch (Exception e) {
            logger.severe("Failed to add product to cart: " + e.getMessage());
            takeScreenshot("add_to_cart_error_" + productName.replace(" ", "_"));
            throw e;
        }
    }
    
    public void clickCartIcon() {
        logger.info("Clicking on cart icon");
        click(cartIcon, "Cart Icon");
        waitForPageLoad();
    }
    
    public boolean isProductDisplayed(String productName) {
        String productXPath = String.format(
            "//a[contains(normalize-space(), '%s') and contains(@class, 'title')]", 
            productName);
            
        try {
            WebElement product = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(productXPath)));
            logger.info("Product found: " + productName);
            return product.isDisplayed();
        } catch (Exception e) {
            logger.warning("Product not found: " + productName);
            return false;
        }
    }
}
