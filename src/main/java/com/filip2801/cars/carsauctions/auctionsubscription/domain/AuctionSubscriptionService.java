package com.filip2801.cars.carsauctions.auctionsubscription.domain;

import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto.AuctionSubscriptionDto;
import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.messsaging.AuctionSubscriptionEventPublisher;
import com.filip2801.cars.carsauctions.car.domain.CarService;
import com.filip2801.cars.carsauctions.car.infrastructure.dto.CarDto;
import com.filip2801.cars.carsauctions.common.exception.BadRequestException;
import com.filip2801.cars.carsauctions.common.security.CustomUserDetails;
import com.filip2801.cars.carsauctions.common.security.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuctionSubscriptionService {

    private final AuctionSubscriptionRepository auctionSubscriptionRepository;
    private final CarService carService;
    private final AuctionSubscriptionEventPublisher auctionSubscriptionEventPublisher;

    public AuctionSubscriptionDto subscribe(Long carMakeId) {
        CustomUserDetails loggedInUser = UserContextHolder.getLoggedInUser();
        if (!loggedInUser.isDealer()) {
            throw new BadRequestException();
        }

        AuctionSubscription auctionSubscription = new AuctionSubscription(null, carMakeId, loggedInUser.getUserId());
        auctionSubscriptionRepository.save(auctionSubscription);

        return Builders.toAuctionSubscription(auctionSubscription);
    }

    public void notifyOnAuctionCreated(Long auctionId, Long carId) {
        CarDto carDto = carService.findCarDto(auctionId);

        var subscriptions = auctionSubscriptionRepository.findAllByCarMakeId(carDto.makeId());

        subscriptions.forEach(subscription -> {
            auctionSubscriptionEventPublisher.publishAuctionStartAlerted(
                    subscription.getDealerId(),
                    auctionId,
                    carId);
        });
    }
}
