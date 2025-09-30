package com.innostore.improvementhub.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {

    @Bean
    public FlywayMigrationStrategy repairStrategy() {
        return flyway -> {
            // Repair the Flyway schema history to fix failed migrations
            flyway.repair();
            // Then run migrations
            flyway.migrate();
        };
    }
}
