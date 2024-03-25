package com.filip2801.cars.carsauctions.auction.domain

import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.AuctionEventPublisher
import com.filip2801.cars.carsauctions.testutils.SimpleIntegrationTestSpecification
import com.filip2801.cars.carsauctions.testutils.TestUtils
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

import java.time.LocalDateTime
import java.util.concurrent.Executors

import static com.filip2801.cars.carsauctions.auction.domain.AuctionStatus.*
import static com.filip2801.cars.carsauctions.testutils.TestUtils.isDateCloseToNow
import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

class AuctionServiceITSpec extends SimpleIntegrationTestSpecification {

    @Autowired
    AuctionRepository auctionRepository
    @Autowired
    AuctionBidRepository auctionBidRepository

    @SpringBean
    AuctionEventPublisher auctionEventPublisher = Mock()

    @Subject
    @Autowired
    AuctionService auctionService

    def "should make a bid"() {
        given:
        mockDealerUser()
        def auction = Auction.start(uniqueId(), 'email@test.com', 100)
        auctionRepository.save(auction)

        when:
        var auctionBidDto = auctionService.makeBid(auction.id, 150)

        then:
        auctionBidDto.id() != null
        auctionBidDto.auctionId() == auction.id
        auctionBidDto.dealerId() == loggedInUser.userId
        auctionBidDto.bidValue() == 150
        auctionBidDto.status() == AuctionBidStatus.MADE
        isDateCloseToNow(auctionBidDto.time(), 1000)
    }

    def "should reject a bid"() {
        given:
        mockDealerUser()
        def auction = Auction.start(uniqueId(), 'email@test.com', 100)
        auctionRepository.save(auction)

        auctionService.makeBid(auction.id, 150)

        when:
        var auctionBidDto = auctionService.makeBid(auction.id, 149)

        then:
        auctionBidDto.id() != null
        auctionBidDto.auctionId() == auction.id
        auctionBidDto.dealerId() == loggedInUser.userId
        auctionBidDto.bidValue() == 149
        auctionBidDto.status() == AuctionBidStatus.REJECTED
        isDateCloseToNow(auctionBidDto.time(), 1000)
    }

    def "should make several bids concurrently"() {
        given:
        def anchorBid = 100
        def auction = Auction.start(uniqueId(), 'email@test.com', anchorBid)
        auctionRepository.save(auction)

        int numberOfBids = 200
        int numberOfThreads = (numberOfBids / 5).intValue()
        var executorService = Executors.newFixedThreadPool(numberOfThreads)

        when:
        for (i in 0..<numberOfBids) {
            executorService.execute(() -> {
                mockDealerUser()
                auctionService.makeBid(auction.id, anchorBid + i)
            })
        }

        then:
        eventually {
            var auctionBids = auctionBidRepository.findByAuctionId(auction.id)
            assert auctionBids.size() == numberOfBids
            assert auctionBids.findAll { it.status == AuctionBidStatus.MADE }.size() > 0
        }
    }

    def "should mark auctions as ended"() {
        given:
        var auction1 = Auction.start(TestUtils.uniqueId(), 'test@email.com', 100)
        var auction2 = Auction.start(TestUtils.uniqueId(), 'test2@email.com', 100)
        var auction3 = Auction.start(TestUtils.uniqueId(), 'test2@email.com', 100)
        auction1.expectedEndTime = LocalDateTime.now().minusSeconds(1)
        auction2.expectedEndTime = LocalDateTime.now().minusSeconds(1)

        auctionRepository.saveAll([auction1, auction2, auction3])

        when:
        auctionService.endExpiredAuctions()

        then:
        var updatedAction1 = auctionRepository.findById(auction1.id).get()
        updatedAction1.status == ENDED

        var updatedAction2 = auctionRepository.findById(auction2.id).get()
        updatedAction2.status == ENDED

        var updatedAction3 = auctionRepository.findById(auction3.id).get()
        updatedAction3.status == RUNNING

        and:
        1 * auctionEventPublisher.publishAuctionEndedEvent({ it.id == auction1.id && it.status == ENDED })
        1 * auctionEventPublisher.publishAuctionEndedEvent({ it.id == auction2.id && it.status == ENDED })
    }

    def "should complete auction"() {
        given:
        var auction = Auction.start(TestUtils.uniqueId(), 'test@email.com', 100)
        auction.expectedEndTime = LocalDateTime.now().minusSeconds(1)
        auction.markAsEnded()

        auctionRepository.save(auction)

        when:
        auctionService.finishWithSatisfiedResult(auction.id)

        then:
        var updatedAction = auctionRepository.findById(auction.id).get()
        updatedAction.status == COMPLETED

        and:
        1 * auctionEventPublisher.publishAuctionResultSatisfied({ it.status == COMPLETED })
    }

    def "should finish auction without finding winner"() {
        given:
        var auction = Auction.start(TestUtils.uniqueId(), 'test@email.com', 100)
        auction.expectedEndTime = LocalDateTime.now().minusSeconds(1)
        auction.markAsEnded()

        auctionRepository.save(auction)

        when:
        auctionService.finishWithNotSatisfiedResult(auction.id)

        then:
        var updatedAction = auctionRepository.findById(auction.id).get()
        updatedAction.status == FINISHED_WITHOUT_WINNER

        and:
        1 * auctionEventPublisher.publishAuctionResultNotSatisfied({ it.status == FINISHED_WITHOUT_WINNER })
    }

}
