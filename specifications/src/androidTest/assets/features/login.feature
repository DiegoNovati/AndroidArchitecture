Feature: Login
  We are checking the different types of login

  Scenario Outline: Login with a fake backend
    Given I have set to use the fake backend
    When I login with "username" and "password"
    Then the login is successful

    Examples:
      | username   | password        |
      | myUsername | anyPassword     |
      | myUsername | anotherPassword |