@HU-13
Feature: Comments on projects and discussions
  As a community member
  I want to comment on projects and discussions
  So that I can take part in the conversation

  Scenario: Comment on a project
    Given a signed-in member viewing a project they own
    When the member publishes a valid comment
    Then the comment is displayed with its author's identity

  Scenario: Comment on a discussion
    Given a signed-in member viewing a discussion they created
    When the member publishes a valid comment
    Then the comment is displayed with its author's identity

  Scenario: Reject an empty comment on the server
    Given a signed-in member viewing a project they own
    When the member submits an empty comment to the server
    Then the comment is rejected with a validation message

  Scenario: The comment author can delete their own comment
    Given a signed-in member viewing a project with their comment
    When the author deletes that comment and confirms
    Then the comment is no longer displayed

  @requires-admin
  Scenario: An administrator can delete another member's comment
    Given a member's project has a comment by its author
    When an administrator deletes that comment and confirms
    Then the comment is no longer displayed
