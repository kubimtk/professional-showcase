package com.wolfgang.showcase.appointment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI appointmentShowcaseOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Appointment Backend Showcase API")
                        .description("Compact recruiter-facing backend example with validation, business rules and tests.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Wolfgang Kubisiak")
                                .url("https://www.linkedin.com/in/wolfgang-kubisiak-55746b46"))
                        .license(new License()
                                .name("Showcase code")
                                .url("https://github.com")));
    }
}
