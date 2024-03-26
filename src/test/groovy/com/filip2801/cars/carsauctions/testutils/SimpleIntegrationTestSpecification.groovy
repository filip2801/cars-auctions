package com.filip2801.cars.carsauctions.testutils

import com.filip2801.cars.carsauctions.common.security.CustomUserDetails
import com.filip2801.cars.carsauctions.user.domain.UserRole
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import static TestUtils.uniqueId
import static TestUtils.uniqueString

class SimpleIntegrationTestSpecification extends BaseIntegrationTestSpecification {

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

}
