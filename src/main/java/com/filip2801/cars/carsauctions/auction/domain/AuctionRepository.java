package com.filip2801.cars.carsauctions.auction.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findAllByStatusAndExpectedEndTimeBefore(AuctionStatus status, LocalDateTime expectedEndTimeBefore);

    List<Auction> findAllByCarId(Long carId);

    List<Auction> findAllByStatus(AuctionStatus status);

}
