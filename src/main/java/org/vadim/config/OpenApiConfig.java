package org.vadim.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private final String SECURITY_SCHEME = "bearer";
    private final String BEARER_FORMAT = "JWT";
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String SHEMA_REQUIREMENT_NAME = "bearerAuth";

    @Bean
    public OpenAPI getOpenAPI(){
        SecurityScheme jwtScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(SECURITY_SCHEME)
                .bearerFormat(BEARER_FORMAT)
                .name(AUTHORIZATION_HEADER)
                .in(SecurityScheme.In.HEADER);
        return new OpenAPI()
                .schemaRequirement(SHEMA_REQUIREMENT_NAME, jwtScheme);
    }
}
