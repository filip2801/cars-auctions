package com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto;

public record AuctionStartAlertedEvent(
        Long dealerId,
        Long auctionId,
        Long carId
) {
}
