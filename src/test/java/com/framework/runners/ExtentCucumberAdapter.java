package com.framework.runners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.framework.utils.ExtentReportManager;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ExtentCucumberAdapter - Cucumber EventListener that logs step results to Extent Reports.
 * Referenced in CucumberOptions plugin as "com.framework.runners.ExtentCucumberAdapter:"
 */
public class ExtentCucumberAdapter implements ConcurrentEventListener {

    private static final Logger log = LogManager.getLogger(ExtentCucumberAdapter.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::handleStepFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleCaseFinished);
    }

    private void handleCaseStarted(TestCaseStarted event) {
        // Test node already created in Hooks.java — nothing extra needed here
        log.debug("Test case started: {}", event.getTestCase().getName());
    }

    private void handleStepFinished(TestStepFinished event) {
        if (!(event.getTestStep() instanceof PickleStepTestStep)) return;

        PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
        String stepText = step.getStep().getText();
        Status status;

        switch (event.getResult().getStatus()) {
            case PASSED   -> status = Status.PASS;
            case FAILED   -> status = Status.FAIL;
            case SKIPPED  -> status = Status.SKIP;
            case PENDING  -> status = Status.WARNING;
            default       -> status = Status.INFO;
        }

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(status, stepText);

            if (event.getResult().getError() != null) {
                test.log(Status.FAIL, event.getResult().getError().getMessage());
            }
        }
    }

    private void handleCaseFinished(TestCaseFinished event) {
        log.debug("Test case finished: {} — {}", event.getTestCase().getName(),
            event.getResult().getStatus());
    }
}
