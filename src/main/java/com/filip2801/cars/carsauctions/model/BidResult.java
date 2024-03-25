package com.filip2801.cars.carsauctions.model;

import java.time.LocalDateTime;

public record BidResult(
        AuctionBidStatus bidStatus,
        LocalDateTime time
) {
}
