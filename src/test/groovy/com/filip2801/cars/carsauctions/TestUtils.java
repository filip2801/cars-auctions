package com.filip2801.cars.carsauctions;

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
}
