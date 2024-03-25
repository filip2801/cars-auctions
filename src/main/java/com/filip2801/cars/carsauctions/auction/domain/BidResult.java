package com.filip2801.cars.carsauctions.auction.domain;

import java.time.LocalDateTime;

public record BidResult(
        AuctionBidStatus bidStatus,
        LocalDateTime time
) {
}
