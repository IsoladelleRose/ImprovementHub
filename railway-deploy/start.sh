#!/bin/sh

# Set default port if PORT is not set or is empty
if [ -z "$PORT" ]; then
    PORT=8080
    echo "PORT not set, using default: $PORT"
else
    echo "Using PORT: $PORT"
fi

# Start the Java application
exec java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=$PORT -jar app.jar