package com.filip2801.cars.carsauctions.auctionsubscription.domain;

import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto.AuctionSubscriptionDto;
import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.common.exception.BadRequestException;
import com.filip2801.cars.carsauctions.common.security.CustomUserDetails;
import com.filip2801.cars.carsauctions.common.security.UserContextHolder;
import org.springframework.stereotype.Service;

@Service
public record AuctionSubscriptionService(
        AuctionSubscriptionRepository auctionSubscriptionRepository) {

    public AuctionSubscriptionDto subscribe(Long carMakeId) {
        CustomUserDetails loggedInUser = UserContextHolder.getLoggedInUser();
        if (!loggedInUser.isDealer()) {
            throw new BadRequestException();
        }

        AuctionSubscription auctionSubscription = new AuctionSubscription(null, carMakeId, loggedInUser.getUserId());
        auctionSubscriptionRepository.save(auctionSubscription);

        return Builders.toAuctionSubscription(auctionSubscription);
    }
}
