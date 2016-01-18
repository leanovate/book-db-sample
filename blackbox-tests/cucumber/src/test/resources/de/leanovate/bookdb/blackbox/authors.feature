Feature: Manage authors
  Scenario: Add and remove author
    Given: A running book database
    When An author with name "Test Writer" is added
    Then The created author does exists
    And The author has the name "Test Writer"
    When The author is deleted
    Then The author does not exists
