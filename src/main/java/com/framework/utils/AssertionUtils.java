package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;

import java.util.List;

/**
 * AssertionUtils - wraps TestNG Assert and AssertJ soft assertions
 * with descriptive logging on pass and fail.
 */
public class AssertionUtils {

    private static final Logger log = LogManager.getLogger(AssertionUtils.class);

    private AssertionUtils() {}

    // ─── Hard Assertions (TestNG) ──────────────────────────────────────────────

    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            log.info("✅ PASS | {} | Expected: [{}] | Actual: [{}]", message, expected, actual);
        } catch (AssertionError e) {
            log.error("❌ FAIL | {} | Expected: [{}] | Actual: [{}]", message, expected, actual);
            throw e;
        }
    }

    public static void assertTrue(boolean condition, String message) {
        try {
            Assert.assertTrue(condition, message);
            log.info("✅ PASS | {}", message);
        } catch (AssertionError e) {
            log.error("❌ FAIL | {}", message);
            throw e;
        }
    }

    public static void assertFalse(boolean condition, String message) {
        try {
            Assert.assertFalse(condition, message);
            log.info("✅ PASS | {}", message);
        } catch (AssertionError e) {
            log.error("❌ FAIL | {}", message);
            throw e;
        }
    }

    public static void assertNotNull(Object object, String message) {
        try {
            Assert.assertNotNull(object, message);
            log.info("✅ PASS | {} is not null", message);
        } catch (AssertionError e) {
            log.error("❌ FAIL | {} is null", message);
            throw e;
        }
    }

    public static void assertNull(Object object, String message) {
        try {
            Assert.assertNull(object, message);
            log.info("✅ PASS | {} is null", message);
        } catch (AssertionError e) {
            log.error("❌ FAIL | {} is not null", message);
            throw e;
        }
    }

    public static void assertContains(String actual, String expected, String message) {
        try {
            Assert.assertTrue(actual.contains(expected),
                message + " | Expected to contain: [" + expected + "] | Actual: [" + actual + "]");
            log.info("✅ PASS | {} | '{}' contains '{}'", message, actual, expected);
        } catch (AssertionError e) {
            log.error("❌ FAIL | {} | '{}' does not contain '{}'", message, actual, expected);
            throw e;
        }
    }

    public static void assertListContains(List<?> list, Object item, String message) {
        assertTrue(list.contains(item), message + " | List does not contain: " + item);
    }

    public static void assertListSize(List<?> list, int expectedSize, String message) {
        assertEquals(list.size(), expectedSize, message + " | List size mismatch");
    }

    // ─── Soft Assertions (AssertJ) ─────────────────────────────────────────────

    /**
     * Usage:
     *   SoftAssertions soft = AssertionUtils.softAssertions();
     *   soft.assertThat(actual).isEqualTo(expected);
     *   soft.assertAll();
     */
    public static SoftAssertions softAssertions() {
        return new SoftAssertions();
    }

    // ─── Custom Verifications ──────────────────────────────────────────────────

    public static void verifyPageTitle(String actualTitle, String expectedTitle) {
        assertContains(actualTitle.toLowerCase(), expectedTitle.toLowerCase(),
            "Page title verification");
    }

    public static void verifyUrl(String actualUrl, String expectedUrlFragment) {
        assertContains(actualUrl, expectedUrlFragment, "URL verification");
    }

    public static void verifyElementText(String actual, String expected) {
        assertEquals(actual.trim(), expected.trim(), "Element text verification");
    }

    public static void verifyElementVisible(boolean isDisplayed, String elementName) {
        assertTrue(isDisplayed, elementName + " should be visible");
    }
}
