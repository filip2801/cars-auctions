package com.filip2801.cars.carsauctions.user.infrastructure;

import com.filip2801.cars.carsauctions.user.domain.UserService;
import com.filip2801.cars.carsauctions.user.infrastructure.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping
    public List<UserDto> getCarsByIds(@RequestParam List<Long> ids) {
        return userService.findAllById(ids);
    }

}
