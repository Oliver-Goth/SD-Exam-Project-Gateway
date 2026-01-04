package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
//ændres kun hvis der er tale om format eller niveau
public class AuditLogger {
    private static final Logger audit = LoggerFactory.getLogger("AUDIT");

    public static void log(String action, String userId, String resourceId, String ip) {
        audit.info("{{\"action\":\"{}\",\"user_id\":\"{}\",\"resource_id\":\"{}\",\"ip\":\"{}\",\"correlation_id\":\"{}\"}}",
                   action, userId, resourceId, ip, MDC.get("correlation_id"));
    }
}
