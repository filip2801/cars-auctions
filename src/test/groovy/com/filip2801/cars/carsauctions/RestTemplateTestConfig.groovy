package com.filip2801.cars.carsauctions

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@TestConfiguration
class RestTemplateTestConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate()
    }
}

