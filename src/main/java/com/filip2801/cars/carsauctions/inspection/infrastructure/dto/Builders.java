package com.filip2801.cars.carsauctions.inspection.infrastructure.dto;

import com.filip2801.cars.carsauctions.car.infrastructure.dto.CarDto;
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointment;

public class Builders {

    public static InspectionAppointmentBookingDto toAppointmentBookingDto(CarDto car, InspectionAppointment inspectionAppointment) {
        return new InspectionAppointmentBookingDto(
                inspectionAppointment.getId(),
                inspectionAppointment.getLocationId(),
                inspectionAppointment.getTime(),
                inspectionAppointment.getStatus(),
                car);
    }

    public static InspectionAppointmentDto toAppointmentDto(InspectionAppointment inspectionAppointment) {
        return new InspectionAppointmentDto(
                inspectionAppointment.getId(),
                inspectionAppointment.getLocationId(),
                inspectionAppointment.getTime(),
                inspectionAppointment.getStatus(),
                inspectionAppointment.getCarId());
    }

}
