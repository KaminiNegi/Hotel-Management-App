Feature: Get User Profile with Token

  Background:

    * url 'http://localhost:9090/api/v1/users'
    * header Authorization = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZWdpc3NAZ21haWwuY29tIiwiaWF0IjoxNzQyOTEwNDA5LCJleHAiOjE3NDI5OTY4MDl9.fjYDaUQIbY_m9P3ZPPAbAqPILhifzZ0gy69pnFKR10U'

  Scenario: Get User Profile
    Given path '8'
    When method GET
    Then status 200

