package com.filip2801.cars.carsauctions.inspection.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    Long makeId;
    @Column(nullable = false)
    Long modelId;
    @Column(nullable = false)
    Long variantId;
    @Column(nullable = false)
    int manufacturingYear;
    @Column(nullable = false)
    int registrationYear;
}
