package com.filip2801.cars.carsauctions.service

import com.filip2801.cars.carsauctions.dto.UserDto
import com.filip2801.cars.carsauctions.model.UserRole
import com.filip2801.cars.carsauctions.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import static com.filip2801.cars.carsauctions.TestUtils.uniqueString

class UserServiceSpec extends Specification {

    UserRepository userRepository = Mock()
    PasswordEncoder passwordEncoder = Mock()

    UserService userService = new UserService(userRepository, passwordEncoder)

    def "should register new dealer"() {
        given:
        def dealerToRegister = new UserDto(null, uniqueString(), uniqueString())

        def encodedPassword = 'encoded password'
        passwordEncoder.encode(dealerToRegister.password()) >> encodedPassword

        when:
        def registeredDealerDto = userService.registerDealer(dealerToRegister)

        then:
        registeredDealerDto.username() == dealerToRegister.username()
        !registeredDealerDto.password()

        1 * userRepository.save({
            it.username == dealerToRegister.username()
            it.password == encodedPassword
            it.role == UserRole.DEALER
        })

    }
}
