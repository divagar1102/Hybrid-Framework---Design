package com.framework.utils;

import com.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitUtils - centralised wait strategies.
 * Covers explicit, fluent, custom condition, and polling waits.
 */
public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;
    private final WebDriverWait longWait;
    private final int defaultTimeout;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        ConfigManager config = ConfigManager.getInstance();
        this.defaultTimeout = config.getExplicitWait();
        this.wait      = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeout));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.longWait  = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    // ─── Visibility ────────────────────────────────────────────────────────────

    public WebElement waitForVisibility(By locator) {
        log.debug("Waiting for visibility of: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForVisibility(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> waitForVisibilityOfAll(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // ─── Clickability ──────────────────────────────────────────────────────────

    public WebElement waitForElementToBeClickable(By locator) {
        log.debug("Waiting for clickability of: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // ─── Presence ──────────────────────────────────────────────────────────────

    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitForPresenceOfAll(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    // ─── Invisibility ──────────────────────────────────────────────────────────

    public boolean waitForInvisibility(By locator) {
        log.debug("Waiting for invisibility of: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    // ─── Text / Attribute ──────────────────────────────────────────────────────

    public boolean waitForTextInElement(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public boolean waitForTextInValue(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementValue(locator, text));
    }

    public boolean waitForAttributeContains(By locator, String attribute, String value) {
        return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    public boolean waitForAttributeToBe(By locator, String attribute, String value) {
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    // ─── URL / Title ───────────────────────────────────────────────────────────

    public boolean waitForUrlContains(String fraction) {
        return wait.until(ExpectedConditions.urlContains(fraction));
    }

    public boolean waitForUrlToBe(String url) {
        return wait.until(ExpectedConditions.urlToBe(url));
    }

    public boolean waitForTitleContains(String text) {
        return wait.until(ExpectedConditions.titleContains(text));
    }

    public boolean waitForTitleToBe(String title) {
        return wait.until(ExpectedConditions.titleIs(title));
    }

    // ─── Page Ready ────────────────────────────────────────────────────────────

    public void waitForPageLoad() {
        wait.until(driver -> {
            String state = (String) ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState");
            return "complete".equals(state);
        });
    }

    public void waitForAjaxToComplete() {
        wait.until(driver -> {
            Object result = ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active == 0");
            return result instanceof Boolean && (Boolean) result;
        });
    }

    // ─── Alert ─────────────────────────────────────────────────────────────────

    public Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    // ─── Frame ─────────────────────────────────────────────────────────────────

    public WebDriver waitForFrameAndSwitch(By locator) {
        return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public WebDriver waitForFrameAndSwitch(int index) {
        return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
    }

    // ─── Selection ─────────────────────────────────────────────────────────────

    public boolean waitForElementToBeSelected(By locator) {
        return wait.until(ExpectedConditions.elementToBeSelected(locator));
    }

    public boolean waitForElementSelectionStateToBe(By locator, boolean selected) {
        return wait.until(ExpectedConditions.elementSelectionStateToBe(locator, selected));
    }

    // ─── Number of Elements ────────────────────────────────────────────────────

    public List<WebElement> waitForNumberOfElementsToBeMoreThan(By locator, int count) {
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, count));
    }

    public List<WebElement> waitForNumberOfElementsToBe(By locator, int count) {
        return wait.until(ExpectedConditions.numberOfElementsToBe(locator, count));
    }

    // ─── Fluent Wait ───────────────────────────────────────────────────────────

    public <T> T fluentWait(ExpectedCondition<T> condition, int timeoutSeconds, int pollingMillis) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class);
        return fluentWait.until(condition);
    }

    public WebElement fluentWaitForElement(By locator, int timeoutSeconds, int pollingMillis) {
        return fluentWait(
            ExpectedConditions.visibilityOfElementLocated(locator),
            timeoutSeconds,
            pollingMillis
        );
    }

    // ─── Short / Long Waits ────────────────────────────────────────────────────

    public WebElement shortWaitForVisibility(By locator) {
        return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement longWaitForVisibility(By locator) {
        return longWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // ─── Thread Sleep (use sparingly) ──────────────────────────────────────────

    public void hardWait(long millis) {
        try {
            log.warn("Using hardWait for {}ms — prefer explicit waits.", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
