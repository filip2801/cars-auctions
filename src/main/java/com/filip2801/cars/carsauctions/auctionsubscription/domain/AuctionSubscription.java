package com.filip2801.cars.carsauctions.auctionsubscription.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuctionSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_subscription_seq")
    @SequenceGenerator(name = "auction_subscription_seq", allocationSize = 1)
    private Long id;

    private Long carMakeId;
    private Long dealerId;

}
