package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI organizationServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Organization Service API")
                        .version("1.0")
                        .description("APIs for managing financial organizations"));
    }
}