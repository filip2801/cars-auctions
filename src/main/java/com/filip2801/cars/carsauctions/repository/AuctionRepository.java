package com.filip2801.cars.carsauctions.repository;

import com.filip2801.cars.carsauctions.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
