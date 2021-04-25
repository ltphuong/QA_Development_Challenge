Feature: One Call API - Current and forecast weather data
  As a user, I want to get current and forecast weather data for a specific location

  @UI @API
  Scenario Outline: TC-01 View 8-Day Weather Forecast
    Given I am on Home page
    When I search "<city>" city from header
    And I click on the city hyperlink
    And I send GET request to 8-Day Weather Forecast with "<city>" city and "<units>" units
    Then I can see 8-Day Weather Forecast matching with API response

    Examples:
      | city        | units   |
      | Ben Tre, VN | metric  |