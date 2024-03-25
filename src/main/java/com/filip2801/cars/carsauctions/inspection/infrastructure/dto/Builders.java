package com.filip2801.cars.carsauctions.inspection.infrastructure.dto;

import com.filip2801.cars.carsauctions.inspection.domain.Car;
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointment;

public class Builders {

    public static InspectionAppointmentBookingDto toAppointmentBookingDto(Car car, InspectionAppointment inspectionAppointment) {
        return new InspectionAppointmentBookingDto(
                inspectionAppointment.getId(),
                inspectionAppointment.getLocationId(),
                inspectionAppointment.getTime(),
                inspectionAppointment.getCustomerEmailAddress(),
                inspectionAppointment.getStatus(),
                toCarDto(car));
    }


    public static CarDto toCarDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getMakeId(),
                car.getModelId(),
                car.getVariantId(),
                car.getManufacturingYear(),
                car.getRegistrationYear());
    }

    public static InspectionAppointmentDto toAppointmentDto(InspectionAppointment inspectionAppointment) {
        return new InspectionAppointmentDto(
                inspectionAppointment.getId(),
                inspectionAppointment.getLocationId(),
                inspectionAppointment.getTime(),
                inspectionAppointment.getCustomerEmailAddress(),
                inspectionAppointment.getStatus(),
                inspectionAppointment.getCarId());
    }

}
