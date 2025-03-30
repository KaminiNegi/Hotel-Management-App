Feature: User Registration API Test

  Background:
    * url 'http://localhost:9090/api/v1/auth'
    * header Content-Type = 'application/json'
    * def uuid = java.util.UUID.randomUUID().toString()
    * def email = 'user_' + uuid + '@gmail.com'
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


