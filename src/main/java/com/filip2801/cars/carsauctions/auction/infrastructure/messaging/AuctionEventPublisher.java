package com.filip2801.cars.carsauctions.auction.infrastructure.messaging;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.*;
import com.filip2801.cars.carsauctions.common.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuctionEventPublisher {

    private final EventPublisher eventPublisher;

    public void publishAuctionStartedEvent(AuctionDto auctionDto) {
        var event = new AuctionStartedEvent(auctionDto);
        eventPublisher.publishEvent(event, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_STARTED);
    }

    public void publishAuctionEndedEvent(AuctionDto auctionDto) {
        var event = new AuctionEndedEvent(auctionDto);
        eventPublisher.publishEvent(event, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_ENDED);
    }

    public void publishAuctionResultSatisfied(AuctionDto auctionDto) {
        var event = new AuctionResultSatisfiedEvent(auctionDto);
        eventPublisher.publishEvent(event, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_RESULT_SATISFIED);
    }

    public void publishAuctionResultNotSatisfied(AuctionDto auctionDto) {
        var event = new AuctionResultSatisfiedEvent(auctionDto);
        eventPublisher.publishEvent(event, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_RESULT_NOT_SATISFIED);
    }

    public void publishAuctionCompletedEvent(AuctionDto auctionDto) {
        var event = new AuctionCompletedEvent(auctionDto);
        eventPublisher.publishEvent(event, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_COMPLETED);
    }

}
