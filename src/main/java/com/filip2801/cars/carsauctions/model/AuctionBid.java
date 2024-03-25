package com.filip2801.cars.carsauctions.model;

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
    @GeneratedValue
    private Long id;

    private Long dealerId;
    private Long auctionId;
    private Integer bidValue;
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private AuctionBidStatus status;
}
