package com.filip2801.cars.carsauctions.notifications.infrastructure;

import com.filip2801.cars.carsauctions.common.security.UserContextHolder;
import com.filip2801.cars.carsauctions.notifications.domain.NotificationRepository;
import com.filip2801.cars.carsauctions.notifications.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.notifications.infrastructure.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping
    public List<NotificationDto> getNotificationsForLoggedInUser() {
        var userId = UserContextHolder.getLoggedInUser().getUserId();

        return notificationRepository.findAllByUserId(userId).stream()
                .map(Builders::toNotificationDto)
                .collect(Collectors.toList());
    }


}
