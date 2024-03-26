package com.filip2801.cars.carsauctions.user.infrastructure;

import com.filip2801.cars.carsauctions.user.domain.UserRepository;
import com.filip2801.cars.carsauctions.user.domain.UserService;
import com.filip2801.cars.carsauctions.user.infrastructure.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UsersInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (userRepository.findAll().isEmpty()) {
            var user = new UserDto(null, "admin", "admin");
            userService.registerNewAgent(user);

            log.info("First agent user created");
        }
    }
}
