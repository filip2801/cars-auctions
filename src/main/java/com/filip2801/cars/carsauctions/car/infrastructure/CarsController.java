package com.filip2801.cars.carsauctions.car.infrastructure;

import com.filip2801.cars.carsauctions.car.domain.CarRepository;
import com.filip2801.cars.carsauctions.car.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.car.infrastructure.dto.CarDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CarsController {

    private final CarRepository carRepository;

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping
    public List<CarDto> getCarsByIds(@RequestParam List<Long> ids) {
        return carRepository.findAllById(ids).stream()
                .map(Builders::toCarDto)
                .collect(Collectors.toList());
    }


}
