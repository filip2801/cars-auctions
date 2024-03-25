package com.filip2801.cars.carsauctions.common.messaging;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
@Getter
class RabbitMqMessageToSendEvent extends ApplicationEvent {
    private final String exchange;
    private final String routingKey;
    private final Object payload;

    public RabbitMqMessageToSendEvent(Object source, String exchange, String routingKey, Object payload) {
        super(source);
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.payload = payload;
    }
}
