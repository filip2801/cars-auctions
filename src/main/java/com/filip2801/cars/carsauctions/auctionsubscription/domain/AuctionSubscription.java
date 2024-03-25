package com.filip2801.cars.carsauctions.auctionsubscription.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuctionSubscription {

    @Id
    @GeneratedValue
    private Long id;

    private Long carMakeId;
    private Long dealerId;

}
