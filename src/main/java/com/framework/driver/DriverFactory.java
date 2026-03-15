package com.framework.driver;

import com.framework.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory - creates and configures WebDriver instances.
 * Supports Chrome, Firefox, Edge, Safari, and Remote (Selenium Grid / BrowserStack / SauceLabs).
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    private DriverFactory() {}

    public static WebDriver createDriver() {
        String browser = System.getProperty("browser", config.getBrowser()).toLowerCase().trim();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(config.isHeadless())));
        boolean remote = config.isRemote();

        log.info("Initialising WebDriver | Browser: {} | Headless: {} | Remote: {}", browser, headless, remote);

        WebDriver driver;

        if (remote) {
            driver = createRemoteDriver(browser, headless);
        } else {
            driver = switch (browser) {
                case "chrome"   -> createChromeDriver(headless);
                case "firefox"  -> createFirefoxDriver(headless);
                case "edge"     -> createEdgeDriver(headless);
                case "safari"   -> createSafariDriver();
                default -> {
                    log.warn("Unknown browser '{}'. Falling back to Chrome.", browser);
                    yield createChromeDriver(headless);
                }
            };
        }

        configureTimeouts(driver);
        driver.manage().window().maximize();
        log.info("WebDriver created successfully.");
        return driver;
    }

    // ─── Chrome ────────────────────────────────────────────────────────────────

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = buildChromeOptions(headless);
        return new ChromeDriver(options);
    }

    public static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--disable-extensions",
            "--disable-infobars",
            "--disable-notifications",
            "--disable-popup-blocking",
            "--remote-allow-origins=*",
            "--window-size=1920,1080",
            "--lang=en-US"
        );

        // Disable password save dialog
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("download.default_directory", System.getProperty("user.dir") + "/target/downloads");
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);
        options.setExperimentalOption("prefs", prefs);

        // Exclude automation flags
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation", "enable-logging"));
        options.setExperimentalOption("useAutomationExtension", false);

        return options;
    }

    // ─── Firefox ───────────────────────────────────────────────────────────────

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", System.getProperty("user.dir") + "/target/downloads");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/octet-stream");
        profile.setPreference("pdfjs.disabled", true);
        options.setProfile(profile);
        options.addArguments("--width=1920", "--height=1080");

        return new FirefoxDriver(options);
    }

    // ─── Edge ──────────────────────────────────────────────────────────────────

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080"
        );

        return new EdgeDriver(options);
    }

    // ─── Safari ────────────────────────────────────────────────────────────────

    private static WebDriver createSafariDriver() {
        // Safari requires 'Allow Remote Automation' to be enabled in Developer menu
        return new SafariDriver();
    }

    // ─── Remote / Selenium Grid ────────────────────────────────────────────────

    private static WebDriver createRemoteDriver(String browser, boolean headless) {
        String remoteUrl = config.getRemoteUrl();
        log.info("Connecting to Remote Grid: {}", remoteUrl);

        try {
            return switch (browser) {
                case "firefox" -> new RemoteWebDriver(new URL(remoteUrl), buildFirefoxOptions(headless));
                case "edge"    -> new RemoteWebDriver(new URL(remoteUrl), buildEdgeOptions(headless));
                default        -> new RemoteWebDriver(new URL(remoteUrl), buildChromeOptions(headless));
            };
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Remote WebDriver URL: " + remoteUrl, e);
        }
    }

    private static FirefoxOptions buildFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("--headless");
        return options;
    }

    private static EdgeOptions buildEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) options.addArguments("--headless=new");
        return options;
    }

    // ─── Timeouts ──────────────────────────────────────────────────────────────

    private static void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
    }
}
