package com.filip2801.cars.carsauctions.repository;

import com.filip2801.cars.carsauctions.model.AuctionBid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionBidRepository extends JpaRepository<AuctionBid, Long> {

    List<AuctionBid> findByAuctionId(Long auctionId);

}
