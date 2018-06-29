package de.davelee.personalman.server.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * This class configures swagger to ensure the correct api endpoints and information is shown.
 * @author Dave Lee
 */
@Configuration
@EnableSwagger2
@Profile("dev-test")
public class SwaggerConfiguration {

    @Bean
    /**
     * This method returns a Docket object containing the api information and endpoints.
     * @return a <code>Docket</code> object containing the api information and endpoints.
     */
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("personalman")
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                .paths(paths())
                .build();
    }

    /**
     * Private helper method to determine which endpoints should be documented according to their prefix.
     * @return a <code>Predicate</code> object containing the regex of the prefixes to include in the documentation.
     */
    private Predicate<String> paths() {
        return or (
                regex("/personalman.*"),
                regex("/absence.*")
        );
    }

    /**
     * Private helper method to return the api information including title, description, license etc.
     * @return a <code>ApiInfo</code> object containing the base information to show to the user.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("PersonalMan Server API")
                .description("Rest API for PersonalMan Server")
                .termsOfServiceUrl("http://www.davelee.de")
                .contact("Dave Lee")
                .license("License Info")
                .licenseUrl("http://www.davelee.de")
                .version("0.1")
                .build();
    }

}
