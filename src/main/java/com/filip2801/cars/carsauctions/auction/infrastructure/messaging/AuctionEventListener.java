package com.filip2801.cars.carsauctions.auction.infrastructure.messaging;

import com.filip2801.cars.carsauctions.auction.domain.AuctionService;
import com.filip2801.cars.carsauctions.auction.domain.satisfaction.AuctionSatisfactionService;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionEndedEvent;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionResultNotSatisfiedEvent;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionResultSatisfiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuctionEventListener {

    private final AuctionSatisfactionService auctionSatisfactionService;
    private final AuctionService auctionService;

    @RabbitListener(queues = AuctionQueuesConfiguration.AUCTION_ENDED_QUEUE)
    public void onAuctionEnded(@Payload AuctionEndedEvent event) {
        log.info("Handling {} ", event);
        auctionSatisfactionService.checkResultSatisfaction(event.auction());
    }

    @RabbitListener(queues = AuctionQueuesConfiguration.AUCTION_RESULT_SATISFIED_QUEUE)
    public void onAuctionResultSatisfiedEvent(@Payload AuctionResultSatisfiedEvent event) {
        log.info("Handling {} ", event);
        auctionService.finishWithSatisfiedResult(event.auction().id());
    }

    @RabbitListener(queues = AuctionQueuesConfiguration.AUCTION_RESULT_NOT_SATISFIED_QUEUE)
    public void onAuctionResultNotSatisfiedEvent(@Payload AuctionResultNotSatisfiedEvent event) {
        log.info("Handling {} ", event);
        auctionService.finishWithNotSatisfiedResult(event.auction().id());
    }
}
