package com.filip2801.cars.carsauctions.user.domain;

import com.filip2801.cars.carsauctions.user.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.user.infrastructure.dto.UserDto;
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
