package de.davelee.personalman.server.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configure the mongo database which is used to store all data in PersonalMan.
 * @author Dave Lee
 */
@Configuration
@EnableMongoRepositories(basePackages = "de.davelee.personalman.server.repository")
public class MongoConfiguration {

    @Value("${mongo.url}")
    private String mongoUrl;

    @Value("${mongo.databaseName}")
    private String mongoDatabaseName;

    /**
     * Create a new client which can communicate with the Mongo Database. The url is provided as a parameter in the Spring
     * Boot application properties.
     * @return a <code>MongoClient</code> object containing the connection to the Mongo Database.
     */
    @Bean
    public MongoClient mongo() {
        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUrl))
                .build());
    }

    /**
     * Create a new template which can be used for querying the database. The name of the database is provided as a parameter
     * in the Sprint Boot application properties.
     * @throws Exception <code>Exception</code> object if not able to connect to the database.
     * @return a <code>MongoTemplate</code> object containing the template for querying the database.
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), mongoDatabaseName);
    }
}