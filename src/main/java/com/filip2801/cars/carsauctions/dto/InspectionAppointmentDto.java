package com.filip2801.cars.carsauctions.dto;

import com.filip2801.cars.carsauctions.model.InspectionAppointmentStatus;

import java.time.LocalDateTime;

public record InspectionAppointmentDto(
        Long appointmentId,
        Long locationId,
        LocalDateTime time,
        String customerEmailAddress,
        InspectionAppointmentStatus status,
        CarDto car
) {
}