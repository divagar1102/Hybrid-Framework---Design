package com.framework.pages.sample;

import com.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * HomePage - Page Object for https://www.saucedemo.com inventory/home page.
 */
public class HomePage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerMenuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = ".shopping_cart_link")
    private WebElement shoppingCartIcon;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    private static final By PAGE_TITLE       = By.cssSelector(".title");
    private static final By INVENTORY_ITEMS  = By.cssSelector(".inventory_item");
    private static final By SORT_DROPDOWN    = By.cssSelector("[data-test='product_sort_container']");
    private static final By CART_BADGE       = By.cssSelector(".shopping_cart_badge");
    private static final By BURGER_MENU      = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK      = By.id("logout_sidebar_link");
    private static final By ADD_TO_CART_BTN  = By.cssSelector(".btn_inventory");

    // ─── Page Actions ──────────────────────────────────────────────────────────

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public boolean isHomePageDisplayed() {
        return isDisplayed(PAGE_TITLE) && getText(PAGE_TITLE).equalsIgnoreCase("Products");
    }

    public int getInventoryItemCount() {
        return getElements(INVENTORY_ITEMS).size();
    }

    public void sortProductsBy(String option) {
        log.info("Sorting products by: {}", option);
        selectByVisibleText(SORT_DROPDOWN, option);
    }

    public void addFirstItemToCart() {
        List<WebElement> addButtons = getElements(ADD_TO_CART_BTN);
        if (!addButtons.isEmpty()) {
            click(addButtons.get(0));
            log.info("Added first item to cart");
        }
    }

    public void addItemToCartByName(String productName) {
        By addButton = By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button");
        click(addButton);
        log.info("Added '{}' to cart", productName);
    }

    public int getCartBadgeCount() {
        if (isElementPresent(CART_BADGE)) {
            return Integer.parseInt(getText(CART_BADGE));
        }
        return 0;
    }

    public void openBurgerMenu() {
        click(burgerMenuButton);
        log.info("Opened burger menu");
    }

    public LoginPage logout() {
        openBurgerMenu();
        wait.waitForElementToBeClickable(LOGOUT_LINK);
        click(logoutLink);
        log.info("Logged out");
        return new LoginPage();
    }

    public List<String> getAllProductNames() {
        return getElementTexts(By.cssSelector(".inventory_item_name"));
    }

    public List<String> getAllProductPrices() {
        return getElementTexts(By.cssSelector(".inventory_item_price"));
    }
}
