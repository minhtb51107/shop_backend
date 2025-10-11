package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shop Backend API")
                        .version("1.0.0")
                        .description("API documentation for Shop Backend Supply Chain Management System")
                        .contact(new Contact()
                                .name("Shop Backend Team")
                                .email("admin@shop.com")));
    }
}
