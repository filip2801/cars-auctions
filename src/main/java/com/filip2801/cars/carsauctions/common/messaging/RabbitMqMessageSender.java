package com.filip2801.cars.carsauctions.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
class RabbitMqMessageSender {

    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onRabbitMqMessageToSendEvent(RabbitMqMessageToSendEvent event) {
        log.debug("Sending event: {}, ", event);
        rabbitTemplate.convertAndSend(event.getExchange(), event.getRoutingKey(), event.getPayload());
    }
}