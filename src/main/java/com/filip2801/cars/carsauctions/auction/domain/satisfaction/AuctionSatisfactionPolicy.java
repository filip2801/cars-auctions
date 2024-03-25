package com.filip2801.cars.carsauctions.auction.domain.satisfaction;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto;

@FunctionalInterface
public interface AuctionSatisfactionPolicy {

    boolean isSatisfied(AuctionDto auction);

}
