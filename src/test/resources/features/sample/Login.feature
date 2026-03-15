# ──────────────────────────────────────────────────────────────────────────────
# Feature: Login Functionality
# Application: SauceDemo (https://www.saucedemo.com)
# ──────────────────────────────────────────────────────────────────────────────

@smoke @regression
Feature: Login Functionality
  As a registered user
  I want to log in to the application
  So that I can access the product catalogue

  Background:
    Given the user is on the login page

  # ────────────────────────── Positive Scenarios ──────────────────────────────

  @smoke @login @positive
  Scenario: Successful login with valid credentials
    When the user enters username "standard_user"
    And the user enters password "secret_sauce"
    And the user clicks the login button
    Then the user should be redirected to the home page
    And the page title should be "Products"

  @smoke @login @positive
  Scenario Outline: Successful login with multiple valid users
    When the user enters username "<username>"
    And the user enters password "<password>"
    And the user clicks the login button
    Then the user should be redirected to the home page

    Examples:
      | username                | password     |
      | standard_user           | secret_sauce |
      | problem_user            | secret_sauce |
      | performance_glitch_user | secret_sauce |

  # ────────────────────────── Negative Scenarios ──────────────────────────────

  @regression @login @negative
  Scenario: Login fails with invalid password
    When the user enters username "standard_user"
    And the user enters password "wrong_password"
    And the user clicks the login button
    Then the user should see an error message
    And the error message should contain "Username and password do not match"

  @regression @login @negative
  Scenario: Login fails with locked out user
    When the user enters username "locked_out_user"
    And the user enters password "secret_sauce"
    And the user clicks the login button
    Then the user should see an error message
    And the error message should contain "Sorry, this user has been locked out"

  @regression @login @negative
  Scenario: Login fails with empty credentials
    When the user clicks the login button
    Then the user should see an error message
    And the error message should contain "Username is required"

  @regression @login @negative
  Scenario: Login fails with empty password
    When the user enters username "standard_user"
    And the user clicks the login button
    Then the user should see an error message
    And the error message should contain "Password is required"

  # ────────────────────────── Logout Scenario ─────────────────────────────────

  @smoke @logout
  Scenario: User can successfully log out
    Given the user is logged in as "standard_user" with password "secret_sauce"
    When the user clicks the burger menu
    And the user clicks the logout button
    Then the user should be on the login page
