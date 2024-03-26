package com.filip2801.cars.carsauctions.user.domain;

import com.filip2801.cars.carsauctions.common.exception.ResourceNotFoundException;
import com.filip2801.cars.carsauctions.user.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.user.infrastructure.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerDealer(UserDto userDto) {
        return registerNewUser(userDto, UserRole.DEALER);
    }

    public UserDto registerNewAgent(UserDto userDto) {
        return registerNewUser(userDto, UserRole.AGENT);
    }

    private UserDto registerNewUser(UserDto userDto, UserRole role) {
        User user = User.builder()
                .username(userDto.username())
                .password(encodePassword(userDto.password()))
                .role(role)
                .build();

        userRepository.save(user);

        return Builders.toUserDto(user);
    }

    public List<UserDto> findAllById(List<Long> ids) {
        return userRepository.findAllById(ids).stream()
                .map(Builders::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        user.updatePassword(encodePassword(password));
        userRepository.save(user);
        return Builders.toUserDto(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
