package com.filip2801.cars.carsauctions.service;

import com.filip2801.cars.carsauctions.dto.InspectionAppointmentDto;
import com.filip2801.cars.carsauctions.dto.CarDto;
import com.filip2801.cars.carsauctions.model.InspectionAppointment;
import com.filip2801.cars.carsauctions.model.Car;
import com.filip2801.cars.carsauctions.model.InspectionAppointmentStatus;
import com.filip2801.cars.carsauctions.repository.InspectionAppointmentRepository;
import com.filip2801.cars.carsauctions.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InspectionAppointmentService {

    private final InspectionAppointmentRepository inspectionAppointmentRepository;
    private final CarRepository carRepository;

    @Transactional
    public InspectionAppointmentDto bookAppointment(InspectionAppointmentDto inspectionAppointmentDto) {
        var car = carRepository.save(toNewCar(inspectionAppointmentDto.car()));
        var appointment = inspectionAppointmentRepository.save(toNewAppointment(inspectionAppointmentDto, car.getId()));

        return toAppointmentDto(car, appointment);
    }

    private InspectionAppointmentDto toAppointmentDto(Car car, InspectionAppointment inspectionAppointment) {
        return new InspectionAppointmentDto(
                inspectionAppointment.getId(),
                inspectionAppointment.getLocationId(),
                inspectionAppointment.getTime(),
                inspectionAppointment.getCustomerEmailAddress(),
                inspectionAppointment.getStatus(),
                toCarDto(car));
    }

    private CarDto toCarDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getMakeId(),
                car.getModelId(),
                car.getVariantId(),
                car.getManufacturingYear(),
                car.getRegistrationYear());
    }

    private Car toNewCar(CarDto carDto) {
        return Car.builder()
                .makeId(carDto.makeId())
                .modelId(carDto.modelId())
                .variantId(carDto.variantId())
                .registrationYear(carDto.registrationYear())
                .manufacturingYear(carDto.manufacturingYear())
                .build();
    }

    private InspectionAppointment toNewAppointment(InspectionAppointmentDto inspectionAppointmentDto, Long carId) {
        return InspectionAppointment.builder()
                .carId(carId)
                .status(InspectionAppointmentStatus.BOOKED)
                .locationId(inspectionAppointmentDto.locationId())
                .time(inspectionAppointmentDto.time())
                .customerEmailAddress(inspectionAppointmentDto.customerEmailAddress())
                .build();
    }

}
