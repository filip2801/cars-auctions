package com.filip2801.cars.carsauctions.web

import com.filip2801.cars.carsauctions.IntegrationTestSpecification
import org.springframework.http.HttpStatus

import static com.filip2801.cars.carsauctions.TestUtils.uniqueString

class DealersControllerITSpec extends IntegrationTestSpecification {

    def "should register new dealer"() {
        given:
        mockAgentUser()
        def requestPayload = [
                username: uniqueString(),
                password: uniqueString()
        ]

        when:
        var response = sendPost("dealers", requestPayload)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id
        response.body.username == requestPayload.username
        !response.body.password
    }

    def "should not allow dealer to register another dealer"() {
        given:
        mockDealerUser()
        def requestPayload = [
                username: uniqueString(),
                password: uniqueString()
        ]

        when:
        sendPost("dealers", requestPayload)

        then:
        thrown status403()
    }
}
