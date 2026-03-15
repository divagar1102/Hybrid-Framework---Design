package com.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager - Thread-safe ExtentReports singleton.
 * Generates an interactive HTML report after each test run.
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            extentReports = createInstance();
        }
        return extentReports;
    }

    private static ExtentReports createInstance() {
        ConfigManager config = ConfigManager.getInstance();
        String reportDir  = config.getReportDir();
        String timestamp  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportPath = reportDir + File.separator + "ExtentReport_" + timestamp + ".html";

        new File(reportDir).mkdirs();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Hybrid Automation Framework — Test Results");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        sparkReporter.config().setEncoding("UTF-8");

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(sparkReporter);
        reports.setSystemInfo("OS",          System.getProperty("os.name"));
        reports.setSystemInfo("Java",        System.getProperty("java.version"));
        reports.setSystemInfo("Browser",     config.getBrowser());
        reports.setSystemInfo("Environment", config.get("env", "default"));
        reports.setSystemInfo("Base URL",    config.getBaseUrl());
        reports.setSystemInfo("Tester",      System.getProperty("user.name"));

        log.info("Extent Report will be generated at: {}", reportPath);
        return reports;
    }

    // ─── Test lifecycle ────────────────────────────────────────────────────────

    public static ExtentTest createTest(String testName) {
        ExtentTest test = getExtentReports().createTest(testName);
        testThreadLocal.set(test);
        return test;
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getExtentReports().createTest(testName, description);
        testThreadLocal.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void removeTest() {
        testThreadLocal.remove();
    }

    // ─── Logging ───────────────────────────────────────────────────────────────

    public static void logInfo(String message) {
        getTest().log(Status.INFO, message);
    }

    public static void logPass(String message) {
        getTest().log(Status.PASS, message);
    }

    public static void logFail(String message) {
        getTest().log(Status.FAIL, message);
    }

    public static void logSkip(String message) {
        getTest().log(Status.SKIP, message);
    }

    public static void logWarning(String message) {
        getTest().log(Status.WARNING, message);
    }

    public static void logPassWithScreenshot(String message, byte[] screenshot) {
        getTest().pass(message, MediaEntityBuilder.createScreenCaptureFromBase64String(
                java.util.Base64.getEncoder().encodeToString(screenshot)).build());
    }

    public static void logFailWithScreenshot(String message, byte[] screenshot) {
        getTest().fail(message, MediaEntityBuilder.createScreenCaptureFromBase64String(
                java.util.Base64.getEncoder().encodeToString(screenshot)).build());
    }

    public static void addScreenshot(byte[] screenshot) {
        getTest().addScreenCaptureFromBase64String(
                java.util.Base64.getEncoder().encodeToString(screenshot));
    }

    // ─── Flush ─────────────────────────────────────────────────────────────────

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("Extent Reports flushed.");
        }
    }
}
