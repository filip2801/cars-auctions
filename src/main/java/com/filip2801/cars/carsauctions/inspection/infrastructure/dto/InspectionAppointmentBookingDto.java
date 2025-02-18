package com.filip2801.cars.carsauctions.inspection.infrastructure.dto;

import com.filip2801.cars.carsauctions.car.infrastructure.dto.CarDto;
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentStatus;

import java.time.LocalDateTime;

public record InspectionAppointmentBookingDto(
        Long appointmentId,
        Long locationId,
        LocalDateTime time,
        InspectionAppointmentStatus status,
        CarDto car
) {
}