package com.filip2801.cars.carsauctions.web

import com.filip2801.cars.carsauctions.IntegrationTestSpecification
import org.springframework.http.HttpStatus

import static com.filip2801.cars.carsauctions.TestUtils.uniqueId

class AppointmentsControllerITSpec extends IntegrationTestSpecification {

    def "should book appointment"() {
        given:
        def requestPayload = [
                locationId          : uniqueId(),
                time                : '2024-05-15T14:30:00',
                customerEmailAddress: 'test@customer.com',
                car                 : [
                        makeId           : uniqueId(),
                        modelId          : uniqueId(),
                        variantId        : uniqueId(),
                        manufacturingYear: 2015,
                        registrationYear : 2016
                ]
        ]

        when:
        var creationResponse = sendPost("appointments", requestPayload)

        then:
        creationResponse.statusCode == HttpStatus.OK
        creationResponse.body.appointmentId
        creationResponse.body.locationId == requestPayload.locationId
        creationResponse.body.time == requestPayload.time
        creationResponse.body.customerEmailAddress == requestPayload.customerEmailAddress
        creationResponse.body.status == 'BOOKED'
        creationResponse.body.car.id
        creationResponse.body.car.makeId == requestPayload.car.makeId
        creationResponse.body.car.modelId == requestPayload.car.modelId
        creationResponse.body.car.variantId == requestPayload.car.variantId
        creationResponse.body.car.manufacturingYear == requestPayload.car.manufacturingYear
        creationResponse.body.car.registrationYear == requestPayload.car.registrationYear
    }

}