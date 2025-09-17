# Deploy Spring Boot Backend to Railway

## Your Database Status ✅
- PostgreSQL 17 with SSL is ready
- Image: `ghcr.io/railwayapp-templates/postgres-ssl:17`

## Step 1: Deploy Backend Service

### Option A: GitHub Integration (Recommended)

1. **Push your code to GitHub** (if not already done):
   ```bash
   cd ImprovementHub
   git add .
   git commit -m "Add PostgreSQL integration and Railway configuration"
   git push origin main
   ```

2. **In Railway Dashboard**:
   - Go to your project (where you created the PostgreSQL database)
   - Click "New Service"
   - Select "GitHub Repo"
   - Choose your ImprovementHub repository
   - **Important**: Set root directory to `/backend`

3. **Railway will automatically**:
   - Detect it's a Spring Boot application
   - Build using Maven
   - Deploy your backend service

### Option B: Railway CLI

```bash
# Navigate to backend directory
cd ImprovementHub/backend

# Install Railway CLI (if not installed)
npm install -g @railway/cli

# Login to Railway
railway login

# Link to your existing project
railway link

# Deploy
railway up
```

## Step 2: Configure Environment Variables

After deployment, in your **backend service** (not database):

1. **Go to Railway Dashboard**
2. **Click on your Backend Service** (not the PostgreSQL service)
3. **Go to "Variables" tab**
4. **Add this variable**:
   - **Name**: `DATABASE_URL`
   - **Value**: `${{ Postgres.DATABASE_URL }}`

This connects your backend to your PostgreSQL database using Railway's private network.

## Step 3: Verify Deployment

### Check Logs:
1. In Railway dashboard, click on your backend service
2. Go to "Deployments" tab
3. Click on the latest deployment
4. Check logs for:
   ```
   Started ImprovementHubApplication
   ```

### Test API Endpoints:
Your backend will be available at: `https://your-backend-service.up.railway.app`

Test with:
```bash
# Replace with your actual Railway backend URL
curl https://your-backend-service.up.railway.app/api/partners

# Should return an empty array [] if working
```

## Step 4: Database Tables Auto-Creation

When your Spring Boot app starts successfully, it will automatically create:
- `partners` table
- `ideas` table
- All necessary indexes

Look for these logs:
```
Hibernate: create table partners (...)
Hibernate: create table ideas (...)
```

## Step 5: Get Your Backend URL

After successful deployment:
1. In Railway dashboard, click your backend service
2. Go to "Settings" tab
3. Copy the "Public Domain" URL
4. This is your API base URL for the Angular frontend

## Troubleshooting

### If deployment fails:
- Check that `/backend` is set as root directory
- Verify `pom.xml` is in the root of the backend folder
- Check deployment logs for errors

### If database connection fails:
- Verify `DATABASE_URL` environment variable is set
- Check that both services are in the same Railway project
- Ensure PostgreSQL service is running

### If tables aren't created:
- Check application logs for Hibernate statements
- Verify `spring.jpa.hibernate.ddl-auto=update` in properties
- Look for any SQL errors in logs

## Next Steps After Successful Deployment

1. ✅ Backend deployed and running
2. ✅ Database connected and tables created
3. 🔄 Update Angular frontend to use Railway backend URL
4. 🔄 Test complete flow: Frontend → Backend → Database
5. 🔄 Deploy Angular frontend to Railway (optional)

## Your Backend API Endpoints

Once deployed, your API will be available at:
`https://your-backend-service.up.railway.app`

### Partner Endpoints:
- `POST /api/partners/register` - Register new partner
- `GET /api/partners` - Get all partners
- `GET /api/partners/{id}` - Get partner by ID

### Idea Endpoints:
- `POST /api/ideas/register` - Register new idea
- `GET /api/ideas` - Get all ideas
- `GET /api/ideas/wants-help` - Get ideas wanting help