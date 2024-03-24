package com.filip2801.cars.carsauctions.web

import com.filip2801.cars.carsauctions.IntegrationTestSpecification
import org.springframework.http.HttpStatus

import static com.filip2801.cars.carsauctions.TestUtils.uniqueId

class AuctionSubscriptionControllerSpec extends IntegrationTestSpecification {

    def "should register new dealer"() {
        given:
        mockDealerUser()
        def requestPayload = [
                carMakeId: uniqueId()
        ]

        when:
        var response = sendPost("auction-subscriptions", requestPayload)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id
        response.body.carMakeId == requestPayload.carMakeId
        response.body.dealerId == userLoggedIn.getId()
    }

}
