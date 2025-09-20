# Multi-stage Docker build - pulls source from parent directory
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy Maven files from backend directory
COPY ../backend/temp-hide/pom.xml .
COPY ../backend/temp-hide/.mvn .mvn
COPY ../backend/temp-hide/mvnw .
COPY ../backend/temp-hide/mvnw.cmd .

# Download dependencies
RUN mvn dependency:go-offline -B || true

# Copy source and build
COPY ../backend/temp-hide/src src
RUN mvn clean package -DskipTests

# Debug: List target directory contents
RUN echo "=== Target directory contents ===" && ls -la target/

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy JAR with wildcard (should work in COPY)
COPY --from=build /app/target/*.jar app.jar

# Debug: Verify JAR is copied
RUN ls -la app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]