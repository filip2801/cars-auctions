package com.filip2801.cars.carsauctions.auction.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionBidRepository extends JpaRepository<AuctionBid, Long> {

    List<AuctionBid> findByAuctionId(Long auctionId);

}
