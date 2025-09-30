package com.innostore.improvementhub.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        // If DATABASE_URL is provided (Railway), convert it to JDBC format
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Railway format: postgresql://user:password@host:port/dbname
            // JDBC format: jdbc:postgresql://host:port/dbname

            String jdbcUrl;
            if (databaseUrl.startsWith("postgresql://") || databaseUrl.startsWith("postgres://")) {
                jdbcUrl = databaseUrl.replace("postgresql://", "jdbc:postgresql://")
                                     .replace("postgres://", "jdbc:postgresql://");
            } else {
                jdbcUrl = databaseUrl;
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setDriverClassName("org.postgresql.Driver");

            // Connection pool settings
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            config.setConnectionTimeout(20000);
            config.setIdleTimeout(300000);
            config.setMaxLifetime(1200000);
            config.setConnectionTestQuery("SELECT 1");

            return new HikariDataSource(config);
        }

        // Fallback to default Spring Boot configuration
        return null;
    }
}
