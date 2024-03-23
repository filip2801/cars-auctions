package com.filip2801.cars.carsauctions.dto;

import com.filip2801.cars.carsauctions.model.InspectionAppointmentStatus;

public record ChangeAppointmentStatusRequest(
    InspectionAppointmentStatus status
) {
}
