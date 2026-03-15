package com.framework.pages.sample;

import com.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for https://www.saucedemo.com login page.
 * Demonstrates PageFactory (@FindBy) + BasePage pattern.
 */
public class LoginPage extends BasePage {

    // ─── Locators via @FindBy (PageFactory) ────────────────────────────────────
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ─── By locators (for dynamic / parameterised lookups) ─────────────────────
    private static final By USERNAME_FIELD   = By.id("user-name");
    private static final By PASSWORD_FIELD   = By.id("password");
    private static final By LOGIN_BUTTON     = By.id("login-button");
    private static final By ERROR_MESSAGE    = By.cssSelector("[data-test='error']");
    private static final By LOGIN_LOGO       = By.className("login_logo");

    // ─── Page Actions ──────────────────────────────────────────────────────────

    public LoginPage open() {
        navigateToBaseUrl();
        log.info("Opened Login Page");
        return this;
    }

    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        sendKeys(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Entering password");
        sendKeys(passwordField, password);
        return this;
    }

    public HomePage clickLoginButton() {
        log.info("Clicking login button");
        click(loginButton);
        return new HomePage();
    }

    public LoginPage clickLoginExpectingError() {
        click(loginButton);
        return this;
    }

    /**
     * Full login flow in one call.
     */
    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    /**
     * Login expecting a failure (returns LoginPage for error assertion).
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginExpectingError();
    }

    // ─── Verification Helpers ──────────────────────────────────────────────────

    public boolean isLoginPageDisplayed() {
        return isDisplayed(LOGIN_LOGO);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public String getUsernamePlaceholder() {
        return getAttribute(USERNAME_FIELD, "placeholder");
    }

    public String getPasswordPlaceholder() {
        return getAttribute(PASSWORD_FIELD, "placeholder");
    }
}
