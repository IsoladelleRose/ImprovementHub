#!/bin/sh

# Set default port if PORT is not set or is empty
if [ -z "$PORT" ]; then
    PORT=8080
    echo "PORT not set, using default: $PORT"
else
    echo "Using PORT: $PORT"
fi

# Debug: Print mail environment variables
echo "=== Mail Configuration Debug ==="
echo "MAIL_HOST: ${MAIL_HOST:-not set}"
echo "MAIL_PORT: ${MAIL_PORT:-not set}"
echo "MAIL_USERNAME: ${MAIL_USERNAME:-not set}"
echo "MAIL_PASSWORD: $([ -z "$MAIL_PASSWORD" ] && echo 'not set' || echo 'is set')"
echo "================================"

# Start the Java application
exec java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dserver.port=$PORT -jar app.jar