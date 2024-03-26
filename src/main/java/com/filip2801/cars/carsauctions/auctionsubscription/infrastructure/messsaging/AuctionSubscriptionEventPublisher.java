package com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.messsaging;

import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto.AuctionStartAlertedEvent;
import com.filip2801.cars.carsauctions.common.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuctionSubscriptionEventPublisher {

    private final EventPublisher eventPublisher;

    public void publishAuctionStartAlerted(Long dealerId, Long auctionId, Long carId) {
        var event = new AuctionStartAlertedEvent(dealerId, auctionId, carId);
        eventPublisher.publishEvent(event, RabbitMqExchanges.AUCTIONS_SUBSCRIPTION_EVENTS, RabbitMqRoutingKeys.AUCTION_START_ALERTED);
    }

}
