package de.davelee.personalman.server;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

/**
 * This class represents the Spring Boot Application for the PersonalMan Server Application.
 * @author Dave Lee
 */
@SpringBootApplication
@ComponentScan({"de.davelee.personalman.server.rest.controllers","de.davelee.personalman.server.configuration",
"de.davelee.personalman.server.services"})
public class PersonalManServerApplication {

    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    public static void main ( String[] args ) {
        SpringApplication.run(PersonalManServerApplication.class, args);
    }

}
