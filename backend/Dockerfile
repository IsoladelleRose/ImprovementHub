# Multi-stage Docker build for Railway
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy Maven files from temp-hide directory
COPY temp-hide/pom.xml .
COPY temp-hide/.mvn .mvn
COPY temp-hide/mvnw .
COPY temp-hide/mvnw.cmd .

# Download dependencies
RUN mvn dependency:go-offline -B || true

# Copy source and build
COPY temp-hide/src src
RUN mvn clean package -DskipTests

# Debug: List target directory contents
RUN echo "=== Target directory contents ===" && ls -la target/

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Debug: List what we're copying
RUN echo "=== About to copy JAR files ==="
COPY --from=build /app/target/ ./debug-target/
RUN ls -la ./debug-target/

# Copy JAR with fallback approach
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]