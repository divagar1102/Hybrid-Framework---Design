package com.framework.pages;

import com.framework.config.ConfigManager;
import com.framework.driver.DriverManager;
import com.framework.utils.WaitUtils;
import com.framework.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BasePage - abstract parent for all Page Objects.
 * Provides reusable, robust Selenium interactions with built-in waits and logging.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected WebDriver driver;
    protected WaitUtils wait;
    protected Actions actions;
    protected JavascriptExecutor js;
    protected ConfigManager config;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait   = new WaitUtils(driver);
        this.actions = new Actions(driver);
        this.js      = (JavascriptExecutor) driver;
        this.config  = ConfigManager.getInstance();
        PageFactory.initElements(driver, this);
    }

    // ─── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public void navigateToBaseUrl() {
        navigateTo(config.getBaseUrl());
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void refreshPage() {
        log.info("Refreshing page");
        driver.navigate().refresh();
    }

    public void goBack() {
        driver.navigate().back();
    }

    public void goForward() {
        driver.navigate().forward();
    }

    // ─── Click Actions ─────────────────────────────────────────────────────────

    public void click(By locator) {
        WebElement element = wait.waitForElementToBeClickable(locator);
        log.debug("Clicking element: {}", locator);
        element.click();
    }

    public void click(WebElement element) {
        wait.waitForElementToBeClickable(element);
        log.debug("Clicking WebElement");
        element.click();
    }

    public void jsClick(By locator) {
        WebElement element = wait.waitForVisibility(locator);
        log.debug("JS clicking element: {}", locator);
        js.executeScript("arguments[0].click();", element);
    }

    public void jsClick(WebElement element) {
        log.debug("JS clicking WebElement");
        js.executeScript("arguments[0].click();", element);
    }

    public void doubleClick(By locator) {
        WebElement element = wait.waitForElementToBeClickable(locator);
        actions.doubleClick(element).perform();
    }

    public void rightClick(By locator) {
        WebElement element = wait.waitForElementToBeClickable(locator);
        actions.contextClick(element).perform();
    }

    // ─── Input Actions ─────────────────────────────────────────────────────────

    public void sendKeys(By locator, String text) {
        WebElement element = wait.waitForVisibility(locator);
        log.debug("Typing '{}' into element: {}", text, locator);
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeys(WebElement element, String text) {
        wait.waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeysWithoutClear(By locator, String text) {
        wait.waitForVisibility(locator).sendKeys(text);
    }

    public void clearAndType(By locator, String text) {
        WebElement element = wait.waitForVisibility(locator);
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    public void pressKey(By locator, Keys key) {
        wait.waitForVisibility(locator).sendKeys(key);
    }

    // ─── Get Element Info ──────────────────────────────────────────────────────

    public String getText(By locator) {
        return wait.waitForVisibility(locator).getText().trim();
    }

    public String getText(WebElement element) {
        wait.waitForVisibility(element);
        return element.getText().trim();
    }

    public String getAttribute(By locator, String attribute) {
        return wait.waitForVisibility(locator).getAttribute(attribute);
    }

    public String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    public String getCssValue(By locator, String property) {
        return wait.waitForVisibility(locator).getCssValue(property);
    }

    // ─── Element State ─────────────────────────────────────────────────────────

    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isSelected(By locator) {
        try {
            return driver.findElement(locator).isSelected();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    // ─── Waits ─────────────────────────────────────────────────────────────────

    public WebElement waitForElement(By locator) {
        return wait.waitForVisibility(locator);
    }

    public void waitForElementToDisappear(By locator) {
        wait.waitForInvisibility(locator);
    }

    public void waitForTextToBePresentInElement(By locator, String text) {
        wait.waitForTextInElement(locator, text);
    }

    // ─── Dropdown ──────────────────────────────────────────────────────────────

    public void selectByVisibleText(By locator, String text) {
        Select select = new Select(wait.waitForVisibility(locator));
        log.debug("Selecting '{}' by visible text from: {}", text, locator);
        select.selectByVisibleText(text);
    }

    public void selectByValue(By locator, String value) {
        Select select = new Select(wait.waitForVisibility(locator));
        select.selectByValue(value);
    }

    public void selectByIndex(By locator, int index) {
        Select select = new Select(wait.waitForVisibility(locator));
        select.selectByIndex(index);
    }

    public String getSelectedDropdownText(By locator) {
        return new Select(wait.waitForVisibility(locator)).getFirstSelectedOption().getText();
    }

    // ─── Multiple Elements ─────────────────────────────────────────────────────

    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    public List<String> getElementTexts(By locator) {
        return driver.findElements(locator)
                     .stream()
                     .map(e -> e.getText().trim())
                     .collect(Collectors.toList());
    }

    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    // ─── Scroll ────────────────────────────────────────────────────────────────

    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);
    }

    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);
    }

    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void scrollBy(int x, int y) {
        js.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    // ─── Hover / Drag ──────────────────────────────────────────────────────────

    public void hoverOver(By locator) {
        WebElement element = wait.waitForVisibility(locator);
        actions.moveToElement(element).perform();
    }

    public void dragAndDrop(By source, By target) {
        WebElement src = wait.waitForVisibility(source);
        WebElement tgt = wait.waitForVisibility(target);
        actions.dragAndDrop(src, tgt).perform();
    }

    // ─── Alerts ────────────────────────────────────────────────────────────────

    public String getAlertText() {
        return wait.waitForAlert().getText();
    }

    public void acceptAlert() {
        wait.waitForAlert().accept();
    }

    public void dismissAlert() {
        wait.waitForAlert().dismiss();
    }

    public void typeInAlert(String text) {
        Alert alert = wait.waitForAlert();
        alert.sendKeys(text);
        alert.accept();
    }

    // ─── Frames ────────────────────────────────────────────────────────────────

    public void switchToFrame(By locator) {
        driver.switchTo().frame(wait.waitForVisibility(locator));
    }

    public void switchToFrame(int index) {
        driver.switchTo().frame(index);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // ─── Windows / Tabs ────────────────────────────────────────────────────────

    public void switchToNewWindow() {
        String current = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(current)) {
                driver.switchTo().window(handle);
                return;
            }
        }
    }

    public void switchToWindow(String handle) {
        driver.switchTo().window(handle);
    }

    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    public void closeCurrentWindow() {
        driver.close();
    }

    // ─── JavaScript Utilities ──────────────────────────────────────────────────

    public Object executeScript(String script, Object... args) {
        return js.executeScript(script, args);
    }

    public void highlightElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    public void setElementValue(By locator, String value) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].value='" + value + "';", element);
    }

    // ─── Screenshot ────────────────────────────────────────────────────────────

    public byte[] takeScreenshot() {
        return ScreenshotUtils.captureScreenshot(driver);
    }

    public String takeAndSaveScreenshot(String name) {
        return ScreenshotUtils.captureAndSave(driver, name);
    }
}
