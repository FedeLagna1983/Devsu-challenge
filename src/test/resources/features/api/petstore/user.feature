Feature: PetStore User API


  @api @smoke @regression
  Scenario: Complete user lifecycle - create, find, update and delete
    * def username = 'devsu_' + java.util.UUID.randomUUID().toString().substring(0, 8)
    * def userPayload = { id: 1001, username: '#(username)', firstName: 'John', lastName: 'Doe', email: 'john.doe@devsu.com', password: 'pass123', phone: '1234567890', userStatus: 1 }
    * def updatedPayload = { id: 1001, username: '#(username)', firstName: 'Jane', lastName: 'Doe', email: 'jane.updated@devsu.com', password: 'pass123', phone: '1234567890', userStatus: 1 }

    # Step 1: Create user
    Given url petStoreBaseUrl + '/user'
    And request userPayload
    When method POST
    Then status 200
    And match response.code == 200
    And match response.message == '1001'

    # Step 2: Find the created user
    Given url petStoreBaseUrl + '/user/' + username
    When method GET
    Then status 200
    And match response.username == username
    And match response.firstName == 'John'
    And match response.email == 'john.doe@devsu.com'

    # Step 3: Update user name and email
    Given url petStoreBaseUrl + '/user/' + username
    And request updatedPayload
    When method PUT
    Then status 200
    And match response.code == 200

    # Step 4: Find the updated user
    Given url petStoreBaseUrl + '/user/' + username
    When method GET
    Then status 200
    And match response.username == username
    And match response.firstName == 'Jane'
    And match response.email == 'jane.updated@devsu.com'

    # Step 5: Delete the user
    Given url petStoreBaseUrl + '/user/' + username
    When method DELETE
    Then status 200
    And match response.code == 200
    And match response.message == username
