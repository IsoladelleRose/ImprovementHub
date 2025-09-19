# Railway Deployment Guide - Separate Services

This guide shows how to deploy the frontend and backend as separate Railway services.

## Step 1: Deploy Backend Service

1. **Create New Railway Service**
   - Go to Railway dashboard
   - Click "New Project" → "Deploy from GitHub repo"
   - Select your `ImprovementHub` repository
   - Choose "Deploy Now"

2. **Configure Backend Service**
   - **Service Name**: `improvement-hub-backend`
   - **Root Directory**: `backend`
   - **Builder**: Docker (automatically detected from railway.json)

3. **Add Environment Variables**
   ```
   DATABASE_URL = ${{ Postgres.DATABASE_URL }}
   ```

4. **Deploy and Note the URL**
   - Deploy the service
   - Copy the Railway-provided URL (e.g., `https://improvement-hub-backend-production.up.railway.app`)

## Step 2: Deploy Frontend Service

1. **Create Another Railway Service**
   - In the same Railway project, click "New Service"
   - Connect to the same GitHub repository
   - Choose "Deploy Now"

2. **Configure Frontend Service**
   - **Service Name**: `improvement-hub-frontend`
   - **Root Directory**: `frontend`
   - **Builder**: Docker (automatically detected from railway.json)

3. **Add Environment Variables**
   ```
   BACKEND_URL = https://your-backend-service-url.up.railway.app
   ```
   Replace with the actual backend URL from Step 1.

## Step 3: Update Angular Configuration (if needed)

If you want to hardcode the backend URL in the Angular app instead of using nginx proxy:

1. **Update environment.prod.ts**:
   ```typescript
   export const environment = {
     production: true,
     apiUrl: 'https://your-backend-service-url.up.railway.app/api'
   };
   ```

2. **Update your Angular services** to use `environment.apiUrl`

## Step 4: Test the Deployment

1. **Backend Health Check**:
   ```bash
   curl https://your-backend-service-url.up.railway.app/health
   ```

2. **Frontend Access**:
   - Visit: `https://your-frontend-service-url.up.railway.app`
   - Test the application functionality

3. **API Integration**:
   - Test that frontend can communicate with backend
   - Check browser network tab for API calls

## Services Architecture

```
Frontend Service (nginx)     Backend Service (Spring Boot)
├── Angular App             ├── REST API (/api/*)
├── Static Files            ├── Health Check (/health)
└── Proxy /api/* → Backend  └── Database Connection
```

## Environment Variables Summary

### Backend Service:
- `DATABASE_URL` = `${{ Postgres.DATABASE_URL }}`

### Frontend Service:
- `BACKEND_URL` = `https://your-backend-url.up.railway.app`

## Benefits of This Approach

✅ **Simpler builds** - Each service has one responsibility
✅ **Independent scaling** - Scale frontend and backend separately
✅ **Easier debugging** - Separate logs and metrics
✅ **Better reliability** - If one service fails, the other can stay up
✅ **Standard Docker practices** - Each Dockerfile is straightforward

## Troubleshooting

### Backend Issues:
- Check DATABASE_URL is set correctly
- Verify PostgreSQL service is running
- Check health endpoint: `/health`

### Frontend Issues:
- Verify BACKEND_URL points to correct backend service
- Check nginx logs for proxy errors
- Ensure backend service is accessible

### CORS Issues:
- Backend automatically allows requests from any origin
- If issues persist, update CORS configuration in Spring Boot