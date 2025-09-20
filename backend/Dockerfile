# Multi-stage Docker build for Railway
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy Maven files
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Download dependencies
RUN mvn dependency:go-offline -B || true

# Copy source and build
COPY src src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy specific JAR from build stage
COPY --from=build /app/target/improvement-hub-backend-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]