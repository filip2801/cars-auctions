package com.filip2801.cars.carsauctions.auction.infrastructure.dto;

import com.filip2801.cars.carsauctions.auction.domain.AuctionBidStatus;

import java.time.LocalDateTime;

public record AuctionBidDto(
        Long id,
        Long dealerId,
        Long auctionId,
        Integer bidValue,
        LocalDateTime time,
        AuctionBidStatus status
) {
}
