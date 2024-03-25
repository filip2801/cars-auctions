package com.filip2801.cars.carsauctions.auction.infrastructure.dto;

import com.filip2801.cars.carsauctions.auction.domain.Auction;
import com.filip2801.cars.carsauctions.auction.domain.AuctionBid;

public class Builders {

    public static AuctionDto toAuctionDto(Auction auction) {
        return new AuctionDto(
                auction.getId(),
                auction.getCarId(),
                auction.getCustomerEmailAddress(),
                auction.getStartTime(),
                auction.getExpectedEndTime(),
                auction.getAnchorBid(),
                auction.getHighestBid(),
                auction.getLeadingBidderId(),
                auction.getStatus());
    }

    public static AuctionBidDto toAuctionBitDto(AuctionBid bid) {
        return new AuctionBidDto(
                bid.getId(),
                bid.getDealerId(),
                bid.getAuctionId(),
                bid.getBidValue(),
                bid.getTime(),
                bid.getStatus()
        );
    }
}
