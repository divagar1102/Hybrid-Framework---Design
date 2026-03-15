package com.framework.stepdefinitions.sample;

import com.framework.pages.sample.HomePage;
import com.framework.utils.AssertionUtils;
import com.framework.utils.ExtentReportManager;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * ProductSteps - step definitions for Products.feature.
 */
public class ProductSteps {

    private static final Logger log = LogManager.getLogger(ProductSteps.class);
    private HomePage homePage = new HomePage();

    @Then("the home page should display products")
    public void theHomePageShouldDisplayProducts() {
        AssertionUtils.assertTrue(homePage.isHomePageDisplayed(),
            "Home page should be displayed with products");
        ExtentReportManager.logPass("Home page is displaying products");
    }

    @Then("the product count should be greater than {int}")
    public void theProductCountShouldBeGreaterThan(int minCount) {
        int count = homePage.getInventoryItemCount();
        AssertionUtils.assertTrue(count > minCount,
            "Product count should be greater than " + minCount + ". Actual: " + count);
        ExtentReportManager.logPass("Product count: " + count);
    }

    @When("the user sorts products by {string}")
    public void theUserSortsProductsBy(String sortOption) {
        homePage.sortProductsBy(sortOption);
        ExtentReportManager.logInfo("Sorted products by: " + sortOption);
    }

    @Then("the products should be displayed in ascending name order")
    public void theProductsShouldBeDisplayedInAscendingNameOrder() {
        List<String> productNames = homePage.getAllProductNames();
        List<String> sorted = productNames.stream().sorted().toList();
        AssertionUtils.assertEquals(productNames, sorted,
            "Products should be in ascending alphabetical order");
        ExtentReportManager.logPass("Products are sorted in ascending order");
    }

    @When("the user adds the first product to the cart")
    public void theUserAddsTheFirstProductToTheCart() {
        homePage.addFirstItemToCart();
        ExtentReportManager.logInfo("Added first product to cart");
    }

    @Then("the cart badge should show {string}")
    public void theCartBadgeShouldShow(String expectedCount) {
        int actual = homePage.getCartBadgeCount();
        AssertionUtils.assertEquals(String.valueOf(actual), expectedCount,
            "Cart badge count should match expected");
        ExtentReportManager.logPass("Cart badge shows: " + actual);
    }

    @Then("all product names should be visible on the page")
    public void allProductNamesShouldBeVisibleOnThePage() {
        List<String> names = homePage.getAllProductNames();
        AssertionUtils.assertTrue(!names.isEmpty(), "Product names list should not be empty");
        names.forEach(name -> AssertionUtils.assertTrue(!name.isEmpty(),
            "Product name should not be empty"));
        ExtentReportManager.logPass("All " + names.size() + " product names are visible");
    }
}
