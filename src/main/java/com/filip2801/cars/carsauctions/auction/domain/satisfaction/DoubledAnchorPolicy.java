package com.filip2801.cars.carsauctions.auction.domain.satisfaction;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto;
import org.springframework.stereotype.Service;

@Service
public class DoubledAnchorPolicy implements AuctionSatisfactionPolicy {

    @Override
    public boolean isSatisfied(AuctionDto auction) {
        return auction.highestBid() >= 2 * auction.anchorBid();
    }

}
