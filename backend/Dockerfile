# Backend Dockerfile - Spring Boot
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Debug: Show built files
RUN echo "Built JAR files:" && ls -la target/

# Production stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Create startup script
RUN cat > /app/start.sh << 'EOF'
#!/bin/bash

# Show environment for debugging
echo "=== Environment Variables ==="
echo "PORT: $PORT"
echo "DATABASE_URL: $DATABASE_URL"
echo "=========================="

# Start Spring Boot application
exec java \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -Dserver.port=${PORT:-8080} \
    -jar app.jar
EOF

RUN chmod +x /app/start.sh

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${PORT:-8080}/health || exit 1

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

CMD ["/app/start.sh"]