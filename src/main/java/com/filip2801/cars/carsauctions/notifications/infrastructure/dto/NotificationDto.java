package com.filip2801.cars.carsauctions.notifications.infrastructure.dto;

import com.filip2801.cars.carsauctions.notifications.domain.NotificationType;

public record NotificationDto(
        Long id,
        Long auctionId,
        Long userId,
        NotificationType type
) {
}
