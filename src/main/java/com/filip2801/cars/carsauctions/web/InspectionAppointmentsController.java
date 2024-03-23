package com.filip2801.cars.carsauctions.web;

import com.filip2801.cars.carsauctions.dto.Builders;
import com.filip2801.cars.carsauctions.dto.ChangeAppointmentStatusRequest;
import com.filip2801.cars.carsauctions.dto.InspectionAppointmentBookingDto;
import com.filip2801.cars.carsauctions.dto.InspectionAppointmentDto;
import com.filip2801.cars.carsauctions.exception.ResourceNotFoundException;
import com.filip2801.cars.carsauctions.service.InspectionAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/inspection-appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class InspectionAppointmentsController {

    private final InspectionAppointmentService inspectionAppointmentService;

    @PostMapping(value = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE)
    InspectionAppointmentBookingDto bookAppointment(@RequestBody InspectionAppointmentBookingDto appointment) {
        return inspectionAppointmentService.bookAppointment(appointment);
    }

    @GetMapping(value = "/{appointmentId}")
    InspectionAppointmentDto updateStatus(@PathVariable Long appointmentId) {
        return inspectionAppointmentService.findById(appointmentId)
                .map(Builders::toAppointmentDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PutMapping(value = "/{appointmentId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    InspectionAppointmentDto updateStatus(@PathVariable Long appointmentId, @RequestBody ChangeAppointmentStatusRequest request) {
        var updatedAppointment =  inspectionAppointmentService.changeStatus(appointmentId, request.status());

        return Builders.toAppointmentDto(updatedAppointment);
    }
}
