package com.filip2801.cars.carsauctions.user.infrastructure.dto;

import com.filip2801.cars.carsauctions.user.domain.User;

public class Builders {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), null);
    }

}
