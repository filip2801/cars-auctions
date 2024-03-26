package com.filip2801.cars.carsauctions.user.infrastructure;

import com.filip2801.cars.carsauctions.common.security.UserContextHolder;
import com.filip2801.cars.carsauctions.user.domain.UserService;
import com.filip2801.cars.carsauctions.user.infrastructure.dto.ChangePasswordRequest;
import com.filip2801.cars.carsauctions.user.infrastructure.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UsersController {

    private final UserService userService;

    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isFullyAuthenticated()")
    UserDto changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        var userId = UserContextHolder.getLoggedInUser().getUserId();
        return userService.changePassword(userId, changePasswordRequest.password());
    }

    @PreAuthorize("hasRole('AGENT')")
    @GetMapping
    public List<UserDto> getCarsByIds(@RequestParam List<Long> ids) {
        return userService.findAllById(ids);
    }

}
