package com.mtogo.delivery.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Delivery Service API",
                version = "1.0",
                description = "Delivery microservice for MTOGO"
        )
)
public class OpenApiConfig {
}
