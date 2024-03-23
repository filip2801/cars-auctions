package com.filip2801.cars.carsauctions.utils;

public class Validate {

    public static void validateIsTrue(boolean value, String failureMessage){
        if (!value) {
            throw new IllegalStateException(failureMessage);
        }
    }

}
