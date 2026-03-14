Feature: Horizontal Slider

  Scenario: Move the slider right and left
    Given I am on "https://the-internet.herokuapp.com/horizontal_slider"
    When I focus on the slider
    And I move the slider right
    And I move the slider left
    Then I should see the slider value updated on the right
