# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace
RUN apk add --no-cache maven curl

# Copy project files with renamed extensions to avoid detection
COPY source-files/ ./

# Rename files back to proper names
RUN mv pom.txt pom.xml && \
    mv mvnw.txt mvnw && \
    mv mvnw-cmd.txt mvnw.cmd && \
    chmod +x mvnw

# Build the JAR
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR file
COPY --from=builder /workspace/target/improvement-hub-backend-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${PORT:-8080}/health || exit 1

# Run the application
CMD ["java", "-server", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]