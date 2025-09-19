# Frontend Dockerfile - Angular with nginx
FROM node:18-alpine AS build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build for production
RUN npm run build

# Production stage with nginx
FROM nginx:alpine

# Copy built app from build stage
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Create nginx configuration that proxies API calls to backend
RUN cat > /etc/nginx/conf.d/default.conf << 'EOF'
server {
    listen 80;
    server_name localhost;

    # Serve Angular app
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }

    # Proxy API calls to backend service
    location /api/ {
        proxy_pass $BACKEND_URL;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Port $server_port;
    }
}
EOF

# Create startup script to handle dynamic backend URL
RUN cat > /docker-entrypoint.sh << 'EOF'
#!/bin/sh

# Replace $BACKEND_URL in nginx config with actual environment variable
if [ ! -z "$BACKEND_URL" ]; then
    sed -i "s|\$BACKEND_URL|$BACKEND_URL|g" /etc/nginx/conf.d/default.conf
fi

# Start nginx
exec nginx -g "daemon off;"
EOF

RUN chmod +x /docker-entrypoint.sh

EXPOSE 80

CMD ["/docker-entrypoint.sh"]