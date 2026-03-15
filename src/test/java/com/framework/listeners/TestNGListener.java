package com.framework.listeners;

import com.framework.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

/**
 * TestNGListener - ITestListener implementation for TestNG lifecycle events.
 * Bridges TestNG events to Extent Reports logging.
 */
public class TestNGListener implements ITestListener, ISuiteListener {

    private static final Logger log = LogManager.getLogger(TestNGListener.class);

    // ─── Suite ─────────────────────────────────────────────────────────────────

    @Override
    public void onStart(ISuite suite) {
        log.info("╔══════════════════════════════════════╗");
        log.info("  Suite Started: {}", suite.getName());
        log.info("╚══════════════════════════════════════╝");
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentReportManager.flush();
        log.info("╔══════════════════════════════════════╗");
        log.info("  Suite Finished: {}", suite.getName());
        log.info("╚══════════════════════════════════════╝");
    }

    // ─── Test ──────────────────────────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶ Test Started: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ Test PASSED: {} ({}ms)", result.getName(), getExecutionTime(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("❌ Test FAILED: {} ({}ms)", result.getName(), getExecutionTime(result));
        if (result.getThrowable() != null) {
            log.error("   Cause: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⏭ Test SKIPPED: {}", result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("⚠ Test FAILED WITHIN SUCCESS %: {}", result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("── Test Context Started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("── Test Context Finished: {} | Passed: {} | Failed: {} | Skipped: {}",
            context.getName(),
            context.getPassedTests().size(),
            context.getFailedTests().size(),
            context.getSkippedTests().size());
    }

    private long getExecutionTime(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
