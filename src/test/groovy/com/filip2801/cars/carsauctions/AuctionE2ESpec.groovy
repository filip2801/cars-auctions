package com.filip2801.cars.carsauctions

import com.filip2801.cars.carsauctions.auction.domain.*
import com.filip2801.cars.carsauctions.car.domain.CarRepository
import com.filip2801.cars.carsauctions.car.domain.CarStatus
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentRepository
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentStatus
import com.filip2801.cars.carsauctions.testutils.ControllerIntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

@Stepwise
class AuctionE2ESpec extends ControllerIntegrationTestSpecification {

    @Autowired
    CarRepository carRepository
    @Autowired
    AuctionRepository auctionRepository
    @Autowired
    AuctionBidRepository auctionBidRepository
    @Autowired
    InspectionAppointmentRepository appointmentRepository
    @Autowired
    AuctionService auctionService


    @Shared
    def appointmentId
    @Shared
    def carId
    @Shared
    def auctionId

    def "should register new appointment"() {
        given:
        def bookInspectionAppointmentRequest = someAppointmentRequestPayload()

        when:
        var bookedInspectionAppointmentResponse = sendPost("inspection-appointments/booking", bookInspectionAppointmentRequest)
        carId = bookedInspectionAppointmentResponse.body.car.id
        appointmentId = bookedInspectionAppointmentResponse.body.appointmentId

        then:
        carRepository.findById(carId).isPresent()
        carRepository.findById(carId).get().getStatus() == CarStatus.TO_SELL
    }

    def "should finalise inspection"() {
        given:
        mockAgentUser()

        when:
        sendPut("inspection-appointments/$appointmentId/status", ['status': 'INSPECTION_SUCCESSFUL'])

        then:
        var inspectionAppointment = appointmentRepository.findById(appointmentId).get()
        inspectionAppointment.status == InspectionAppointmentStatus.INSPECTION_SUCCESSFUL
    }

    // todo subscriptions

    def "should start auction"() {
        given:
        mockAgentUser()

        def requestPayload = [
                carId               : carId,
                customerEmailAddress: 'email@test.com',
                anchorBid           : 100
        ]

        when:
        var response = sendPost("auctions", requestPayload)
        auctionId = response.body.id

        then:
        var auctions = auctionRepository.findAllByCarId(carId)
        auctions.size() == 1
        auctions.get(0).getStatus() == AuctionStatus.RUNNING
    }

    def "should bid first time"() {
        given:
        mockDealerUser()

        when:
        sendPost("auctions/$auctionId/bids", [bidValue: 150])

        then:
        var bids = auctionBidRepository.findByAuctionId(auctionId)
        bids.size() == 1
        bids.get(0).getStatus() == AuctionBidStatus.MADE
        bids.get(0).getBidValue() == 150

        var auction = auctionRepository.findById(auctionId).get()
        auction.highestBid == 150
        auction.leadingBidderId == userLoggedIn.id
    }

    def "should bid second time"() {
        given:
        mockDealerUser()

        when:
        sendPost("auctions/$auctionId/bids", [bidValue: 300])

        then:
        var bids = auctionBidRepository.findByAuctionId(auctionId)
        bids.size() == 2
        bids.get(1).getStatus() == AuctionBidStatus.MADE
        bids.get(1).getBidValue() == 300

        var auction = auctionRepository.findById(auctionId).get()
        auction.highestBid == 300
        auction.leadingBidderId == userLoggedIn.id
    }

    def "should end auction"() {
        given:
        var auction = auctionRepository.findById(auctionId).get()
        auction.expectedEndTime = LocalDateTime.now().minusMinutes(1)
        auctionRepository.save(auction)

        when:
        auctionService.endExpiredAuctions()

        then:
        eventually {
            var updatedAuction = auctionRepository.findById(auctionId).get()
            assert updatedAuction.status == AuctionStatus.COMPLETED
        }
    }

    private LinkedHashMap<String, Serializable> someAppointmentRequestPayload() {
        return [
                locationId: uniqueId(),
                time      : '2024-05-15T14:30:00',
                car       : [
                        customerEmailAddress: 'test@customer.com',
                        makeId              : uniqueId(),
                        modelId             : uniqueId(),
                        variantId           : uniqueId(),
                        manufacturingYear   : 2015,
                        registrationYear    : 2016
                ]
        ]
    }

    LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date);
    }

}
