Feature: User Registration API Test

  Background:
    * url 'http://localhost:9090/api/v1/auth'
    * header Content-Type = 'application/json'
    * def email = 'user' + Math.floor(Math.random() * 10000) + '@gmail.com'
    * def password = 'Test@1234'

  Scenario: Register a New User with random email
    Given path 'register'
    And request
    """
    {
      "firstname": "John",
      "lastname": "Doe",
      "email": "#(email)",
      "password": "#(password)"
    }
    """
    When method POST
    Then status 200
    * print 'User registered with email:', email


  Scenario: Login Registered user
    * def retryCondition = function(response) { return responseStatus == 200; }

    Given path 'authenticate'
    And request { "email": "#(email)", "password": "#(password)" }
    When method POST
    Then retry until retryCondition
    * def authToken = response.access_token
    * print 'Successfully logged in with token:', authToken
