package com.filip2801.cars.carsauctions.car.infrastructure.messaging;

import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.RabbitMqExchanges;
import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.RabbitMqRoutingKeys;
import com.filip2801.cars.carsauctions.common.messaging.RabbitQueueAdmin;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class CarQueuesConfiguration {

    public static final String CARS_AUCTION_COMPLETED = "car.auctionCompleted";

    private final RabbitQueueAdmin rabbitQueueAdmin;

    @PostConstruct
    public void init() {
        rabbitQueueAdmin.createDirectExchangeAndQueue(CARS_AUCTION_COMPLETED, RabbitMqExchanges.AUCTIONS_EVENTS, RabbitMqRoutingKeys.AUCTION_COMPLETED);
    }

}
