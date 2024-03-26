package com.filip2801.cars.carsauctions.inspection.infrastructure.dto;

import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentStatus;

import java.time.LocalDateTime;

public record InspectionAppointmentDto(
        Long id,
        Long locationId,
        LocalDateTime time,
        InspectionAppointmentStatus status,
        Long carId
) {
}