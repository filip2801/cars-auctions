package com.filip2801.cars.carsauctions.notifications.infrastructure.messaging;

import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto.AuctionStartAlertedEvent;
import com.filip2801.cars.carsauctions.notifications.domain.Notification;
import com.filip2801.cars.carsauctions.notifications.domain.NotificationRepository;
import com.filip2801.cars.carsauctions.notifications.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuctionSubscriptionEventListener {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = NotificationQueuesConfiguration.NOTIFICATIONS_AUCTION_START_ALERTED)
    public void onAuctionStartAlertedEvent(@Payload AuctionStartAlertedEvent event) {
        log.debug("Handling {} ", event);

        Notification notification = new Notification(null, event.auctionId(), event.dealerId(), NotificationType.NEW_AUCTION);
        notificationRepository.save(notification);
    }
}
