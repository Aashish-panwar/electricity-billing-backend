package com.electricity.electricity_billing_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI electricityBillingAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                // Railway HTTPS URL
                .servers(List.of(
                        new Server()
                                .url("https://electricity-billing-backend-production.up.railway.app")
                                .description("Production Server")
                ))

                .info(
                        new Info()
                                .title("Electricity Billing System API")
                                .version("1.0")
                                .description("REST APIs for Electricity Billing System")
                                .contact(
                                        new Contact()
                                                .name("Ashish Panwar")
                                                .email("ashish@example.com")
                                )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                .schemaRequirement(
                        securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}
