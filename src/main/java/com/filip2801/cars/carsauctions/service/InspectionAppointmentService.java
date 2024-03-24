package com.filip2801.cars.carsauctions.service;

import com.filip2801.cars.carsauctions.dto.CarDto;
import com.filip2801.cars.carsauctions.dto.InspectionAppointmentBookingDto;
import com.filip2801.cars.carsauctions.dto.InspectionAppointmentDto;
import com.filip2801.cars.carsauctions.exception.BadRequestException;
import com.filip2801.cars.carsauctions.exception.ResourceNotFoundException;
import com.filip2801.cars.carsauctions.model.Car;
import com.filip2801.cars.carsauctions.model.InspectionAppointment;
import com.filip2801.cars.carsauctions.model.InspectionAppointmentStatus;
import com.filip2801.cars.carsauctions.repository.CarRepository;
import com.filip2801.cars.carsauctions.repository.InspectionAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.filip2801.cars.carsauctions.dto.Builders.toAppointmentBookingDto;
import static com.filip2801.cars.carsauctions.dto.Builders.toAppointmentDto;
import static com.filip2801.cars.carsauctions.model.InspectionAppointmentStatus.BOOKED;
import static com.filip2801.cars.carsauctions.model.InspectionAppointmentStatus.INSPECTION_SUCCESSFUL;

@RequiredArgsConstructor
@Service
public class InspectionAppointmentService {

    private final InspectionAppointmentRepository inspectionAppointmentRepository;
    private final CarRepository carRepository;

    @Transactional
    public InspectionAppointmentBookingDto bookAppointment(InspectionAppointmentBookingDto inspectionAppointmentBookingDto) {
        var car = carRepository.save(toNewCar(inspectionAppointmentBookingDto.car()));
        var appointment = inspectionAppointmentRepository.save(toNewAppointment(inspectionAppointmentBookingDto, car.getId()));

        return toAppointmentBookingDto(car, appointment);
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

    private InspectionAppointment toNewAppointment(InspectionAppointmentBookingDto inspectionAppointmentBookingDto, Long carId) {
        return InspectionAppointment.builder()
                .carId(carId)
                .status(InspectionAppointmentStatus.BOOKED)
                .locationId(inspectionAppointmentBookingDto.locationId())
                .time(inspectionAppointmentBookingDto.time())
                .customerEmailAddress(inspectionAppointmentBookingDto.customerEmailAddress())
                .build();
    }

    public InspectionAppointment changeStatus(Long appointmentId, InspectionAppointmentStatus newStatus) {
        var appointment = inspectionAppointmentRepository.findById(appointmentId)
                .orElseThrow(ResourceNotFoundException::new);

        if (newStatus != INSPECTION_SUCCESSFUL || appointment.getStatus() != BOOKED) {
            throw new BadRequestException();
        }
        appointment.finaliseInspection();

        return inspectionAppointmentRepository.save(appointment);
    }

    public Optional<InspectionAppointment> findById(Long appointmentId) {
        return inspectionAppointmentRepository.findById(appointmentId);
    }

    public Optional<InspectionAppointment> findByCarId(Long carId) {
        return inspectionAppointmentRepository.findByCarId( carId);
    }
}
