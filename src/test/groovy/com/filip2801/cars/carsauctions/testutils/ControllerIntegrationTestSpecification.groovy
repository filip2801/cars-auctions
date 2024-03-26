package com.filip2801.cars.carsauctions.testutils

import com.filip2801.cars.carsauctions.user.domain.User
import com.filip2801.cars.carsauctions.user.domain.UserRepository
import com.filip2801.cars.carsauctions.user.domain.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.client.RestTemplate

import static TestUtils.uniqueString

class ControllerIntegrationTestSpecification extends BaseIntegrationTestSpecification {

    @Autowired
    RestTemplate restTemplate

    @Autowired
    UserRepository userRepository
    @Autowired
    PasswordEncoder passwordEncoder

    String getBaseUrl() {
        return "http://localhost:${port}${contextPath}"
    }
    @LocalServerPort
    int port

    @Value('${server.servlet.context-path:/}')
    String contextPath

    protected UserContext userLoggedIn

    def sendGetForObject(String url) {
        restTemplate.getForEntity("${getBaseUrl()}/${url}", HashMap)
    }

    def sendGetForList(String url) {
        restTemplate.getForEntity("${getBaseUrl()}/${url}", ArrayList)
    }

    def sendPost(String url, Object requestPayload) {
        restTemplate.exchange("${getBaseUrl()}/${url}", HttpMethod.POST, entityWithHeaders(requestPayload), HashMap)
    }

    def sendPut(String url, Object requestPayload) {
        restTemplate.exchange("${getBaseUrl()}/${url}", HttpMethod.PUT, entityWithHeaders(requestPayload), HashMap)
    }

    private HttpEntity<Object> entityWithHeaders(requestPayload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (userLoggedIn != null) {
            String auth = userLoggedIn.user.getUsername() + ":" + userLoggedIn.plainTextPassword
            byte[] encodedAuth = Base64.encoder.encode(auth.bytes);
            String authHeader = "Basic " + new String(encodedAuth);
            headers.set("Authorization", authHeader)
        }

        return new HttpEntity<>(requestPayload, headers);
    }

    void loginAsUser(UserContext newUserContext) {
        userLoggedIn = newUserContext
    }

    UserContext mockAgentUser() {
        mockUser(UserRole.AGENT)
    }

    UserContext mockDealerUser() {
        mockUser(UserRole.DEALER)
    }

    private UserContext mockUser(UserRole role) {
        var plainTextPassword = uniqueString()
        var encryptedPassword = passwordEncoder.encode(plainTextPassword)
        var user = new User(null, uniqueString(), encryptedPassword, role)
        userRepository.save(user)

        userLoggedIn = new UserContext(user, plainTextPassword)
    }

    private HttpEntity<Object> entityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    Class status400() {
        org.springframework.web.client.HttpClientErrorException$BadRequest
    }

    Class status401() {
        org.springframework.web.client.HttpClientErrorException$Unauthorized
    }

    Class status403() {
        org.springframework.web.client.HttpClientErrorException$Forbidden
    }

    Class status404() {
        org.springframework.web.client.HttpClientErrorException$NotFound
    }

    class UserContext {
        User user
        String plainTextPassword

        UserContext(User user, String plainTextPassword) {
            this.user = user
            this.plainTextPassword = plainTextPassword
        }
    }

    User getLoggedInUser() {
        return userLoggedIn.user
    }
}
