package com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.messsaging;

import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.RabbitMqExchanges;
import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.RabbitMqRoutingKeys;
import com.filip2801.cars.carsauctions.common.messaging.RabbitQueueAdmin;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class AuctionSubscriptionQueuesConfiguration {

    public static final String AUCTION_STARTED_QUEUE = "auctions.auctionStarted";

    private final RabbitQueueAdmin rabbitQueueAdmin;

    @PostConstruct
    public void init() {
        rabbitQueueAdmin.createDirectExchangeAndQueue(AUCTION_STARTED_QUEUE, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_STARTED);
    }

}
