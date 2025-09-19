# Multi-stage Dockerfile for Angular frontend + Spring Boot backend

# Stage 1: Build Angular frontend
FROM node:18-alpine AS frontend-build

WORKDIR /app/frontend

# Copy package files
COPY frontend/package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY frontend/ ./

# Build for production
RUN npm run build

# Stage 2: Build Spring Boot backend
FROM openjdk:17-jdk-slim AS backend-build

WORKDIR /app/backend

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml first (for better Docker layer caching)
COPY backend/pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY backend/src src

# Build the application
RUN mvn clean package -DskipTests

# List built files for debugging
RUN ls -la target/

# Stage 3: Runtime image with nginx + Java
FROM nginx:alpine

# Install OpenJDK 17 and curl for health checks
RUN apk add --no-cache openjdk17-jre curl

# Copy built frontend from frontend-build stage
COPY --from=frontend-build /app/frontend/dist/frontend/ /usr/share/nginx/html/

# Copy built backend JAR from backend-build stage
COPY --from=backend-build /app/backend/target/improvement-hub-backend-1.0.0.jar /app/backend.jar

# Create startup script
RUN cat > /app/start.sh << 'EOF'
#!/bin/bash

# Create nginx config with dynamic port
cat > /etc/nginx/conf.d/default.conf << NGINX_EOF
server {
    listen ${PORT:-80};
    server_name localhost;

    # Serve Angular frontend
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files \$uri \$uri/ /index.html;
    }

    # Proxy API requests to Spring Boot backend
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    # Proxy health check
    location /health {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
NGINX_EOF

# Start Spring Boot backend in background
echo "Starting Spring Boot backend..."
java -XX:+UseContainerSupport -XX:MaxRAMPercentage=50.0 -jar /app/backend.jar &

# Wait a moment for backend to start
sleep 10

# Start nginx in foreground
echo "Starting nginx..."
nginx -g "daemon off;"
EOF

RUN chmod +x /app/start.sh

# Expose port (Railway will set PORT environment variable)
EXPOSE $PORT

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${PORT:-80}/health || exit 1

# Start both services
CMD ["/app/start.sh"]