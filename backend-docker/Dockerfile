# Multi-stage build that fetches source from parent directory
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /build

# Install Maven
RUN apk add --no-cache maven

# Copy source from parent backend directory
COPY ../backend/pom.xml.bak pom.xml
COPY ../backend/src src
COPY ../backend/.mvn.bak .mvn
COPY ../backend/mvnw.bak mvnw
COPY ../backend/mvnw.cmd.bak mvnw.cmd

# Make mvnw executable and build
RUN chmod +x mvnw
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built JAR
COPY --from=build /build/target/*.jar app.jar

# Expose port and run
EXPOSE 8080
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Dserver.port=${PORT:-8080}", "-jar", "app.jar"]