package com.filip2801.cars.carsauctions.common.messaging;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public record EventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {

    public void publishEvent(Object event, String exchangeName, String routingKey) {
        applicationEventPublisher.publishEvent(new RabbitMqMessageToSendEvent(this, exchangeName, routingKey, event));
    }

}
