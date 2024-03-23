package com.filip2801.cars.carsauctions.dto;

public record CarDto(
        Long id,
        Long makeId,
        Long modelId,
        Long variantId,
        int manufacturingYear,
        int registrationYear
) {
}
