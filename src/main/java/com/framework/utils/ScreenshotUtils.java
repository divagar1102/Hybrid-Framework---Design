package com.framework.utils;

import com.framework.config.ConfigManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtils - captures full-page and element-level screenshots.
 */
public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {}

    /**
     * Captures a full-page screenshot as byte array (for Allure / Extent embedding).
     */
    public static byte[] captureScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Captures and saves screenshot to the configured directory.
     *
     * @param driver  active WebDriver
     * @param name    descriptive name (spaces replaced with underscores)
     * @return        absolute path of saved file, or empty string on failure
     */
    public static String captureAndSave(WebDriver driver, String name) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String sanitised = name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String fileName  = sanitised + "_" + timestamp + ".png";
        String dir       = ConfigManager.getInstance().getScreenshotDir();
        String filePath  = dir + File.separator + fileName;

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);
            log.info("Screenshot saved: {}", filePath);
            return filePath;
        } catch (IOException e) {
            log.error("Failed to save screenshot '{}': {}", name, e.getMessage());
            return "";
        }
    }

    /**
     * Captures a screenshot of a specific WebElement.
     */
    public static byte[] captureElement(WebElement element) {
        try {
            return element.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture element screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Saves an element-level screenshot.
     */
    public static String captureAndSaveElement(WebElement element, String name) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName  = name + "_element_" + timestamp + ".png";
        String dir       = ConfigManager.getInstance().getScreenshotDir();
        String filePath  = dir + File.separator + fileName;

        try {
            File srcFile  = element.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);
            log.info("Element screenshot saved: {}", filePath);
            return filePath;
        } catch (IOException e) {
            log.error("Failed to save element screenshot: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Creates the screenshot directory if it does not exist.
     */
    public static void ensureScreenshotDirExists() {
        String dir = ConfigManager.getInstance().getScreenshotDir();
        File directory = new File(dir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) log.info("Screenshot directory created: {}", dir);
        }
    }
}
