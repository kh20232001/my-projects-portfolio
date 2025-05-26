package com.api.jobpal.common.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerUiConfig {

    @Bean
    public OpenAPI createRestApiWithSecurityScheme() {
        return new OpenAPI().components(new Components()
                // Add security scheme for Authorization Bearer token
                .addSecuritySchemes("basicScheme", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("jwt")))
                .addSecurityItem(new SecurityRequirement().addList("basicScheme"));
    }
}
