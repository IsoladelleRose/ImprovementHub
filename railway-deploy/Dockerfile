# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace
RUN apk add --no-cache maven

# Copy source files and rename them
COPY source-files/ ./
RUN mv pom.txt pom.xml
RUN mv mvnw.txt mvnw
RUN mv mvnw-cmd.txt mvnw.cmd
RUN chmod +x mvnw

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR file
COPY --from=builder /workspace/target/improvement-hub-backend-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Start the application
CMD ["sh", "-c", "java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT:-8080} -jar app.jar"]