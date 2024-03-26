package com.filip2801.cars.carsauctions.auction.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuctionBid {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_bid_seq")
    @SequenceGenerator(name = "auction_bid_seq", allocationSize = 1)
    private Long id;

    private Long dealerId;
    private Long auctionId;
    private Integer bidValue;
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private AuctionBidStatus status;
}
