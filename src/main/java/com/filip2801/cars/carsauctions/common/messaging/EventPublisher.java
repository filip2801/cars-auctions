package com.filip2801.cars.carsauctions.common.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record EventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {

    public void publishEvent(Object event, String exchangeName, String routingKey) {
        log.debug("Sending {} to exchange {} with routing key {}", event, exchangeName, routingKey);
        applicationEventPublisher.publishEvent(new RabbitMqMessageToSendEvent(this, exchangeName, routingKey, event));
    }

}
