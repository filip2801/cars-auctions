package com.filip2801.cars.carsauctions.inspection.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InspectionAppointmentRepository extends JpaRepository<InspectionAppointment, Long> {
    Optional<InspectionAppointment> findByCarId(Long carId);
}
