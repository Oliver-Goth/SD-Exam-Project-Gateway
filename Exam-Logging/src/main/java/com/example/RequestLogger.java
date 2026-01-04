package com.example;

import java.util.UUID;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


public class RequestLogger {
    private static final Logger log = LoggerFactory.getLogger(RequestLogger.class);

    public static <T> T log(
            String userId,
            String method,
            String path,
            int status,
            Supplier<T> action
    ) {
        long start = System.currentTimeMillis();
        String correlationId = UUID.randomUUID().toString();

        MDC.put("correlation_id", correlationId);
        MDC.put("user_id", userId);

        try {
            return action.get();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            log.info("Request handled method={} path={} status={} elapsed_ms={}",
                    method, path, status, elapsed);
            MDC.clear();
        }
    }

}





