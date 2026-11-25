@HU-07
Feature: Project publication
  As a developer
  I want to publish a project with its description and technology stack
  So that I can showcase my work

  Scenario: Publish a project under the signed-in member's profile
    Given a signed-in member
    When the member publishes a project with a title, description, and technology
    Then the project appears in the member's project list
    And the project is attributed to the signed-in member

  Scenario Outline: Reject a project with a missing required field on the server
    Given a signed-in member
    When the member submits a project with a missing "<field>" to the server
    Then the project is rejected with a validation message

    Examples:
      | field       |
      | title       |
      | description |
