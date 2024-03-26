package com.filip2801.cars.carsauctions.car.domain;

import com.filip2801.cars.carsauctions.common.Validate;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_seq")
    @SequenceGenerator(name = "car_seq", allocationSize = 1)
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

    public void assignWinner(Long winnerId) {
        Validate.validateIsTrue(status == CarStatus.TO_SELL, "Car status is " + status);
        this.status = CarStatus.SOLD;
        this.winnerId = winnerId;
    }
}
