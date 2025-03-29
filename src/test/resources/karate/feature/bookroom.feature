Feature: User Login API Test

    Background:
        * url 'http://localhost:9090/api/v1'
        * header Content-Type = 'application/json'
        * def randomRoomId = Math.floor(Math.random() * 12) + 1


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


    Scenario: Book Room

        Given path 'rooms/book'
        And header Authorization = 'Bearer ' + authToken
        And request
    """
    {
    "roomId": "#(randomRoomId)"
    }
    """
        When method POST
        Then status 200

    Scenario: Unbook Room

        Given path 'rooms/unbook'
        And header Authorization = 'Bearer ' + authToken
        And request
    """
    {
    "roomId": "#(randomRoomId)"
    }
    """
        When method POST
        Then status 200
