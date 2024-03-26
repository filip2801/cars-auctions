package com.filip2801.cars.carsauctions.car.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Car {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    String customerEmailAddress;
    @Column(nullable = false)
    Long makeId;
    @Column(nullable = false)
    Long modelId;
    @Column(nullable = false)
    Long variantId;
    @Column(nullable = false)
    int manufacturingYear;
    @Column(nullable = false)
    int registrationYear;

    @Enumerated(EnumType.STRING)
    CarStatus status;

    @Column
    Long winnerId;
}
