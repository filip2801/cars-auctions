package com.filip2801.cars.carsauctions.service;

import com.filip2801.cars.carsauctions.dto.Builders;
import com.filip2801.cars.carsauctions.dto.UserDto;
import com.filip2801.cars.carsauctions.model.User;
import com.filip2801.cars.carsauctions.model.UserRole;
import com.filip2801.cars.carsauctions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerDealer(UserDto userDto) {
        User user = User.builder()
                .username(userDto.username())
                .password(passwordEncoder.encode(userDto.password()))
                .role(UserRole.DEALER)
                .build();

        userRepository.save(user);

        return Builders.toUserDto(user);
    }
}
