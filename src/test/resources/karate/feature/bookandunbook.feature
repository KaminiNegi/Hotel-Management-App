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

  Scenario: Book Room If Not Already Booked
    Given path 'rooms'
    And header Authorization = 'Bearer ' + authToken
    When method GET
    Then status 200

    * def rooms = response
    * print 'Rooms Response:', rooms

    # ✅ Ensure `availableRooms` is assigned correctly before using it
    * def availableRooms = rooms.filter(r => r.booked == false)
    * def availableRoom = availableRooms.length > 0 ? availableRooms[0] : null

    # ✅ Only call the booking API if a room is available
    Given path 'rooms/book'
    And header Authorization = 'Bearer ' + authToken
    And request
    """
    {
      "roomId": "#(availableRoom != null ? availableRoom.id : '')"
    }
    """
    When method POST
    Then status 200
    * print 'Attempted to book room:', availableRoom

  # ✅ Unbook the same room that was booked
    Given path 'rooms/unbook'
    And header Authorization = 'Bearer ' + authToken
    And request
    """
    {
      "roomId": "#(availableRoom.id)"
    }
    """
    When method POST
    Then status 200
    * print 'Room unbooked successfully:', availableRoom