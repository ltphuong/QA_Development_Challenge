Feature: Call current weather data for one location
  As a user, I want to call current weather data for one location

  @UI @API
  Scenario Outline: TC-01 View Weather Information in Search Weather Result Page
    Given I am on Home page
    When I search "<city>" city from header
    And I send GET request to search "<city>" city with "<units>" units
    Then I can see the "<city>" city matching with API response
    And I can see the weather description matching with API response
    And I can see the weather information matching with API response with "<pattern>" pattern
    And I can see the geographical coordinate matching with API response

    Examples:
      | city        | units   | pattern                                                               |
      | Ben Tre, VN | metric  | %s°С temperature from %s to %s °С, wind %s m/s. clouds %s %%, %s hpa  |