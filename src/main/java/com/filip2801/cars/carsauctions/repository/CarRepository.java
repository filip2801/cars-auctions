package com.filip2801.cars.carsauctions.repository;

import com.filip2801.cars.carsauctions.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
