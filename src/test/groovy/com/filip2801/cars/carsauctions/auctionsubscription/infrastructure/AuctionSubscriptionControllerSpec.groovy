package com.filip2801.cars.carsauctions.auctionsubscription.infrastructure

import com.filip2801.cars.carsauctions.testutils.ControllerIntegrationTestSpecification
import org.springframework.http.HttpStatus

import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

class AuctionSubscriptionControllerSpec extends ControllerIntegrationTestSpecification {

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
