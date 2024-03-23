package com.filip2801.cars.carsauctions.repository;

import com.filip2801.cars.carsauctions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

}
