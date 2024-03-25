package com.filip2801.cars.carsauctions.testutils

import com.filip2801.cars.carsauctions.common.security.CustomUserDetails
import com.filip2801.cars.carsauctions.user.domain.UserRole
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import static TestUtils.uniqueId
import static TestUtils.uniqueString

@ContextConfiguration(initializers = DbInitializer.class)
@SpringBootTest
class SimpleIntegrationTestSpecification extends Specification {

    CustomUserDetails loggedInUser

    void mockAgentUser() {
        mockUser(UserRole.AGENT)
    }

    void mockDealerUser() {
        mockUser(UserRole.DEALER)
    }

    private void mockUser(UserRole role) {
        loggedInUser = new CustomUserDetails(uniqueId(), uniqueString(), uniqueString(), role)
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loggedInUser, null)
        SecurityContextHolder.getContext().setAuthentication(authentication)
    }

    void eventually(Closure<?> conditions) {
        var pollingConditions = new PollingConditions(timeout: 10)
        pollingConditions.eventually(conditions)
    }

}
