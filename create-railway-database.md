# Creating PostgreSQL Database on Railway

## Method 1: Railway Dashboard (Recommended)

1. **Login to Railway**: https://railway.app
2. **Create/Select Project**:
   - Click "New Project" or select existing project
   - Give it a name like "ImprovementHub"

3. **Add PostgreSQL Database**:
   - Click "New Service" or "+"
   - Select "Database"
   - Choose "PostgreSQL"
   - Railway automatically provisions the database

4. **Get Database Details**:
   - Click on your PostgreSQL service
   - Go to "Connect" tab
   - You'll see connection details and environment variables

## Method 2: Railway CLI

```bash
# Install Railway CLI (if not already installed)
npm install -g @railway/cli

# Login to Railway
railway login

# Create new project
railway new

# Add PostgreSQL database
railway add --database postgresql
```

## Step 2: Verify Database Creation

After creating the database, you should see:

### In Railway Dashboard:
- PostgreSQL service running
- Environment variables available:
  - `DATABASE_URL`
  - `PGDATABASE`
  - `PGHOST`
  - `PGPASSWORD`
  - `PGPORT`
  - `PGUSER`

### Connection String Format:
```
postgresql://username:password@host:port/database
```

## Step 3: Test Database Connection (Optional)

You can test the connection using psql:

```bash
# Connect using the DATABASE_URL from Railway
psql "your-database-url-from-railway"

# Or connect with individual parameters
psql -h hostname -p port -U username -d database
```

## Step 4: Database Schema

Once connected, the database will be empty initially. When you deploy your Spring Boot application, it will automatically create these tables:

- `partners` - Store partner registration data
- `ideas` - Store idea submissions

## Next Steps After Database Creation

1. **Note down the DATABASE_URL** from Railway dashboard
2. **Deploy your Spring Boot backend** to Railway
3. **Set the DATABASE_URL environment variable** in your backend service
4. **Your app will automatically create the required tables**

## Troubleshooting

### If database creation fails:
- Check your Railway account limits
- Verify you have sufficient credits/plan
- Try refreshing the Railway dashboard

### If connection fails:
- Verify the DATABASE_URL format
- Check that the database service is running
- Ensure your application has the correct environment variable

## Database Configuration Details

Your database will be configured with:
- **Engine**: PostgreSQL (latest stable version)
- **Storage**: SSD storage with automatic backups
- **Network**: Private network within Railway
- **SSL**: Enabled by default
- **Connection Pooling**: Handled by Railway

## Security Features

Railway PostgreSQL includes:
- Automatic SSL/TLS encryption
- Private network isolation
- Regular automated backups
- Monitoring and alerting
- Access logs and metrics