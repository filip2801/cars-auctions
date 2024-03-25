package com.filip2801.cars.carsauctions.auction.domain.satisfaction;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto;
import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.AuctionEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuctionSatisfactionService {

    private final List<AuctionSatisfactionPolicy> satisfactionPolicies;
    private final AuctionEventPublisher auctionEventPublisher;

    public void checkResultSatisfaction(AuctionDto auctionDto) {
        if (isResultSatisfied(auctionDto)) {
            auctionEventPublisher.publishAuctionResultSatisfied(auctionDto);
        } else {
            auctionEventPublisher.publishAuctionResultNotSatisfied(auctionDto);
        }
    }

    private boolean isResultSatisfied(AuctionDto auction) {
        for (var policy : satisfactionPolicies) {
            if (!policy.isSatisfied(auction)) {
                return false;
            }
        }
        return true;
    }
}
