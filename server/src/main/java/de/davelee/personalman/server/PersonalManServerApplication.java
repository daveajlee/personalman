package de.davelee.personalman.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class represents the Spring Boot Application for the PersonalMan Server Application.
 * @author Dave Lee
 */
@SpringBootApplication
@ComponentScan({"de.davelee.personalman.server.rest.controllers","de.davelee.personalman.server.configuration",
"de.davelee.personalman.server.services"})
public class PersonalManServerApplication {

    public static void main ( String[] args ) {
        SpringApplication.run(PersonalManServerApplication.class, args);
    }

}
