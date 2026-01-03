package com.example;

import org.slf4j.Logger;

import java.util.function.Supplier;

public class TimeIt {

    public static <T> T info(Logger log, String taskName, Supplier<T> action) {
        long start = System.currentTimeMillis();
        try {
            return action.get();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            log.info("{} completed in {} ms", taskName, elapsed);
        }
    }
}


