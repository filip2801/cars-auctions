package com.filip2801.cars.carsauctions.testutils

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.RabbitMQContainer

class RabbitMqInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//        var container = new RabbitMQContainer("rabbitmq:3.13.0-alpine")
        var container = new RabbitMQContainer("rabbitmq:3.13.0-management-alpine")
        container.start()

        TestPropertyValues.of(
                "spring.rabbitmq.host=${container.host}",
                "spring.rabbitmq.port=${container.getAmqpPort()}",
                "spring.rabbitmq.username=${container.getAdminUsername()}",
                "spring.rabbitmq.password=${container.getAdminPassword()}"
        ).applyTo(configurableApplicationContext.getEnvironment())
    }

}
