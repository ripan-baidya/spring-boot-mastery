package in.ripanbaidya.openapidoc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI config() {
        return new OpenAPI()
                // Application Metadata, This will appear on the top of the documentation page
                .info(new Info()
                        .title("My REST API")
                        .version("1.0.0")
                        .description("""
                                This is the official backend API documentation for my application.
                                It covers all modules like User, Auth, Payments, and Admin.
                                """
                        )
                        // Contact Information
                        .contact(new Contact()
                                .name("Ripan Baidya")
                                .email("baidya.ripan024@gmail.com")
                                .url("https://github.com/ripan-baidya")
                        )
                        .termsOfService("https://smartbear.com/terms-of-use/")
                        // Licence-related information
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org/")
                        )
                )

                // External Documentation
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs")
                )

                // Supported Servers
                .servers(List.of(
                        new Server().description("Localhost").url("http://localhost:8080"),
                        new Server().description("Production").url("https://springshop.com"),
                        new Server().description("Staging").url("https://staging.springshop.com")
                ))

                // Security Requirement
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )

                // API grouping (Tags)
                .tags(List.of(
                        new Tag().name("Users").description("User related API's - Config"),
                        new Tag().name("Payments").description("Payment related API's - Config"),
                        new Tag().name("Admins").description("Admin related API's - Config")
                ));
    }
}
