package com.filip2801.cars.carsauctions.auction.infrastructure.messaging;

import com.filip2801.cars.carsauctions.common.messaging.RabbitQueueAdmin;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class QueuesConfiguration {

    public static final String AUCTION_ENDED_QUEUE = "auctions.auctionEnded";
    public static final String AUCTION_RESULT_SATISFIED_QUEUE = "auctions.resultSatisfied";
    public static final String AUCTION_RESULT_NOT_SATISFIED_QUEUE = "auctions.resultNotSatisfied";

    private final RabbitQueueAdmin rabbitQueueAdmin;

    @PostConstruct
    public void init() {
        rabbitQueueAdmin.createDirectExchangeAndQueue(AUCTION_ENDED_QUEUE, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_ENDED);
        rabbitQueueAdmin.createDirectExchangeAndQueue(AUCTION_RESULT_SATISFIED_QUEUE, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_RESULT_SATISFIED);
        rabbitQueueAdmin.createDirectExchangeAndQueue(AUCTION_RESULT_NOT_SATISFIED_QUEUE, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_RESULT_NOT_SATISFIED);
    }

}
