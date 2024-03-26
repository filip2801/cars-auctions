package com.filip2801.cars.carsauctions.car.infrastructure.dto;

import com.filip2801.cars.carsauctions.car.domain.CarStatus;

public record CarDto(
        Long id,
        String customerEmailAddress,
        Long makeId,
        Long modelId,
        Long variantId,
        int manufacturingYear,
        int registrationYear,
        CarStatus status
) {
}
