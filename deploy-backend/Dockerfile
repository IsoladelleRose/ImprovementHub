# Minimal multi-stage build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /build
RUN apk add --no-cache maven
# Copy all source files directly
COPY ../backend/temp-hide/ .
RUN mvn clean package -DskipTests

# Runtime stage - extremely simple
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy any JAR file from build stage
RUN mkdir -p /app
COPY --from=build /build/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Dserver.port=${PORT:-8080}", "-jar", "/app/app.jar"]