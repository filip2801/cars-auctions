package com.filip2801.cars.carsauctions.repository;

import com.filip2801.cars.carsauctions.model.InspectionAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionAppointmentRepository extends JpaRepository<InspectionAppointment, Long> {
}
