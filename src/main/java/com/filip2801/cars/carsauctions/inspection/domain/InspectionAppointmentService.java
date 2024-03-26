package com.filip2801.cars.carsauctions.inspection.domain;

import com.filip2801.cars.carsauctions.car.domain.CarService;
import com.filip2801.cars.carsauctions.common.exception.BadRequestException;
import com.filip2801.cars.carsauctions.common.exception.ResourceNotFoundException;
import com.filip2801.cars.carsauctions.inspection.infrastructure.dto.InspectionAppointmentBookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentStatus.BOOKED;
import static com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentStatus.INSPECTION_SUCCESSFUL;
import static com.filip2801.cars.carsauctions.inspection.infrastructure.dto.Builders.toAppointmentBookingDto;

@RequiredArgsConstructor
@Service
public class InspectionAppointmentService {

    private final InspectionAppointmentRepository inspectionAppointmentRepository;
    private final CarService carService;

    @Transactional
    public InspectionAppointmentBookingDto bookAppointment(InspectionAppointmentBookingDto inspectionAppointmentBookingDto) {
        var car = carService.registerCar(inspectionAppointmentBookingDto.car());
        var appointment = inspectionAppointmentRepository.save(toNewAppointment(inspectionAppointmentBookingDto, car.id()));

        return toAppointmentBookingDto(car, appointment);
    }

    private InspectionAppointment toNewAppointment(InspectionAppointmentBookingDto inspectionAppointmentBookingDto, Long carId) {
        return InspectionAppointment.builder()
                .carId(carId)
                .status(InspectionAppointmentStatus.BOOKED)
                .locationId(inspectionAppointmentBookingDto.locationId())
                .time(inspectionAppointmentBookingDto.time())
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
        return inspectionAppointmentRepository.findByCarId(carId);
    }
}
