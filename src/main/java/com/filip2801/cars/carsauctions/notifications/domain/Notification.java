package com.filip2801.cars.carsauctions.notifications.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue
    Long id;

    Long auctionId;
    Long userId;

    @Enumerated(EnumType.STRING)
    NotificationType type;
}
