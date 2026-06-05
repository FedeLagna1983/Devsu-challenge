Feature: Complete purchase flow


  @checkout @smoke @regression
  Scenario: Complete purchase flow with two products until order confirmation
    Given the user opens the SauceDemo login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    And the user adds two products to the cart from the Inventory
    And the user opens the cart from the Inventory
    When the user proceeds to checkout
    And the user fills the checkout form with first name "John", last name "Doe" and zip code "12345"
    And the user finishes the purchase
    Then the order confirmation "Thank you for your order!" should be displayed
