package com.framework.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * DriverManager - Thread-safe WebDriver management using ThreadLocal.
 * Ensures complete isolation between parallel test threads.
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    /**
     * Initialises a new WebDriver and binds it to the current thread.
     */
    public static void initDriver() {
        if (driverThreadLocal.get() != null) {
            log.warn("Driver already initialised for thread [{}]. Quitting existing driver.", Thread.currentThread().getName());
            quitDriver();
        }
        WebDriver driver = DriverFactory.createDriver();
        driverThreadLocal.set(driver);
        log.info("Driver initialised for thread [{}]", Thread.currentThread().getName());
    }

    /**
     * Returns the WebDriver bound to the current thread.
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                "WebDriver not initialised for thread [" + Thread.currentThread().getName() +
                "]. Call DriverManager.initDriver() before using getDriver()."
            );
        }
        return driver;
    }

    /**
     * Quits the WebDriver and removes it from the ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Driver quit for thread [{}]", Thread.currentThread().getName());
            } catch (Exception e) {
                log.error("Error while quitting driver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Returns true if a driver is active for the current thread.
     */
    public static boolean isDriverActive() {
        return driverThreadLocal.get() != null;
    }
}
