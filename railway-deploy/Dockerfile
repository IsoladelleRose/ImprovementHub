# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace
RUN apk add --no-cache maven

# Copy source files and rename them
COPY source-files/ ./
# pom.xml already exists (no longer pom.txt)
RUN mv mvnw.txt mvnw
RUN mv mvnw-cmd.txt mvnw.cmd
RUN chmod +x mvnw

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR file and startup script
COPY --from=builder /workspace/target/improvement-hub-backend-1.0.0.jar app.jar
COPY start.sh .
RUN chmod +x start.sh

# Expose port
EXPOSE 8080

# Start the application using the startup script
CMD ["./start.sh"]