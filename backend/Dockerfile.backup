# Single-stage Dockerfile for Spring Boot with Java 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Install Maven
RUN apk add --no-cache maven curl

# Copy pom.xml and download dependencies first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Show what was built (debugging)
RUN echo "=== Build Results ===" && ls -la target/

# Create startup script
RUN echo '#!/bin/sh' > /app/start.sh && \
    echo 'echo "=== Environment Variables ==="' >> /app/start.sh && \
    echo 'echo "PORT: $PORT"' >> /app/start.sh && \
    echo 'echo "DATABASE_URL: $DATABASE_URL"' >> /app/start.sh && \
    echo 'echo "=========================="' >> /app/start.sh && \
    echo 'echo "Looking for JAR files..."' >> /app/start.sh && \
    echo 'ls -la target/' >> /app/start.sh && \
    echo 'JAR_FILE=$(find target -name "*.jar" -type f | head -1)' >> /app/start.sh && \
    echo 'echo "Found JAR: $JAR_FILE"' >> /app/start.sh && \
    echo 'if [ -z "$JAR_FILE" ]; then' >> /app/start.sh && \
    echo '  echo "ERROR: No JAR file found!"' >> /app/start.sh && \
    echo '  exit 1' >> /app/start.sh && \
    echo 'fi' >> /app/start.sh && \
    echo 'exec java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT:-8080} -jar "$JAR_FILE"' >> /app/start.sh

RUN chmod +x /app/start.sh

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${PORT:-8080}/health || exit 1

EXPOSE 8080

CMD ["/app/start.sh"]