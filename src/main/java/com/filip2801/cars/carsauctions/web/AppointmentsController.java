package com.filip2801.cars.carsauctions.web;

import com.filip2801.cars.carsauctions.dto.InspectionAppointmentDto;
import com.filip2801.cars.carsauctions.service.InspectionAppointmentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AppointmentsController {

    private final InspectionAppointmentService inspectionAppointmentService;

    public AppointmentsController(InspectionAppointmentService inspectionAppointmentService) {
        this.inspectionAppointmentService = inspectionAppointmentService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    InspectionAppointmentDto bookAppointment(@RequestBody InspectionAppointmentDto appointment) {
        return inspectionAppointmentService.bookAppointment(appointment);
    }
}
