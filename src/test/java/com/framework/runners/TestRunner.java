package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestRunner - Cucumber + TestNG test runner.
 *
 * Usage:
 *   mvn test                            → run all tests
 *   mvn test -Dcucumber.filter.tags="@smoke"   → run smoke suite
 *   mvn test -Dbrowser=firefox          → run with Firefox
 *   mvn test -Dheadless=true            → run headlessly
 *   mvn test -Denv=staging              → run against staging
 */
@CucumberOptions(
    features  = "src/test/resources/features",
    glue      = {"com.framework.hooks", "com.framework.stepdefinitions"},
    tags      = "@smoke or @regression",
    plugin    = {
        "pretty",
        "html:target/cucumber-reports/cucumber-report.html",
        "json:target/cucumber-reports/cucumber-report.json",
        "junit:target/cucumber-reports/cucumber-report.xml",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "com.framework.runners.ExtentCucumberAdapter:"
    },
    monochrome   = true,
    dryRun       = false,
    publish      = false
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Override to enable parallel scenario execution.
     * Change parallel=true and set thread count in testng.xml.
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
