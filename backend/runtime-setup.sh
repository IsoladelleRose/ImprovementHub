#!/bin/bash

echo "Installing Java 21 for runtime..."
apt-get update
apt-get install -y wget gnupg

echo "Adding Eclipse Temurin repository..."
wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add -
echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
apt-get update
apt-get install -y temurin-21-jdk

echo "Setting up Java environment..."
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "Java installed successfully:"
$JAVA_HOME/bin/java -version

echo "Creating java symlink..."
ln -sf $JAVA_HOME/bin/java /usr/local/bin/java

echo "Verifying java command..."
which java
java -version

echo "Starting Node.js server with Java environment..."
JAVA_HOME=$JAVA_HOME PATH=$PATH node server.js