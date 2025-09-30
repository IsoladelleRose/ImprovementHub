#!/bin/sh

# Set default port if PORT is not set or is empty
if [ -z "$PORT" ]; then
    PORT=8080
    echo "PORT not set, using default: $PORT"
else
    echo "Using PORT: $PORT"
fi

# Debug: Print all environment variables
echo "=== Environment Variables Debug ==="
env | sort
echo "================================"

# Start the Java application with explicit environment variable passing
exec java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 \
  -Dserver.port=$PORT \
  -DMAIL_HOST="${MAIL_HOST}" \
  -DMAIL_PORT="${MAIL_PORT}" \
  -DMAIL_USERNAME="${MAIL_USERNAME}" \
  -DMAIL_PASSWORD="${MAIL_PASSWORD}" \
  -jar app.jar