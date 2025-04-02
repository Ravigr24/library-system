package com.gostack.lowes.lowesAssignment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Library API")
                        .description("Spring Boot library application")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
