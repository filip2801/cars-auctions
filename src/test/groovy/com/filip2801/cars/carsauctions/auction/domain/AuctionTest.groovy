package com.filip2801.cars.carsauctions.auction.domain


import spock.lang.Specification

import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.testutils.TestUtils.isDateCloseToNow
import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

class AuctionTest extends Specification {

    def "should start auction"() {
        given:
        var carId = uniqueId()
        var customerEmailAddress = 'test@example.com'
        var anchorBid = 100

        when:
        def auction = Auction.start(carId, customerEmailAddress, anchorBid)

        then:
        auction.carId == carId
        auction.customerEmailAddress == customerEmailAddress
        auction.anchorBid == anchorBid
        isDateCloseToNow(auction.startTime, 1000)
        auction.expectedEndTime == auction.startTime.plusMinutes(24)
        auction.status == AuctionStatus.RUNNING
        auction.highestBid == null
        auction.leadingBidderId == null
    }

    def "should place first bid"() {
        given:
        var carId = uniqueId()
        var anchorBid = 100
        var firstBid = anchorBid

        var auction = Auction.start(carId, 'test@example.com', anchorBid)
        var originalExpectedEndTime = auction.getExpectedEndTime()

        var bidderId = uniqueId()

        when:
        var bidResult = auction.makeBid(bidderId, firstBid)

        then:
        bidResult.bidStatus() == AuctionBidStatus.MADE
        isDateCloseToNow(bidResult.time(), 1000)

        auction.highestBid == firstBid
        auction.leadingBidderId == bidderId
        auction.expectedEndTime == originalExpectedEndTime
    }

    def "should place second bid"() {
        given:
        var carId = uniqueId()
        var anchorBid = 100

        var auction = Auction.start(carId, 'test@example.com', anchorBid)
        var originalExpectedEndTime = auction.getExpectedEndTime()

        var firstBidderId = uniqueId()
        var secondBidderId = uniqueId()
        var firstBid = 101
        var secondBid = 102

        auction.makeBid(firstBidderId, firstBid)

        when:
        var bidResult = auction.makeBid(secondBidderId, secondBid)

        then:
        bidResult.bidStatus() == AuctionBidStatus.MADE
        isDateCloseToNow(bidResult.time(), 1000)

        auction.highestBid == secondBid
        auction.leadingBidderId == secondBidderId
        auction.expectedEndTime == originalExpectedEndTime
    }

    def "should extend auction on late bid"() {
        given:
        var carId = uniqueId()
        var auction = Auction.start(carId, 'latebid@example.com', 100)
        var bidderId = uniqueId()
        var newBid = 105

        def firstExpectedEndTime = LocalDateTime.now().plusSeconds(119)
        auction.expectedEndTime = firstExpectedEndTime

        when:
        var bidResult = auction.makeBid(bidderId, newBid)

        then:
        bidResult.bidStatus() == AuctionBidStatus.MADE
        auction.expectedEndTime == firstExpectedEndTime.plusMinutes(2)
    }

    def "should not extend auction when bid is not late"() {
        given:
        var carId = uniqueId()
        var auction = Auction.start(carId, 'test@example.com', 100)
        var bidderId = uniqueId()
        var newBid = 105

        // simulate bidding in the last 3 minutes
        def firstExpectedEndTime = LocalDateTime.now().plusMinutes(3)
        auction.expectedEndTime = firstExpectedEndTime

        when:
        var bidResult = auction.makeBid(bidderId, newBid)

        then:
        bidResult.bidStatus() == AuctionBidStatus.MADE
        auction.expectedEndTime == firstExpectedEndTime
    }

    def "should not place first bid when it is lower than anchor bid"() {
        given:
        var carId = uniqueId()
        var anchorBid = 100

        var auction = Auction.start(carId, 'test@example.com', anchorBid)

        var bidderId = uniqueId()
        var newBid = 99

        when:
        var bidResult = auction.makeBid(bidderId, newBid)

        then:
        bidResult.bidStatus() == AuctionBidStatus.REJECTED
        isDateCloseToNow(bidResult.time(), 1000)

        auction.highestBid == null
        auction.leadingBidderId == null
    }

    def "should not place bid when it is lower than highest bid"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)

        var leadingBidderId = uniqueId()
        var highestBid = 150
        auction.makeBid(leadingBidderId, highestBid)

        when:
        var bidResult = auction.makeBid(uniqueId(), highestBid - 1)

        then:
        bidResult.bidStatus() == AuctionBidStatus.REJECTED
        isDateCloseToNow(bidResult.time(), 1000)

        auction.highestBid == highestBid
        auction.leadingBidderId == leadingBidderId
    }

    def "should not place bid when it is equal to highest bid"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)

        var leadingBidderId = uniqueId()
        var highestBid = 150
        auction.makeBid(leadingBidderId, highestBid)

        when:
        var bidResult = auction.makeBid(uniqueId(), highestBid)

        then:
        bidResult.bidStatus() == AuctionBidStatus.REJECTED
        isDateCloseToNow(bidResult.time(), 1000)

        auction.highestBid == highestBid
        auction.leadingBidderId == leadingBidderId
    }

    def "should not place bid when bid time is after auction end time"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)

        var leadingBidderId = uniqueId()
        var highestBid = 150
        auction.makeBid(leadingBidderId, highestBid)

        // simulate auction end time in the past
        def firstExpectedEndTime = LocalDateTime.now().minusNanos(1)
        auction.expectedEndTime = firstExpectedEndTime

        when:
        var bidResult = auction.makeBid(uniqueId(), highestBid + 1)

        then:
        bidResult.bidStatus() == AuctionBidStatus.REJECTED
        isDateCloseToNow(bidResult.time(), 1000)

        auction.expectedEndTime == firstExpectedEndTime
        auction.highestBid == highestBid
        auction.leadingBidderId == leadingBidderId
    }

    def "should mark auction as ended"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)
        auction.expectedEndTime = LocalDateTime.now().minusNanos(1)

        when:
        auction.markAsEnded()

        then:
        auction.status == AuctionStatus.ENDED
    }

    def "should not mark auction as ended when it is not running"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)
        auction.expectedEndTime = LocalDateTime.now().minusNanos(1)
        auction.markAsEnded()

        when:
        auction.markAsEnded()

        then:
        thrown IllegalStateException
    }


    def "should not mark auction as ended when it is not expired"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)

        when:
        auction.markAsEnded()

        then:
        thrown IllegalStateException
    }

    def "should complete ended auction"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)
        auction.expectedEndTime = LocalDateTime.now().minusNanos(1)
        auction.markAsEnded()

        when:
        auction.complete()

        then:
        auction.status == AuctionStatus.COMPLETED
    }

    def "should finish ended auction without finding winner"() {
        given:
        var carId = uniqueId()

        var auction = Auction.start(carId, 'test@example.com', 100)
        auction.expectedEndTime = LocalDateTime.now().minusNanos(1)
        auction.markAsEnded()

        when:
        auction.finishWithoutSatisfiedResult()

        then:
        auction.status == AuctionStatus.FINISHED_WITHOUT_WINNER
    }
}
