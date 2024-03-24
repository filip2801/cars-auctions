package com.filip2801.cars.carsauctions.web;

import com.filip2801.cars.carsauctions.dto.*;
import com.filip2801.cars.carsauctions.exception.ResourceNotFoundException;
import com.filip2801.cars.carsauctions.service.InspectionAppointmentService;
import com.filip2801.cars.carsauctions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/dealers", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class DealersController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('AGENT')")
    UserDto registerDealer(@RequestBody UserDto userDto) {
        return userService.registerDealer(userDto);
    }

}
