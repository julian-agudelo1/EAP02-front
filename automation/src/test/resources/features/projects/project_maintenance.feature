@HU-08 @pending-ui
Feature: Project maintenance
  As a project author
  I want to edit or delete my projects
  So that my profile stays up to date

  Scenario: The author edits a project
    Given a project owned by the signed-in member
    When the author changes the project details
    Then the updated details are displayed

  Scenario: A non-author cannot edit or delete a project
    Given a project owned by another member
    When a different member attempts to edit or delete the project
    Then the project remains unchanged

  Scenario: The author confirms project deletion
    Given a project owned by the signed-in member
    When the author requests to delete the project
    Then deletion requires confirmation
    And the project is removed only after confirmation
