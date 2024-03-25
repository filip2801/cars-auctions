package com.filip2801.cars.carsauctions.auction.domain

import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentService
import com.filip2801.cars.carsauctions.testutils.TestUtils
import spock.lang.Specification

import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.auction.domain.AuctionStatus.ENDED
import static com.filip2801.cars.carsauctions.auction.domain.AuctionStatus.RUNNING
import static com.filip2801.cars.carsauctions.testutils.TestUtils.isDateCloseToNow

class AuctionServiceSpec extends Specification {

    AuctionRepository auctionRepository = Mock()
    AuctionBidRepository auctionBidRepository = Mock()
    InspectionAppointmentService inspectionAppointmentService = Mock()

    AuctionService service = new AuctionService(auctionRepository, auctionBidRepository, inspectionAppointmentService)

    def "should mark auctions as ended"() {
        given:
        var auction1 = Auction.start(TestUtils.uniqueId(), 'test@email.com', 100)
        var auction2 = Auction.start(TestUtils.uniqueId(), 'test2@email.com', 100)
        auction1.expectedEndTime = LocalDateTime.now().minusSeconds(1)
        auction2.expectedEndTime = LocalDateTime.now().minusSeconds(1)

        auctionRepository.findAllByStatusAndExpectedEndTimeBefore(RUNNING, { isDateCloseToNow(it, 1000) } as LocalDateTime) >> [auction1, auction2]

        when:
        service.endExpiredAuctions()

        then:
        auction1.status == ENDED
        auction2.status == ENDED
    }
}
