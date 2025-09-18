#!/bin/bash

# Find the JAR file in target directory
JAR_FILE=$(find target -name "*.jar" -type f | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "Error: No JAR file found in target directory"
    ls -la target/
    exit 1
fi

echo "Starting application with JAR: $JAR_FILE"
exec java -Dserver.port=${PORT:-8080} -jar "$JAR_FILE"