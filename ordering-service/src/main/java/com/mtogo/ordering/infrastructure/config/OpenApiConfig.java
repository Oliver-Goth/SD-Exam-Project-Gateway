package com.mtogo.ordering.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ordering Service API",
                version = "1.0",
                description = "Ordering microservice for MTOGO (create, confirm, get orders)"
        )
)
public class OpenApiConfig {
}
