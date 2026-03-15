package com.framework.hooks;

import com.framework.config.ConfigManager;
import com.framework.driver.DriverManager;
import com.framework.utils.ExtentReportManager;
import com.framework.utils.ScreenshotUtils;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Hooks - global Cucumber Before/After hooks.
 * Handles driver lifecycle, reporting, screenshots on failure.
 */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    // ─── Suite-level Setup ─────────────────────────────────────────────────────

    @BeforeAll
    public static void globalSetup() {
        log.info("═══════════════════════════════════════════════════");
        log.info("  Hybrid Automation Framework — Test Suite Starting");
        log.info("  Browser  : {}", config.getBrowser());
        log.info("  Base URL : {}", config.getBaseUrl());
        log.info("  Headless : {}", config.isHeadless());
        log.info("═══════════════════════════════════════════════════");
        ScreenshotUtils.ensureScreenshotDirExists();
        ExtentReportManager.getExtentReports(); // initialise early
    }

    // ─── Scenario Setup ────────────────────────────────────────────────────────

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        log.info("──────────────────────────────────────────────────────");
        log.info("▶ Starting Scenario: [{}]", scenario.getName());
        log.info("  Tags     : {}", scenario.getSourceTagNames());
        log.info("  Status   : {}", scenario.getStatus());

        // Initialise WebDriver
        DriverManager.initDriver();

        // Create Extent test node
        ExtentReportManager.createTest(scenario.getName(),
            "Tags: " + scenario.getSourceTagNames());
        ExtentReportManager.logInfo("Browser: " + config.getBrowser());
        ExtentReportManager.logInfo("URL: " + config.getBaseUrl());
    }

    // ─── Scenario Teardown ─────────────────────────────────────────────────────

    @After(order = 0)
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                captureAndAttachScreenshot(scenario);
                ExtentReportManager.logFail("Scenario FAILED: " + scenario.getName());
            } else {
                ExtentReportManager.logPass("Scenario PASSED: " + scenario.getName());
            }
        } catch (Exception e) {
            log.error("Error in afterScenario hook: {}", e.getMessage());
        } finally {
            DriverManager.quitDriver();
            ExtentReportManager.removeTest();
            log.info("◀ Finished Scenario: [{}] — {}", scenario.getName(), scenario.getStatus());
            log.info("──────────────────────────────────────────────────────");
        }
    }

    // ─── Suite-level Teardown ──────────────────────────────────────────────────

    @AfterAll
    public static void globalTeardown() {
        ExtentReportManager.flush();
        log.info("═══════════════════════════════════════════════════");
        log.info("  Test Suite Completed — Reports Generated");
        log.info("═══════════════════════════════════════════════════");
    }

    // ─── Tagged Hooks ──────────────────────────────────────────────────────────

    @Before("@api")
    public void beforeApiScenario(Scenario scenario) {
        log.info("API scenario setup — no browser required for: {}", scenario.getName());
        // Skip driver init for API-only scenarios — call DriverManager.quitDriver() safely
        // The standard @Before(order=0) already runs, so we may skip init here
    }

    @Before("@smoke")
    public void beforeSmokeScenario(Scenario scenario) {
        ExtentReportManager.logInfo("⚡ SMOKE TEST");
    }

    @Before("@regression")
    public void beforeRegressionScenario(Scenario scenario) {
        ExtentReportManager.logInfo("🔁 REGRESSION TEST");
    }

    // ─── Screenshot Helper ─────────────────────────────────────────────────────

    private void captureAndAttachScreenshot(Scenario scenario) {
        try {
            if (DriverManager.isDriverActive()) {
                WebDriver driver = DriverManager.getDriver();
                byte[] screenshot = ScreenshotUtils.captureScreenshot(driver);

                if (screenshot.length > 0) {
                    // Attach to Cucumber report
                    scenario.attach(screenshot, "image/png", "Failure Screenshot");

                    // Attach to Allure report
                    Allure.addAttachment("Failure Screenshot",
                        new ByteArrayInputStream(screenshot));

                    // Attach to Extent report
                    ExtentReportManager.logFailWithScreenshot(
                        "Screenshot on failure", screenshot);

                    log.info("Screenshot captured and attached for failed scenario.");
                }
            }
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }
}
