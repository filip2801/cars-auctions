package com.filip2801.cars.carsauctions.auction.infrastructure.dto;

import com.filip2801.cars.carsauctions.auction.domain.AuctionStatus;

import java.time.LocalDateTime;

public record AuctionDto(
        Long id,
        Long carId,
        String customerEmailAddress,
        LocalDateTime startTime,
        LocalDateTime expectedEndTime,
        Integer anchorBid,
        Integer highestBid,
        Long leadingDealerId,
        AuctionStatus status) {

}
