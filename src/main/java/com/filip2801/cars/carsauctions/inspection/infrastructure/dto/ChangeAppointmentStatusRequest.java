package com.filip2801.cars.carsauctions.inspection.infrastructure.dto;

import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentStatus;

public record ChangeAppointmentStatusRequest(
        InspectionAppointmentStatus status
) {
}
