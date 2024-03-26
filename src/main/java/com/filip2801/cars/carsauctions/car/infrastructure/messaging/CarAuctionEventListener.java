package com.filip2801.cars.carsauctions.car.infrastructure.messaging;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionCompletedEvent;
import com.filip2801.cars.carsauctions.car.domain.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CarAuctionEventListener {

    private final CarService carService;

    @RabbitListener(queues = CarQueuesConfiguration.CARS_AUCTION_COMPLETED)
    public void onAuctionCompletedEvent(@Payload AuctionCompletedEvent event) {
        log.debug("Handling {} ", event);

        carService.assignWinner(event.auction().carId(), event.auction().leadingDealerId());
    }
}
