package com.filip2801.cars.carsauctions.web.security;

import com.filip2801.cars.carsauctions.model.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class CustomUserDetails extends User {

    private Long userId;
    private UserRole role;

    public CustomUserDetails(Long userId, String username, String password, UserRole role) {
        super(username, password, getAuthorities(role));
        this.userId = userId;
        this.role = role;
    }

    public boolean isDealer() {
        return role == UserRole.DEALER;
    }

    public Long getUserId() {
        return userId;
    }

    private static List<SimpleGrantedAuthority> getAuthorities(UserRole userRole) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

}
