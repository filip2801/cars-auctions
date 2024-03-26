package com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.messsaging;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionStartedEvent;
import com.filip2801.cars.carsauctions.auctionsubscription.domain.AuctionSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SubscriptionsAuctionEventListener {

    private final AuctionSubscriptionService auctionSubscriptionService;

    @RabbitListener(queues = AuctionSubscriptionQueuesConfiguration.AUCTION_STARTED_QUEUE)
    public void onAuctionEnded(@Payload AuctionStartedEvent event) {
        log.debug("Handling {} ", event);

        auctionSubscriptionService.notifyOnAuctionCreated(event.auction().id(), event.auction().carId());
    }
}
