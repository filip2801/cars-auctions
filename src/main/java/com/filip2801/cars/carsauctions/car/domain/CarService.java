package com.filip2801.cars.carsauctions.car.domain;

import com.filip2801.cars.carsauctions.car.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.car.infrastructure.dto.CarDto;
import com.filip2801.cars.carsauctions.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;

    public CarDto registerCar(CarDto carDto) {
        var car = toNewCar(carDto);

        carRepository.save(car);

        return Builders.toCarDto(car);
    }

    private Car toNewCar(CarDto carDto) {
        return Car.builder()
                .customerEmailAddress(carDto.customerEmailAddress())
                .makeId(carDto.makeId())
                .modelId(carDto.modelId())
                .variantId(carDto.variantId())
                .registrationYear(carDto.registrationYear())
                .manufacturingYear(carDto.manufacturingYear())
                .status(CarStatus.TO_SELL)
                .build();
    }

    public CarDto findCarDto(Long carId) {
        return carRepository.findById(carId)
                .map(Builders::toCarDto)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
