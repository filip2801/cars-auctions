package com.filip2801.cars.carsauctions.testutils

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class DbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        def postgresContainer = new PostgreSQLContainer<>("postgres:16.2-alpine")
                .withDatabaseName("cars_auctions");
        postgresContainer.start();

        TestPropertyValues.of(
                "spring.datasource.url=${postgresContainer.jdbcUrl}",
                "spring.datasource.username=${postgresContainer.username}",
                "spring.datasource.password=${postgresContainer.password}"
        ).applyTo(configurableApplicationContext.getEnvironment())
    }

}
