package com.filip2801.cars.carsauctions.auction.domain.satisfaction

import com.filip2801.cars.carsauctions.auction.domain.AuctionStatus
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto
import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.AuctionEventPublisher
import spock.lang.Specification

import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

class AuctionSatisfactionServiceTest extends Specification {

    private final AuctionEventPublisher auctionEventPublisher = Mock()

    def "should check that auction result is satisfied"() {
        given:
        var auction = someAuctionDto()

        AuctionSatisfactionPolicy policy1 = (a) -> true
        AuctionSatisfactionPolicy policy2 = (a) -> true
        List<AuctionSatisfactionPolicy> policies = [policy1, policy2]

        var service = new AuctionSatisfactionService(policies, auctionEventPublisher)

        when:
        service.checkResultSatisfaction(auction)

        then:
        1 * auctionEventPublisher.publishAuctionResultSatisfied(auction)
    }

    def "should check that auction result is not satisfied"() {
        given:
        var auction = someAuctionDto()
        AuctionSatisfactionPolicy policy1 = (a) -> true
        AuctionSatisfactionPolicy policy2 = (a) -> false
        List<AuctionSatisfactionPolicy> policies = [policy1, policy2]

        var service = new AuctionSatisfactionService(policies, auctionEventPublisher)

        when:
        service.checkResultSatisfaction(auction)

        then:
        1 * auctionEventPublisher.publishAuctionResultNotSatisfied(auction)
    }

    private AuctionDto someAuctionDto() {
        new AuctionDto(uniqueId(), uniqueId(), 'email@test.com', LocalDateTime.now(), LocalDateTime.now(), 100, null, null, AuctionStatus.RUNNING)
    }
}
