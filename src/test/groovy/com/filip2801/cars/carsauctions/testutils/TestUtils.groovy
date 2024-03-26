package com.filip2801.cars.carsauctions.testutils

import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

class TestUtils {

    private static final AtomicLong ID_SEQ = new AtomicLong(1_000)

    static Long uniqueId() {
        return ID_SEQ.getAndIncrement()
    }

    static String uniqueString() {
        return UUID.randomUUID().toString()
    }

    static boolean isDateCloseToNow(LocalDateTime date, int maxDifferenceMillis) {
        return isDateCloseTo(date, LocalDateTime.now(), maxDifferenceMillis)
    }

    static boolean isDateCloseTo(LocalDateTime date, LocalDateTime closeTo, int maxDifferenceMillis) {
        var duration = Duration.between(date, closeTo)
        return duration.compareTo(Duration.ofMillis(maxDifferenceMillis)) < 0
    }

}
