package com.takemypet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI takeMyPetOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TakeMyPet API")
                        .description("""
                                REST API for the TakeMyPet pet-sitting platform.

                                Connects **pet owners** (Proprietario) with **pet sitters** (PetSitter).
                                Supports user registration, announcements, events, ratings, and reports.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TakeMyPet Team")
                                .email("takemypetapp@gmail.com"))
                        .license(new License().name("MIT")))
                .servers(List.of(
                        new Server().url("/").description("Current server")));
    }
}
