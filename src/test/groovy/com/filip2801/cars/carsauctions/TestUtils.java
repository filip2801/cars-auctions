package com.filip2801.cars.carsauctions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestUtils {

    private static final AtomicLong ID_SEQ = new AtomicLong(1_000);

    public static Long uniqueId() {
        return ID_SEQ.getAndIncrement();
    }

    public static String uniqueString() {
        return UUID.randomUUID().toString();
    }

    public static boolean isDateCloseToNow(LocalDateTime date, int maxDifferenceMillis) {
        return isDateCloseTo(date, LocalDateTime.now(), maxDifferenceMillis);
    }

    public static boolean isDateCloseTo(LocalDateTime date, LocalDateTime closeTo, int maxDifferenceMillis) {
        var duration = Duration.between(date, closeTo);
        return duration.compareTo(Duration.ofMillis(maxDifferenceMillis)) < 0;
    }

}
