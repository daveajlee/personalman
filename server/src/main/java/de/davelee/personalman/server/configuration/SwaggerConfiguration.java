package de.davelee.personalman.server.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This class configures swagger to ensure the correct api endpoints and information is shown.
 * @author Dave Lee
 */
@Configuration
@Profile("dev-test")
public class SwaggerConfiguration {

    @Bean
    public OpenAPI personalManOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("PersonalMan API")
                        .description("PersonalMan provides absence management for employees or volunteers. It is designed as a server application with a RESTful API which any client can then implement in any programming language.")
                        .version("v1.2.1")
                        .license(new License().name("Apache 2.0").url("https://www.davelee.de")))
                .externalDocs(new ExternalDocumentation()
                        .description("TraMS Documentation")
                        .url("https://www.davelee.de/trams/"));
    }

    /**
     * This method returns a GroupedOpenApi object containing the api information and endpoints.
     * @return a <code>GroupedOpenApi</code> object containing the api information and endpoints.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("personalman")
                .pathsToMatch("/api/**")
                .build();
    }

}
