@HU-02
Feature: User sign-in
  As a registered member
  I want to sign in
  So that I can access my community feed

  Scenario: Sign in with valid credentials
    Given a member account already exists
    When the member signs in with valid credentials
    Then the member's signed-in session is visible
    And the projects feed is displayed

  Scenario: Use the same generic error for an incorrect password and unknown email
    Given a member account already exists
    When the member signs in with an incorrect password
    And a visitor signs in with a nonexistent email
    Then both attempts show the same generic credentials error

  @requires-admin
  Scenario: Record each failed sign-in attempt
    Given a failed sign-in attempt has a trace reference
    When an administrator opens the audit page
    Then the failed attempt is present in the audit records
