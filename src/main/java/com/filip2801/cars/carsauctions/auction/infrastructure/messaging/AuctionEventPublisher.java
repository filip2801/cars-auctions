package com.filip2801.cars.carsauctions.auction.infrastructure.messaging;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionEndedEvent;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionResultSatisfiedEvent;
import com.filip2801.cars.carsauctions.common.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuctionEventPublisher {

    private final EventPublisher eventPublisher;

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

}
