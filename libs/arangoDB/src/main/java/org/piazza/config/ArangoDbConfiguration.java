package org.piazza.config;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "org.piazza")
@EnableArangoRepositories(basePackages = "org.piazza.repository")
public class ArangoDbConfiguration implements ArangoConfiguration {

    @Value("${kafka.arangodb.host}")
    private String host;
    @Value("${kafka.arangodb.port}")
    private String port;
    @Value("${kafka.arangodb.user}")
    private String user;
    @Value("${kafka.arangodb.maxConnections}")
    private String maxConnections;

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .host(host, Integer.parseInt(port))
                .password(null)
                .maxConnections(Integer.parseInt(maxConnections));
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ArangoDB arangoDB() {
        ArangoDB.Builder builder = arango();
        return builder.build();
    }

    @Override
    public String database() {
        return "piazza";
    }
}