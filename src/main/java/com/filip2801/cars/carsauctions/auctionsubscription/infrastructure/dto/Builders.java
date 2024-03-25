package com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto;

import com.filip2801.cars.carsauctions.auctionsubscription.domain.AuctionSubscription;

public class Builders {

    public static AuctionSubscriptionDto toAuctionSubscription(AuctionSubscription auctionSubscription) {
        return new AuctionSubscriptionDto(
                auctionSubscription.getId(),
                auctionSubscription.getCarMakeId(),
                auctionSubscription.getDealerId()
        );
    }
}
