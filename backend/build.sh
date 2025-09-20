#!/bin/bash

echo "Installing Java 21..."
apt-get update && apt-get install -y wget gnupg

echo "Adding Eclipse Temurin repository for Java 21..."
wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add -
echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
apt-get update && apt-get install -y temurin-21-jdk

echo "Setting JAVA_HOME..."
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "Java version:"
java -version

echo "Restoring Java project files..."
mv pom.xml.hidden pom.xml
mv mvnw.hidden mvnw
mv mvnw.cmd.hidden mvnw.cmd
mv .mvn.hidden .mvn
mv src.hidden src

echo "Installing Maven..."
apt-get install -y maven

echo "Building Spring Boot application..."
mvn clean package -DskipTests

echo "Build complete!"
ls -la target/