package org.piazza.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatasourceConfig {

    @Value("${app.datasource.main.jdbc-url}")
    private String url;
    @Value("${app.datasource.main.driver-class-name}")
    private String driver;
    @Value("${app.datasource.main.username}")
    private String username;
    @Value("${app.datasource.main.password}")
    private String password;
    @Value("${app.datasource.main.minimum-idle}")
    private String minimumIdle;
    @Value("${app.datasource.main.maximum-pool}")
    private String maximumPool;
    @Value("${app.datasource.main.maximum-life-time}")
    private String lifeTime;
    @Value("${app.datasource.main.timeout}")
    private String timeout;

    public HikariConfig hikariConfig() {
        HikariConfig config= new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);//postgres
        config.setPassword(password);//postgres
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMinimumIdle(Integer.parseInt(minimumIdle));//10
        config.setMaximumPoolSize(Integer.parseInt(maximumPool));//40
        config.setMaxLifetime(Integer.parseInt(lifeTime));//120000
        config.setConnectionTimeout(Integer.parseInt(timeout));//60000
        config.setDriverClassName(driver);
        return config;
    }

    @Bean
    @Primary
    public HikariDataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public JdbcTemplate jdbcTemplate(HikariDataSource hikariDataSource) {
        return new JdbcTemplate(hikariDataSource);
    }
}
