package com.filip2801.cars.carsauctions.web

import com.filip2801.cars.carsauctions.IntegrationTestSpecification
import org.springframework.http.HttpStatus

import java.time.Duration
import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.TestUtils.uniqueId

class AuctionControllerITSpec extends IntegrationTestSpecification {

    def "should register new auction"() {
        given:
        mockAgentUser()
        var bookedAppointment = bookAppointment()
        finaliseInspection(bookedAppointment.appointmentId)

        def carId = bookedAppointment.car.id

        def requestPayload = [
                carId               : carId,
                customerEmailAddress: 'email@test.com',
                anchorBid           : 100
        ]

        when:
        var response = sendPost("auctions", requestPayload)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id
        response.body.carId == requestPayload.carId

        dateCloseTo(parseDate(response.body.startTime), LocalDateTime.now(), 1000)
        parseDate(response.body.expectedEndTime) == parseDate(response.body.startTime).plusMinutes(24)

        response.body.anchorBid == requestPayload.anchorBid
        response.body.highestBid == null
        response.body.leadingDealerId == null
        response.body.status == 'RUNNING'
    }

    def "should not register new auction when inspection is not finished"() {
        given:
        mockAgentUser()
        var bookedAppointment = bookAppointment()
        def carId = bookedAppointment.car.id

        def requestPayload = [
                carId               : carId,
                customerEmailAddress: 'email@test.com',
                anchorBid           : 100
        ]

        when:
        sendPost("auctions", requestPayload)

        then:
        thrown status400()
    }

    def "should not register new auction when car inspection does not exist"() {
        given:
        mockAgentUser()
        def requestPayload = [
                carId               : uniqueId(),
                customerEmailAddress: 'email@test.com',
                anchorBid           : 100
        ]

        when:
        sendPost("auctions", requestPayload)

        then:
        thrown status400()
    }

    def bookAppointment() {
        def bookInspectionAppointmentRequest = someAppointmentRequestPayload()
        var bookedInspectionAppointmentResponse = sendPost("inspection-appointments/booking", bookInspectionAppointmentRequest)
        return bookedInspectionAppointmentResponse.body
    }

    def finaliseInspection(Long appointmentId) {
        sendPut("inspection-appointments/$appointmentId/status", ['status': 'INSPECTION_SUCCESSFUL'])
    }

    private LinkedHashMap<String, Serializable> someAppointmentRequestPayload() {
        return [
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
    }

    LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date);
    }

    void dateCloseTo(LocalDateTime date, LocalDateTime closeTo, int maxDifferenceMillis) {
        var duration = Duration.between(date, closeTo)
        assert duration.compareTo(Duration.ofMillis(maxDifferenceMillis)) < 0
    }
}
