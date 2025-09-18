# Railway Deployment Troubleshooting Guide

## Current Status
✅ Maven wrapper files added
✅ PostgreSQL database created
✅ Code pushed to GitHub
❌ Build still failing

## Required Environment Variables for Railway

### In your Backend Service (NOT the database):

**Required:**
- `DATABASE_URL` = `${{ Postgres.DATABASE_URL }}`

**Optional but Recommended:**
- `SPRING_PROFILES_ACTIVE` = `production`
- `JAVA_OPTS` = `-Xmx512m -Xms256m`

## Step-by-Step Setup

### 1. Verify Service Configuration
- **Root Directory**: Must be exactly `backend` (not `/backend`)
- **Branch**: `main`
- **Auto-Deploy**: Should be enabled

### 2. Check Build Logs
Common error patterns and solutions:

#### Error: "mvnw: Permission denied"
**Solution**: Files already fixed, try redeploy

#### Error: "Could not find pom.xml"
**Solution**: Verify root directory is set to `backend`

#### Error: "Failed to connect to database"
**Solution**: Add `DATABASE_URL` environment variable

#### Error: "Port already in use"
**Solution**: Railway handles ports automatically, no action needed

#### Error: "OutOfMemoryError"
**Solution**: Add `JAVA_OPTS=-Xmx512m` environment variable

### 3. Environment Variables Setup

Go to your **Backend Service** (not PostgreSQL):
1. Click "Variables" tab
2. Add these variables:

```
DATABASE_URL = ${{ Postgres.DATABASE_URL }}
SPRING_PROFILES_ACTIVE = production
JAVA_OPTS = -Xmx512m -Xms256m
```

### 4. Alternative: Docker Deployment

If NIXPACKS continues to fail, try Docker:

1. In Railway service settings:
   - **Builder**: Docker
   - **Dockerfile Path**: `Dockerfile`

2. Redeploy the service

### 5. Test Deployment Success

Once deployed, test these endpoints:
```bash
# Health check
curl https://your-service.up.railway.app/actuator/health

# API test
curl https://your-service.up.railway.app/api/partners
```

## Common Build Error Solutions

### Memory Issues
Add environment variable:
```
JAVA_OPTS=-Xmx512m -Xms256m
```

### Database Connection Issues
1. Verify `DATABASE_URL` is set correctly
2. Check PostgreSQL service is running
3. Ensure both services are in same Railway project

### Build Timeout
Railway builds can take 5-10 minutes for first deployment. Wait patiently.

### Java Version Issues
Our setup uses Java 17. If errors about Java version:
1. Check pom.xml has `<java.version>17</java.version>`
2. Verify Spring Boot version 3.2.0 (supports Java 17)

## Debug Information

### Check These in Railway Dashboard:

1. **Service Overview**:
   - Status should be "Active"
   - Last deployment should be "Success"

2. **Deployments Tab**:
   - Click latest deployment
   - Check build logs for errors
   - Check runtime logs for startup issues

3. **Variables Tab**:
   - Verify `DATABASE_URL` is present
   - Value should be `${{ Postgres.DATABASE_URL }}`

4. **Settings Tab**:
   - Root Directory: `backend`
   - Auto-Deploy: ON
   - Branch: `main`

### What to Share for Help:

If still having issues, share:
1. **Build error logs** from Railway dashboard
2. **Runtime error logs** (if build succeeds but app fails to start)
3. **Current environment variables** in the backend service
4. **Service settings** (root directory, branch, etc.)

## Alternative Solutions

### Option 1: Use Railway CLI
```bash
cd ImprovementHub/backend
railway login
railway link [your-project-id]
railway up
```

### Option 2: Manual Environment Variable
Instead of `${{ Postgres.DATABASE_URL }}`, you can:
1. Go to PostgreSQL service
2. Copy the actual DATABASE_URL value
3. Paste it directly in backend service variables

### Option 3: Different Builder
Try changing builder in service settings:
- NIXPACKS (default)
- Docker (if you have Dockerfile)
- Heroku Buildpacks

## Success Indicators

✅ Build completes without errors
✅ Application starts (shows in logs: "Started ImprovementHubApplication")
✅ Health check responds: `/actuator/health`
✅ Database tables are created automatically
✅ API endpoints respond: `/api/partners`

## Next Steps After Success

1. Get your Railway backend URL
2. Update Angular frontend to use Railway API
3. Test complete flow: Frontend → Backend → Database
4. Deploy frontend to Railway (optional)