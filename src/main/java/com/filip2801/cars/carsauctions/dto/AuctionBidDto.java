package com.filip2801.cars.carsauctions.dto;

import com.filip2801.cars.carsauctions.model.AuctionBidStatus;

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
