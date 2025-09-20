# Deploy Backend

This directory contains only Docker deployment files for Railway.

Railway should detect this as a Docker project and use the Dockerfile to build the Java application from the parent backend directory.

## Files:
- `Dockerfile` - Multi-stage build pulling source from `../backend/temp-hide/`
- `railway.toml` - Forces Railway to use Docker
- `README.md` - This file

Point your Railway project to this `deploy-backend` directory instead of the `backend` directory.