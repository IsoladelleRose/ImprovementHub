# Railway Deployment Guide for ImprovementHub Backend

## Prerequisites
- Railway account with PostgreSQL database already created
- Your Spring Boot backend code ready

## Step 1: Deploy Backend to Railway

### Option A: Connect GitHub Repository
1. Go to [Railway Dashboard](https://railway.app/dashboard)
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Choose your ImprovementHub repository
5. Select the `backend` folder as the root directory

### Option B: Railway CLI Deployment
```bash
# Install Railway CLI
npm install -g @railway/cli

# Login to Railway
railway login

# Navigate to your backend directory
cd ImprovementHub/backend

# Initialize Railway project
railway link

# Deploy
railway up
```

## Step 2: Configure Environment Variables

In your Railway backend service:

1. **Go to your backend service** in Railway dashboard
2. **Click on "Variables" tab**
3. **Add the following variable:**
   - **Name**: `DATABASE_URL`
   - **Value**: `${{ Postgres.DATABASE_URL }}`

This connects your backend service to your PostgreSQL database using Railway's private network.

## Step 3: Additional Environment Variables (Optional)

You can also add these for better configuration:

```
# Production settings
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080

# JPA settings for production
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
```

## Step 4: Create Production Profile (Recommended)

Create a production configuration file:

### File: `src/main/resources/application-production.properties`
```properties
# Production-specific settings
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=update
logging.level.com.innostore.improvementhub=INFO
logging.level.org.springframework.web=INFO
```

## Step 5: Update CORS for Production

After deployment, update your CORS configuration to include your Railway frontend URL:

```properties
# CORS configuration for production
management.endpoints.web.cors.allowed-origins=http://localhost:4200,https://your-frontend-app.railway.app
```

## Step 6: Verify Deployment

1. **Check deployment logs** in Railway dashboard
2. **Test API endpoints**:
   ```bash
   # Replace with your Railway backend URL
   curl https://your-backend-app.railway.app/api/partners
   ```

3. **Check database connection** - Look for successful startup logs

## Step 7: Database Schema Creation

When your Spring Boot app starts for the first time, it will automatically:
- Connect to your Railway PostgreSQL database
- Create the `partners` and `ideas` tables
- Set up all necessary indexes and constraints

## Environment Variable Configuration

Your `application.properties` is now configured to:
- Use `DATABASE_URL` environment variable when available (Railway)
- Fallback to localhost when running locally
- Handle SSL connections automatically

## Production Checklist

- [ ] Backend deployed to Railway
- [ ] DATABASE_URL environment variable set
- [ ] Database tables created automatically
- [ ] API endpoints responding correctly
- [ ] CORS configured for your frontend domain
- [ ] Logs showing successful database connection

## API Endpoints (After Deployment)

Replace `your-backend-app.railway.app` with your actual Railway backend URL:

### Partner Endpoints:
- `POST https://your-backend-app.railway.app/api/partners/register`
- `GET https://your-backend-app.railway.app/api/partners`

### Idea Endpoints:
- `POST https://your-backend-app.railway.app/api/ideas/register`
- `GET https://your-backend-app.railway.app/api/ideas`

## Next Steps

1. **Deploy your backend** using one of the methods above
2. **Set the DATABASE_URL variable** in Railway
3. **Test your API endpoints**
4. **Update your Angular frontend** to use the Railway backend URL
5. **Deploy your Angular frontend** (also can be done on Railway)

## Troubleshooting

If you encounter issues:
1. Check Railway deployment logs
2. Verify DATABASE_URL is set correctly
3. Ensure your Railway PostgreSQL service is running
4. Check that tables are being created automatically