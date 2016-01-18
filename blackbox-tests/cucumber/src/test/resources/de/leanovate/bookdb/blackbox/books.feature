Feature: Manage books
  Scenario: Add book without author
    Given: A running book database
    When A book with title "Da Book" without author is created
    Then The created book does exists
    And The book has the title "Da Book"
    When The book is deleted
    Then The book does not exists