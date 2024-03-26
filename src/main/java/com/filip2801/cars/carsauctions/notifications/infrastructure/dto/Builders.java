package com.filip2801.cars.carsauctions.notifications.infrastructure.dto;

import com.filip2801.cars.carsauctions.notifications.domain.Notification;

public class Builders {

    public static NotificationDto toNotificationDto(Notification notification) {
        return new NotificationDto(notification.getId(), notification.getAuctionId(), notification.getUserId(), notification.getType());
    }
}
