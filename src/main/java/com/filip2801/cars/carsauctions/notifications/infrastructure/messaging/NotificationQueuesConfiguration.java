package com.filip2801.cars.carsauctions.notifications.infrastructure.messaging;

import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.messsaging.RabbitMqExchanges;
import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.messsaging.RabbitMqRoutingKeys;
import com.filip2801.cars.carsauctions.common.messaging.RabbitQueueAdmin;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class NotificationQueuesConfiguration {

    public static final String NOTIFICATIONS_AUCTION_START_ALERTED = "notifications.auctionStartAlerted";

    private final RabbitQueueAdmin rabbitQueueAdmin;

    @PostConstruct
    public void init() {
        rabbitQueueAdmin.createDirectExchangeAndQueue(NOTIFICATIONS_AUCTION_START_ALERTED, RabbitMqExchanges.AUCTIONS_SUBSCRIPTION_EVENTS, RabbitMqRoutingKeys.AUCTION_START_ALERTED);
    }

}
