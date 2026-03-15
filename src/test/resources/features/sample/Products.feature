@regression
Feature: Product Catalogue
  As a logged-in user
  I want to browse and interact with the product catalogue
  So that I can add items to my cart

  Background:
    Given the user is logged in as "standard_user" with password "secret_sauce"

  @smoke @products
  Scenario: Verify product catalogue is displayed
    Then the home page should display products
    And the product count should be greater than 0

  @regression @products
  Scenario: Sort products by name ascending
    When the user sorts products by "Name (A to Z)"
    Then the products should be displayed in ascending name order

  @regression @products
  Scenario: Add product to cart
    When the user adds the first product to the cart
    Then the cart badge should show "1"

  @regression @products
  Scenario: Verify all product names are visible
    Then all product names should be visible on the page
