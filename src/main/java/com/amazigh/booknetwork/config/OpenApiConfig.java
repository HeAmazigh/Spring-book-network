package com.amazigh.booknetwork.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Amazigh",
            email = "amazighhettal@gmail.com",
            url = "https://amazigh-ettal.com"
        ),
        description = "OpenApi documentation",
        title = "OpenApi Specification - Amazigh",
        version = "1.0",
        license = @License(
            name = "license-name",
            url = "https://license-url.com"
        ),
        termsOfService = "Terms of service"
    ),
    servers = {
        @Server(
            description = "LOCAL ENV",
            url = "http://localhost:8080/api/v1"
        ),
        @Server(
            description = "PROD ENV",
            url = "https://prod.com/api/v1"
        )
    },
    security = {
        @SecurityRequirement(
            name = "bearerAuth"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT bearer auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
