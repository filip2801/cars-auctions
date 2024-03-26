package com.filip2801.cars.carsauctions.auction.infrastructure

import com.filip2801.cars.carsauctions.auction.domain.Auction
import com.filip2801.cars.carsauctions.auction.domain.AuctionRepository
import com.filip2801.cars.carsauctions.testutils.ControllerIntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.testutils.TestUtils.isDateCloseToNow
import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

class AuctionControllerITSpec extends ControllerIntegrationTestSpecification {

    @Autowired
    AuctionRepository auctionRepository

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

        isDateCloseToNow(parseDate(response.body.startTime), 1000)
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

    def "should make a bid"() {
        given:
        mockDealerUser()
        def auction = Auction.start(uniqueId(), 'email@test.com', 100)
        auctionRepository.save(auction)

        def requestPayload = [bidValue: 150]

        when:
        var response = sendPost("auctions/$auction.id/bids", requestPayload)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id
        response.body.auctionId == auction.id
        response.body.dealerId == getLoggedInUser().id
        response.body.bidValue == requestPayload.bidValue
        response.body.status == 'MADE'
        isDateCloseToNow(parseDate(response.body.time), 1000)
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
                car                 : [
                        customerEmailAddress: 'test@customer.com',
                        makeId              : uniqueId(),
                        modelId             : uniqueId(),
                        variantId           : uniqueId(),
                        manufacturingYear   : 2015,
                        registrationYear    : 2016
                ]
        ]
    }

    def "should run again auction which finished without finding winner"() {
        given:
        mockAgentUser()
        var firstAuction = Auction.start(uniqueId(), 'test@example.com', 100)
        firstAuction.expectedEndTime = LocalDateTime.now().minusNanos(1)
        firstAuction.markAsEnded()
        firstAuction.finishWithoutSatisfiedResult()

        auctionRepository.save(firstAuction)


        when:
        var response = sendPost("auctions/$firstAuction.id/run-again", Map.of())

        then:
        response.statusCode == HttpStatus.OK
        response.body.id != null
        response.body.id != firstAuction.id
        response.body.carId == firstAuction.carId

        isDateCloseToNow(parseDate(response.body.startTime), 1000)
        parseDate(response.body.expectedEndTime) == parseDate(response.body.startTime).plusMinutes(24)

        response.body.anchorBid == firstAuction.anchorBid
        response.body.highestBid == null
        response.body.leadingDealerId == null
        response.body.status == 'RUNNING'
    }

    def "should not run again completed auction"() {
        given:
        mockAgentUser()
        var firstAuction = Auction.start(uniqueId(), 'test@example.com', 100)
        firstAuction.expectedEndTime = LocalDateTime.now().minusNanos(1)
        firstAuction.markAsEnded()
        firstAuction.complete()

        auctionRepository.save(firstAuction)


        when:
        var response = sendPost("auctions/$firstAuction.id/run-again", Map.of())

        then:
        thrown status400()
    }

    LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date);
    }

}
