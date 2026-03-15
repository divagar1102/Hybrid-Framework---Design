package com.framework.stepdefinitions.sample;

import com.framework.pages.sample.HomePage;
import com.framework.pages.sample.LoginPage;
import com.framework.utils.AssertionUtils;
import com.framework.utils.ExtentReportManager;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LoginSteps - step definitions for Login.feature and shared login Background steps.
 */
public class LoginSteps {

    private static final Logger log = LogManager.getLogger(LoginSteps.class);

    private LoginPage loginPage;
    private HomePage  homePage;

    // ─── Background / Preconditions ────────────────────────────────────────────

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginPage = new LoginPage().open();
        ExtentReportManager.logInfo("Opened login page");
        AssertionUtils.assertTrue(loginPage.isLoginPageDisplayed(),
            "Login page should be displayed");
    }

    @Given("the user is logged in as {string} with password {string}")
    public void theUserIsLoggedInAs(String username, String password) {
        loginPage = new LoginPage().open();
        homePage  = loginPage.login(username, password);
        ExtentReportManager.logInfo("Logged in as: " + username);
        AssertionUtils.assertTrue(homePage.isHomePageDisplayed(),
            "Home page should be displayed after login");
    }

    // ─── Actions ───────────────────────────────────────────────────────────────

    @When("the user enters username {string}")
    public void theUserEntersUsername(String username) {
        loginPage.enterUsername(username);
        ExtentReportManager.logInfo("Entered username: " + username);
    }

    @When("the user enters password {string}")
    public void theUserEntersPassword(String password) {
        loginPage.enterPassword(password);
        ExtentReportManager.logInfo("Entered password");
    }

    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        // Determine if we expect success or failure based on page state
        if (loginPage.isLoginPageDisplayed()) {
            try {
                homePage = loginPage.clickLoginButton();
            } catch (Exception e) {
                // Login failed - loginPage state is preserved for error assertions
                log.info("Login did not proceed to home page (expected for negative tests)");
            }
        }
        ExtentReportManager.logInfo("Clicked login button");
    }

    @When("the user clicks the burger menu")
    public void theUserClicksTheBurgerMenu() {
        homePage.openBurgerMenu();
        ExtentReportManager.logInfo("Clicked burger menu");
    }

    @When("the user clicks the logout button")
    public void theUserClicksTheLogoutButton() {
        loginPage = homePage.logout();
        ExtentReportManager.logInfo("Clicked logout button");
    }

    // ─── Assertions ────────────────────────────────────────────────────────────

    @Then("the user should be redirected to the home page")
    public void theUserShouldBeRedirectedToTheHomePage() {
        homePage = new HomePage();
        AssertionUtils.assertTrue(homePage.isHomePageDisplayed(),
            "User should be on the home page after successful login");
        ExtentReportManager.logPass("User redirected to home page");
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        String actualTitle = homePage.getPageTitle();
        AssertionUtils.assertEquals(actualTitle, expectedTitle,
            "Page title should match expected value");
        ExtentReportManager.logPass("Page title verified: " + actualTitle);
    }

    @Then("the user should see an error message")
    public void theUserShouldSeeAnErrorMessage() {
        loginPage = new LoginPage();
        AssertionUtils.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be visible");
        ExtentReportManager.logPass("Error message is displayed");
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedText) {
        loginPage = new LoginPage();
        String actual = loginPage.getErrorMessage();
        AssertionUtils.assertContains(actual, expectedText,
            "Error message text verification");
        ExtentReportManager.logPass("Error message contains expected text: " + expectedText);
    }

    @Then("the user should be on the login page")
    public void theUserShouldBeOnTheLoginPage() {
        loginPage = new LoginPage();
        AssertionUtils.assertTrue(loginPage.isLoginPageDisplayed(),
            "Login page should be visible after logout");
        ExtentReportManager.logPass("User is on the login page");
    }
}
