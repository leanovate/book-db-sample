Feature: Author
  Scenario: Add and remove author
    Given: Running book database
    When An author with name "Test Writer" is added
    Then Created author can be opened
    And The auther has the name "Test Writer"
    When The author is deleted
    Then The author does not exists
