package com.innostore.improvementhub.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        System.out.println("=== DATABASE CONFIG DEBUG ===");
        System.out.println("DATABASE_URL environment variable: " + databaseUrl);
        System.out.println("============================");

        if (databaseUrl != null && (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            // Railway provides postgres:// but Spring Boot needs postgresql://
            databaseUrl = databaseUrl.replace("postgres://", "postgresql://");

            try {
                URI uri = new URI(databaseUrl);
                String username = uri.getUserInfo().split(":")[0];
                String password = uri.getUserInfo().split(":")[1];
                String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();

                return DataSourceBuilder.create()
                        .driverClassName("org.postgresql.Driver")
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .build();

            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid DATABASE_URL format", e);
            }
        }

        // Fallback to default configuration for local development
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/improvement_hub")
                .username("postgres")
                .password("password")
                .build();
    }
}