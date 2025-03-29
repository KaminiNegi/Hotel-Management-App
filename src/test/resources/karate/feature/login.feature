Feature: User Login API Test

    Background:
        * url 'http://localhost:9090/api/v1'
        * header Content-Type = 'application/json'


        Given path 'auth/authenticate'
        And request
    """
    {
      "email": "negiss@gmail.com",
      "password": "negi"
    }
    """
        When method POST
        Then status 200
        * def authToken = response.access_token
        * print 'Generated Token:', authToken


    Scenario: Get User Profile

        Given path 'users/18'
        And header Authorization = 'Bearer ' + authToken
        When method GET
        Then status 200


