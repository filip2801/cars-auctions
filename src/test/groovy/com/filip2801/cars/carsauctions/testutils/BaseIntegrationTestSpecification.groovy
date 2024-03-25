package com.filip2801.cars.carsauctions.testutils


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(initializers = [DbInitializer.class, RabbitMqInitializer.class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestTemplateTestConfig)
@ActiveProfiles("test")
class BaseIntegrationTestSpecification extends Specification {

}
