# 🚀 Hybrid Automation Framework

**Java · Selenium · Cucumber · TestNG · ExtentReports · Allure · Log4j2**

A production-ready, enterprise-grade test automation framework with a full hybrid architecture supporting BDD, parallel execution, multi-browser, multi-environment, data-driven testing, and dual reporting.

---

## 📋 Table of Contents
- [Tech Stack](#-tech-stack)
- [Framework Architecture](#-framework-architecture)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [IntelliJ Setup](#-intellij-setup)
- [Running Tests](#-running-tests)
- [Configuration](#-configuration)
- [Writing Tests](#-writing-tests)
- [Reporting](#-reporting)
- [Utilities Reference](#-utilities-reference)

---

## 🛠 Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 17 | Core language |
| Maven | 3.9+ | Build & dependency management |
| Selenium WebDriver | 4.18.1 | Browser automation |
| Cucumber | 7.15.0 | BDD framework |
| TestNG | 7.9.0 | Test runner & parallel execution |
| WebDriverManager | 5.7.0 | Automatic browser driver management |
| ExtentReports | 5.1.1 | Interactive HTML reports |
| Allure | 2.25.0 | Advanced reporting with history |
| Log4j2 | 2.22.1 | Structured logging |
| JavaFaker | 1.0.2 | Realistic test data generation |
| Apache POI | 5.2.5 | Excel test data |
| Jackson | 2.16.1 | JSON test data |
| RestAssured | 5.4.0 | API testing |
| AssertJ | 3.25.1 | Fluent assertions |
| Lombok | 1.18.30 | Boilerplate reduction |

---

## 🏗 Framework Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    HYBRID AUTOMATION FRAMEWORK                       │
├─────────────────────────────────────────────────────────────────────┤
│  FEATURE FILES (.feature)  →  BDD scenarios in Gherkin               │
│  STEP DEFINITIONS          →  Gherkin-to-Java bindings               │
│  PAGE OBJECTS              →  BasePage + Page-specific classes       │
│  HOOKS                     →  @Before / @After lifecycle management  │
│  DRIVER LAYER              →  ThreadLocal WebDriver (parallel safe)  │
│  UTILITIES                 →  Wait, Screenshot, Excel, JSON, API...  │
│  CONFIG                    →  Multi-env properties + system override  │
│  REPORTING                 →  Extent HTML + Allure + Cucumber JSON   │
│  LOGGING                   →  Log4j2 (console + rolling file)        │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
hybrid-automation-framework/
├── pom.xml                                    # Maven dependencies & plugins
├── testng.xml                                 # Main TestNG suite
├── testng-parallel.xml                        # Parallel execution suite
│
├── src/
│   ├── main/java/com/framework/
│   │   ├── config/
│   │   │   └── ConfigManager.java             # Singleton config reader (multi-env)
│   │   ├── driver/
│   │   │   ├── DriverFactory.java             # Browser driver creation (Chrome/FF/Edge/Safari/Grid)
│   │   │   └── DriverManager.java             # ThreadLocal driver management
│   │   ├── pages/
│   │   │   ├── BasePage.java                  # Abstract parent page (all Selenium interactions)
│   │   │   └── sample/
│   │   │       ├── LoginPage.java             # Sample login page object
│   │   │       └── HomePage.java              # Sample home/product page object
│   │   └── utils/
│   │       ├── WaitUtils.java                 # All explicit & fluent wait strategies
│   │       ├── ScreenshotUtils.java           # Full-page & element screenshots
│   │       ├── ExcelUtils.java                # Apache POI Excel reader/writer
│   │       ├── JsonUtils.java                 # Jackson JSON reader/writer
│   │       ├── RandomDataUtils.java           # JavaFaker test data generator
│   │       ├── ExtentReportManager.java       # ExtentReports singleton manager
│   │       ├── AssertionUtils.java            # Logged hard + soft assertions
│   │       ├── ApiUtils.java                  # RestAssured API helpers
│   │       └── DateUtils.java                 # Date formatting & manipulation
│   │
│   └── test/
│       ├── java/com/framework/
│       │   ├── hooks/
│       │   │   └── Hooks.java                 # Cucumber Before/After hooks
│       │   ├── runners/
│       │   │   ├── TestRunner.java            # Main Cucumber+TestNG runner
│       │   │   ├── ParallelRunner.java        # Parallel execution runner
│       │   │   └── ExtentCucumberAdapter.java # Bridges Cucumber steps → Extent
│       │   ├── listeners/
│       │   │   └── TestNGListener.java        # TestNG lifecycle listener
│       │   └── stepdefinitions/sample/
│       │       ├── LoginSteps.java            # Login feature step defs
│       │       └── ProductSteps.java          # Products feature step defs
│       │
│       └── resources/
│           ├── config/
│           │   ├── config.properties          # Base/default configuration
│           │   ├── config-dev.properties      # Dev environment overrides
│           │   └── config-staging.properties  # Staging environment overrides
│           ├── features/sample/
│           │   ├── Login.feature              # Login BDD scenarios
│           │   └── Products.feature           # Product catalogue scenarios
│           ├── testdata/
│           │   └── testdata.json              # JSON test data
│           ├── allure.properties              # Allure config
│           ├── cucumber.properties            # Cucumber config
│           └── log4j2.xml                     # Logging configuration
```

---

## 📦 Prerequisites

| Requirement | Minimum Version |
|-------------|----------------|
| JDK | 17+ |
| Maven | 3.8+ |
| Chrome/Firefox/Edge | Latest |
| IntelliJ IDEA | 2023+ (Community or Ultimate) |

---

## 🖥 IntelliJ Setup

1. **Open the project**
   - File → Open → select the `hybrid-automation-framework` folder
   - IntelliJ will detect it as a Maven project

2. **Install required plugins** (File → Settings → Plugins):
   - ✅ Cucumber for Java
   - ✅ Gherkin
   - ✅ Lombok
   - ✅ TestNG (usually pre-installed)

3. **Enable annotation processing** (for Lombok):
   - File → Settings → Build → Compiler → Annotation Processors
   - ✅ Enable annotation processing

4. **Reload Maven**:
   - Right-click `pom.xml` → Maven → Reload project
   - Or click the Maven tool window → Reload All Maven Projects

5. **Set Java SDK**:
   - File → Project Structure → Project SDK → Java 17+

---

## ▶ Running Tests

### From IntelliJ
- Right-click `testng.xml` → Run
- Right-click any `.feature` file → Run Feature
- Right-click `TestRunner` class → Run

### From Terminal (Maven)

```bash
# Run all tests
mvn test

# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Run regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Run with Firefox
mvn test -Dbrowser=firefox

# Run headlessly
mvn test -Dheadless=true

# Run against staging environment
mvn test -Denv=staging

# Run in parallel (4 threads)
mvn test -Pparallel

# Combined: Firefox + headless + staging
mvn test -Dbrowser=firefox -Dheadless=true -Denv=staging

# Generate Allure report
mvn allure:report
```

---

## ⚙ Configuration

All settings live in `src/test/resources/config/config.properties`.

| Property | Default | Description |
|----------|---------|-------------|
| `browser` | `chrome` | chrome / firefox / edge / safari |
| `headless` | `false` | Run without UI |
| `base.url` | saucedemo.com | Application URL |
| `implicit.wait` | `10` | Implicit wait (seconds) |
| `explicit.wait` | `20` | Explicit wait timeout |
| `page.load.timeout` | `30` | Page load timeout |
| `remote.execution` | `false` | Use Selenium Grid |
| `remote.url` | localhost:4444 | Grid hub URL |
| `api.base.url` | `` | Base URL for REST API tests |

**Priority order**: System properties > env-specific file > base config

---

## ✍ Writing Tests

### 1. Create a Feature File
```gherkin
@smoke
Feature: My Feature
  Scenario: My Test
    Given I am on the home page
    When I do something
    Then I should see something
```

### 2. Create a Page Object
```java
public class MyPage extends BasePage {
    // Use @FindBy or By locators
    // All Selenium interactions inherited from BasePage
    public void clickSomething() { click(By.id("myButton")); }
    public String getSomethingText() { return getText(By.id("myText")); }
}
```

### 3. Create Step Definitions
```java
public class MySteps {
    private MyPage myPage = new MyPage();

    @Given("I am on the home page")
    public void iAmOnTheHomePage() { myPage.navigateToBaseUrl(); }

    @Then("I should see something")
    public void iShouldSeeSomething() {
        AssertionUtils.assertTrue(myPage.isElementPresent(By.id("x")), "Element visible");
    }
}
```

---

## 📊 Reporting

After each run, find reports in:

| Report | Location |
|--------|----------|
| Extent HTML | `target/reports/ExtentReport_<timestamp>.html` |
| Allure JSON | `target/allure-results/` |
| Allure HTML | `mvn allure:report` → `target/site/allure-maven-plugin/` |
| Cucumber HTML | `target/cucumber-reports/cucumber-report.html` |
| Cucumber JSON | `target/cucumber-reports/cucumber-report.json` |
| Screenshots | `target/screenshots/` |
| Logs | `target/logs/framework.log` |

---

## 🔧 Utilities Reference

### WaitUtils
```java
wait.waitForVisibility(By.id("element"));
wait.waitForElementToBeClickable(By.id("btn"));
wait.waitForUrlContains("/dashboard");
wait.fluentWaitForElement(locator, 30, 500);
```

### RandomDataUtils
```java
RandomDataUtils.getEmail();           // fake.user@domain.com
RandomDataUtils.getFullName();        // John Smith
RandomDataUtils.getPassword();        // Xk9#mP2@qR
RandomDataUtils.getPhoneNumber();     // 555-867-5309
RandomDataUtils.getRandomInt(1, 100); // 42
```

### ExcelUtils
```java
ExcelUtils excel = new ExcelUtils("src/test/resources/testdata/TestData.xlsx");
List<Map<String,String>> data = excel.setSheet("Login").getDataAsListOfMaps();
```

### JsonUtils
```java
Map<String,Object> data = JsonUtils.readFromClasspathAsMap("testdata/testdata.json");
String username = JsonUtils.getJsonValue("testdata.json", "username");
```

### ApiUtils
```java
Response response = ApiUtils.get("/users/1");
ApiUtils.validateStatusCode(response, 200);
String name = ApiUtils.extractValue(response, "data.first_name");
```

### AssertionUtils
```java
AssertionUtils.assertEquals(actual, expected, "Values should match");
AssertionUtils.assertContains(text, "expected", "Should contain text");
AssertionUtils.assertTrue(condition, "Should be true");

// Soft assertions
SoftAssertions soft = AssertionUtils.softAssertions();
soft.assertThat(actual).isEqualTo(expected);
soft.assertAll();
```

---

## 🌐 Parallel Execution

The framework uses `ThreadLocal<WebDriver>` — each thread gets its own isolated browser session. To run in parallel:

```bash
mvn test -Pparallel
```

Thread count is set in `testng-parallel.xml` (`thread-count="4"`).

---

## 📞 Support

For issues, add new step definitions in `stepdefinitions/`, new pages in `pages/`, and new features in `features/`. The framework is designed to scale with zero coupling between components.
