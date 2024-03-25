package com.filip2801.cars.carsauctions.common;

public class Validate {

    public static void validateIsTrue(boolean value, String failureMessage) {
        if (!value) {
            throw new IllegalStateException(failureMessage);
        }
    }

}
