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
        // If DATABASE_URL is provided (Railway), parse and convert it
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            // Railway format: postgresql://user:password@host:port/dbname
            // JDBC format: jdbc:postgresql://host:port/dbname

            try {
                java.net.URI dbUri = new java.net.URI(databaseUrl);

                String username = null;
                String password = null;
                if (dbUri.getUserInfo() != null) {
                    String[] userInfo = dbUri.getUserInfo().split(":");
                    username = userInfo[0];
                    password = userInfo.length > 1 ? userInfo[1] : "";
                }

                String jdbcUrl = String.format("jdbc:postgresql://%s:%d%s",
                    dbUri.getHost(),
                    dbUri.getPort(),
                    dbUri.getPath());

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("org.postgresql.Driver");

                // Connection pool settings
                config.setMaximumPoolSize(5);
                config.setMinimumIdle(1);
                config.setConnectionTimeout(20000);
                config.setIdleTimeout(300000);
                config.setMaxLifetime(1200000);
                config.setConnectionTestQuery("SELECT 1");

                return new HikariDataSource(config);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse DATABASE_URL: " + e.getMessage(), e);
            }
        }

        // Fallback to default Spring Boot configuration
        return null;
    }
}
