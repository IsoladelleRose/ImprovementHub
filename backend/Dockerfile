# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml first (for better Docker layer caching)
COPY pom.xml ./

# Download dependencies (this will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code and startup script
COPY src src
COPY start.sh ./

# Make startup script executable
RUN chmod +x start.sh

# Build the application
RUN mvn clean package -DskipTests

# List files to verify JAR was created
RUN ls -la target/

# Expose port
EXPOSE 8080

# Run the application using startup script
CMD ["bash", "-c", "JAR_FILE=$(find target -name '*.jar' -type f | head -1); echo 'Starting with JAR: $JAR_FILE'; java -Dserver.port=${PORT:-8080} -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar \"$JAR_FILE\""]