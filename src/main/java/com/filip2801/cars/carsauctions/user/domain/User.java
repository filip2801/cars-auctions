package com.filip2801.cars.carsauctions.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", allocationSize = 1)
    Long id;

    String username;
    String password;

    @Enumerated(EnumType.STRING)
    UserRole role;

    public void updatePassword(String password) {
        this.password = password;
    }
}
