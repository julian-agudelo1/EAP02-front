@HU-01
Feature: Account registration
  As a visitor
  I want to create an account with a unique email and a compliant password
  So that I can join the community

  Background:
    Given the visitor opens the registration page

  Scenario: Register with valid details
    When the visitor registers with a unique email and a strong password
    Then the account-created confirmation is shown
    And the visitor is asked to sign in

  Scenario: Reject an email that is already registered
    Given a member account already exists
    When the visitor registers again with the same email
    Then the registration is rejected with a clear message

  Scenario: Reject an invalid email on the server
    When the visitor submits an invalid email to the server
    Then the server rejects the registration

  Scenario: Explain password rules that are not satisfied
    When the visitor submits a password that violates the password policy
    Then the error explains the password requirements
