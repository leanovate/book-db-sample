Feature: Manage genres
  Scenario: Add and remove genre
    Given: A running book database
    When A genre "test_genre" is added
    Then The genre "test_genre" exists
    When The genre "test_genre" is deleted
    Then The genre "test_genre" does not exists
