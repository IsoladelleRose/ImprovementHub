#!/bin/bash

# Build script for Railway deployment
# This builds the JAR and prepares it for Docker

echo "Building Spring Boot JAR for Railway..."

# Clean and build
mvn clean package -DskipTests

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit 1
fi

# Find the built JAR
JAR_FILE=$(find target -name "*.jar" -type f | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "No JAR file found in target directory!"
    ls -la target/
    exit 1
fi

echo "Found JAR: $JAR_FILE"

# Copy JAR to predictable location for Docker
cp "$JAR_FILE" improvement-hub-backend-1.0.0.jar

echo "JAR prepared for Docker build: improvement-hub-backend-1.0.0.jar"
ls -la improvement-hub-backend-1.0.0.jar

echo "Now you can build with: docker build -f Dockerfile.simple -t improvement-hub-backend ."