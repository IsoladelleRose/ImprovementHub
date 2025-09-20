#!/bin/bash

echo "Installing Java 21..."
apt-get update && apt-get install -y openjdk-21-jdk

echo "Setting JAVA_HOME..."
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "Java version:"
java -version

echo "Building Spring Boot application..."
chmod +x mvnw
./mvnw clean package -DskipTests

echo "Build complete!"
ls -la target/