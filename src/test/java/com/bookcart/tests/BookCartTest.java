package com.bookcart.tests;

import com.bookcart.base.BaseTest;
import com.bookcart.pages.CartPage;
import com.bookcart.pages.CheckoutPage;
import com.bookcart.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class BookCartTest extends BaseTest {
    private static final Logger logger = Logger.getLogger(BookCartTest.class.getName());
    
    @Test(description = "Test adding a product to cart and proceeding to checkout")
    public void testAddToCartAndCheckout() {
        try {
            // Test data
            String productName = "The Secret";
            logger.info("Starting test with product: " + productName);
            
            // Initialize page objects
            HomePage homePage = new HomePage(driver);
            
            // 1. Search for a product
            logger.info("Step 1: Searching for product - " + productName);
            homePage.searchForProduct(productName);
            
            // Verify search results
            logger.info("Verifying search results");
            boolean isProductDisplayed = homePage.isProductDisplayed(productName);
            Assert.assertTrue(isProductDisplayed, 
                "Product should be displayed in search results");
            
            // 2. Add product to cart
            logger.info("Step 2: Adding product to cart");
            homePage.addProductToCart(productName);
            
            // 3. Go to cart
            logger.info("Step 3: Navigating to cart");
            homePage.clickCartIcon();
            
            // Initialize CartPage
            CartPage cartPage = new CartPage(driver);
            
            // Verify cart page is displayed and product is in cart
            logger.info("Verifying cart page and product in cart");
            boolean isCartPageDisplayed = cartPage.isCartPageDisplayed();
            Assert.assertTrue(isCartPageDisplayed, "Cart page should be displayed");
            
            boolean isProductInCart = cartPage.isProductInCart(productName);
            Assert.assertTrue(isProductInCart, "Product should be in the cart");
            
            // 4. Proceed to checkout
            logger.info("Step 4: Proceeding to checkout");
            cartPage.clickCheckoutButton();
            
            // Initialize CheckoutPage
            CheckoutPage checkoutPage = new CheckoutPage(driver);
            
            // Verify we're on the login page (checkout requires login)
            logger.info("Verifying login page is displayed");
            boolean isLoginPageDisplayed = checkoutPage.isLoginPageDisplayed();
            Assert.assertTrue(isLoginPageDisplayed, 
                "Should be redirected to login page for checkout");
                
            logger.info("Test completed successfully");
            
        } catch (Exception e) {
            logger.severe("Test failed: " + e.getMessage());
            takeScreenshot("test_failure");
            throw e;
        }
    }
}
