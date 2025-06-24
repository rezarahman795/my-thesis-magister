package com.ubl.tesis.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

/**
 * @author RR
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "REST API TESIS", version = "1.0",
                description = "Documentation REST API TESIS"),
        security = @SecurityRequirement(name = "X-API-KEY")
)
@SecuritySchemes(
        @SecurityScheme(name = "X-API-KEY", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, scheme = "apiKeyScheme")
)
public class OpenApiConfig {
}
