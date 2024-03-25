package com.filip2801.cars.carsauctions.auction.domain.satisfaction

import com.filip2801.cars.carsauctions.auction.domain.AuctionStatus
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto
import spock.lang.Specification

import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

class DoubledAnchorPolicyTest extends Specification {

    def policy = new DoubledAnchorPolicy()
    
    def "should check that policy is satisfied"() {
        expect:
        policy.isSatisfied(auction)

        where:
        auction << [
                someAuctionDto(100, 200),
                someAuctionDto(100, 201),
        ]
    }

    def "should check that policy is not satisfied"() {
        expect:
        !policy.isSatisfied(someAuctionDto(100, 199))
    }


    private AuctionDto someAuctionDto(int anchorBid, int highestBid) {
        new AuctionDto(uniqueId(), uniqueId(), 'email@test.com', LocalDateTime.now(), LocalDateTime.now(), anchorBid, highestBid, uniqueId(), AuctionStatus.RUNNING)
    }
}
