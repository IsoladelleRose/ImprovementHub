#!/bin/bash

echo "Building JAR locally..."
cd ../backend/temp-hide

# Try to build with system Maven if available
if command -v mvn &> /dev/null; then
    echo "Using system Maven..."
    mvn clean package -DskipTests

    # Copy JAR to deploy directory
    if [ -f target/*.jar ]; then
        cp target/*.jar ../../deploy-backend/app.jar
        echo "JAR copied to deploy-backend/app.jar"
        ls -la ../../deploy-backend/app.jar
    else
        echo "No JAR found in target directory"
        ls -la target/
    fi
else
    echo "Maven not available - using Docker to build JAR..."
    docker run --rm -v $(pwd):/app -w /app eclipse-temurin:21-jdk-alpine sh -c "
        apk add --no-cache maven &&
        mvn clean package -DskipTests &&
        ls -la target/
    "

    # Copy JAR if built successfully
    if [ -f target/*.jar ]; then
        cp target/*.jar ../../deploy-backend/app.jar
        echo "JAR copied to deploy-backend/app.jar"
    fi
fi