package com.filip2801.cars.carsauctions.testutils


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@ContextConfiguration(initializers = [DbInitializer.class, RabbitMqInitializer.class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestTemplateTestConfig)
@ActiveProfiles("test")
class BaseIntegrationTestSpecification extends Specification {


    void eventually(Closure<?> conditions) {
        var pollingConditions = new PollingConditions(timeout: 10)
        pollingConditions.eventually(conditions)
    }

}
