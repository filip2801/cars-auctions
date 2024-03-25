package com.filip2801.cars.carsauctions.common.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserContextHolder {

    public static CustomUserDetails getLoggedInUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("User is not logged in");
        }

        var principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        } else {
            throw new IllegalStateException("User details is type of " + principal.getClass().getSimpleName());
        }
    }
}
