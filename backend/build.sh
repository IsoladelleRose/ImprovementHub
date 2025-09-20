#!/bin/bash

echo "Installing Java 21..."
apt-get update && apt-get install -y openjdk-21-jdk

echo "Setting JAVA_HOME..."
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "Java version:"
java -version

echo "Restoring Java project files..."
mv pom.xml.hidden pom.xml
mv mvnw.hidden mvnw
mv mvnw.cmd.hidden mvnw.cmd
mv .mvn.hidden .mvn
mv src.hidden src

echo "Building Spring Boot application..."
chmod +x mvnw
./mvnw clean package -DskipTests

echo "Build complete!"
ls -la target/