package com.filip2801.cars.carsauctions.inspection.infrastructure;

import com.filip2801.cars.carsauctions.common.exception.ResourceNotFoundException;
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentService;
import com.filip2801.cars.carsauctions.inspection.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.inspection.infrastructure.dto.ChangeAppointmentStatusRequest;
import com.filip2801.cars.carsauctions.inspection.infrastructure.dto.InspectionAppointmentBookingDto;
import com.filip2801.cars.carsauctions.inspection.infrastructure.dto.InspectionAppointmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/inspection-appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class InspectionAppointmentsController {

    private final InspectionAppointmentService inspectionAppointmentService;

    @PostMapping(value = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("permitAll()")
    InspectionAppointmentBookingDto bookAppointment(@RequestBody InspectionAppointmentBookingDto appointment) {
        return inspectionAppointmentService.bookAppointment(appointment);
    }

    @GetMapping(value = "/{appointmentId}")
    @PreAuthorize("permitAll()")
    InspectionAppointmentDto getAppointment(@PathVariable Long appointmentId) {
        return inspectionAppointmentService.findById(appointmentId)
                .map(Builders::toAppointmentDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PutMapping(value = "/{appointmentId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('AGENT')")
    InspectionAppointmentDto updateStatus(@PathVariable Long appointmentId, @RequestBody ChangeAppointmentStatusRequest request) {
        var updatedAppointment = inspectionAppointmentService.changeStatus(appointmentId, request.status());

        return Builders.toAppointmentDto(updatedAppointment);
    }
}
