package com.filip2801.cars.carsauctions.auctionsubscription.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionSubscriptionRepository extends JpaRepository<AuctionSubscription, Long> {

    List<AuctionSubscription> findAllByCarMakeId(Long makeId);

}
