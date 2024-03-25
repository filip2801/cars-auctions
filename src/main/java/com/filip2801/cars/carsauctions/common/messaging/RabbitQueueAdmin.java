package com.filip2801.cars.carsauctions.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitQueueAdmin {

    private final RabbitAdmin rabbitAdmin;

    public void createDirectExchangeAndQueue(String queueName, String exchangeName, String routingKey) {
        DirectExchange directExchange = new DirectExchange(exchangeName);

        String dlqName = queueName + ".dlq";
        Queue queue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", dlqName)
                .build();
        Queue dlq = QueueBuilder.durable(dlqName).build();
        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(routingKey);

        rabbitAdmin.declareExchange(directExchange);
        rabbitAdmin.declareQueue(dlq);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
    }

}
