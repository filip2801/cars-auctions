package com.filip2801.cars.carsauctions.service

import com.filip2801.cars.carsauctions.SimpleIntegrationTestSpecification
import com.filip2801.cars.carsauctions.model.Auction
import com.filip2801.cars.carsauctions.model.AuctionBidStatus
import com.filip2801.cars.carsauctions.repository.AuctionBidRepository
import com.filip2801.cars.carsauctions.repository.AuctionRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

import java.util.concurrent.Executors

import static com.filip2801.cars.carsauctions.TestUtils.isDateCloseToNow
import static com.filip2801.cars.carsauctions.TestUtils.uniqueId

class AuctionServiceITSpec extends SimpleIntegrationTestSpecification {

    @Autowired
    AuctionRepository auctionRepository
    @Autowired
    AuctionBidRepository auctionBidRepository

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

}
