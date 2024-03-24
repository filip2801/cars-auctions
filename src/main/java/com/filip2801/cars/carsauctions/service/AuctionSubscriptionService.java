package com.filip2801.cars.carsauctions.service;

import com.filip2801.cars.carsauctions.dto.AuctionSubscriptionDto;
import com.filip2801.cars.carsauctions.dto.Builders;
import com.filip2801.cars.carsauctions.exception.BadRequestException;
import com.filip2801.cars.carsauctions.model.AuctionSubscription;
import com.filip2801.cars.carsauctions.repository.AuctionSubscriptionRepository;
import com.filip2801.cars.carsauctions.web.security.CustomUserDetails;
import com.filip2801.cars.carsauctions.web.security.UserContextHolder;
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
