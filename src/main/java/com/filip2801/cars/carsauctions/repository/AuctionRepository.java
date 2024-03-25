package com.filip2801.cars.carsauctions.repository;

import com.filip2801.cars.carsauctions.model.Auction;
import com.filip2801.cars.carsauctions.model.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findAllByStatusAndExpectedEndTimeBefore(AuctionStatus status, LocalDateTime expectedEndTimeBefore);

}
