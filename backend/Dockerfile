# Backend Dockerfile - Spring Boot
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Install Maven (Alpine uses apk instead of apt-get)
RUN apk add --no-cache maven

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
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Create startup script
RUN echo '#!/bin/bash' > /app/start.sh && \
    echo 'echo "=== Environment Variables ==="' >> /app/start.sh && \
    echo 'echo "PORT: $PORT"' >> /app/start.sh && \
    echo 'echo "DATABASE_URL: $DATABASE_URL"' >> /app/start.sh && \
    echo 'echo "=========================="' >> /app/start.sh && \
    echo 'exec java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+EnableDynamicAgentLoading -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Dserver.port=${PORT:-8080} -jar app.jar' >> /app/start.sh

RUN chmod +x /app/start.sh

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${PORT:-8080}/health || exit 1

# Install curl for health checks (Alpine uses apk)
RUN apk add --no-cache curl

CMD ["/app/start.sh"]