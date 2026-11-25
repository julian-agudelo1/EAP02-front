@HU-14
Feature: Reactions to community content
  As a community member
  I want to like a publication
  So that I can show appreciation for it

  Scenario: Add and remove a reaction
    Given a signed-in member viewing a project they own
    When the member adds a like
    Then the reaction count increases by one
    When the member removes the like
    Then the reaction count returns to its original value

  Scenario: Do not count duplicate reactions from the same member
    Given a signed-in member viewing a project they own
    When the member adds the same like twice
    Then only one reaction from that member is counted
