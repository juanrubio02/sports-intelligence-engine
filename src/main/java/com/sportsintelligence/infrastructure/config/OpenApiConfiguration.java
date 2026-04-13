package com.sportsintelligence.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI sportsIntelligenceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sports Intelligence Engine API")
                        .description("API for simulating football matches and recalculating live win and draw probabilities.")
                        .version("v0.0.1")
                        .contact(new Contact().name("Juan"))
                        .license(new License().name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project repository documentation")
                        .url("https://github.com/your-username/sports-intelligence-engine"));
    }
}
