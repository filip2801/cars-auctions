package com.filip2801.cars.carsauctions.car.infrastructure.dto;

import com.filip2801.cars.carsauctions.car.domain.Car;

public class Builders {

    public static CarDto toCarDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getCustomerEmailAddress(),
                car.getMakeId(),
                car.getModelId(),
                car.getVariantId(),
                car.getManufacturingYear(),
                car.getRegistrationYear(),
                car.getStatus());
    }
}
