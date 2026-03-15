package com.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * ParallelRunner - enables parallel Cucumber scenario execution via TestNG.
 *
 * Usage: mvn test -Pparallel
 * Thread count is controlled via testng-parallel.xml
 */
@CucumberOptions(
    features  = "src/test/resources/features",
    glue      = {"com.framework.hooks", "com.framework.stepdefinitions"},
    tags      = "@smoke or @regression",
    plugin    = {
        "pretty",
        "html:target/cucumber-reports/parallel-report.html",
        "json:target/cucumber-reports/parallel-report.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true
)
public class ParallelRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
