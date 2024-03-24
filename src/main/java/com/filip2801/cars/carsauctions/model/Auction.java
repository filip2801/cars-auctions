package com.filip2801.cars.carsauctions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Auction {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private Long carId;
    private String customerEmailAddress;
    private LocalDateTime startTime;
    private LocalDateTime expectedEndTime;
    private Integer anchorBid;
    private Integer highestBid;
    private Long leadingDealerId;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;
}
