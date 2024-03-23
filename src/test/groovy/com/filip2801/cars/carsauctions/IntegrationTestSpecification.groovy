package com.filip2801.cars.carsauctions

import com.filip2801.cars.carsauctions.model.User
import com.filip2801.cars.carsauctions.model.UserRole
import com.filip2801.cars.carsauctions.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static com.filip2801.cars.carsauctions.TestUtils.uniqueString

@ContextConfiguration(initializers = DbInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestTemplateTestConfig)
class IntegrationTestSpecification extends Specification {

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

    private User userLoggedIn
    private String userPlainPassword

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
            String auth = userLoggedIn.getUsername() + ":" + userPlainPassword
            byte[] encodedAuth = Base64.encoder.encode(auth.bytes);
            String authHeader = "Basic " + new String(encodedAuth);
            headers.set("Authorization", authHeader)
        }

        return new HttpEntity<>(requestPayload, headers);
    }

    void mockAgentUser() {
        mockUser(UserRole.AGENT)
    }

    void mockDealerUser() {
        mockUser(UserRole.DEALER)
    }

    private void mockUser(UserRole role) {
        userPlainPassword = uniqueString()
        var encryptedPassword = passwordEncoder.encode(userPlainPassword)
        userLoggedIn = new User(null, uniqueString(), encryptedPassword, role)
        userRepository.save(userLoggedIn)
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

}
