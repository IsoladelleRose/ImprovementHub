# PostgreSQL Database Setup for ImprovementHub

## 1. Install PostgreSQL

### Windows:
1. Download PostgreSQL from: https://www.postgresql.org/download/windows/
2. Run the installer and follow the setup wizard
3. Remember the password you set for the `postgres` user
4. Default port is 5432

### Using Docker (Alternative):
```bash
docker run --name improvement-hub-db \
  -e POSTGRES_DB=improvement_hub \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15
```

## 2. Create Database

### Using psql command line:
```sql
-- Connect to PostgreSQL
psql -U postgres -h localhost

-- Create database
CREATE DATABASE improvement_hub;

-- Connect to the new database
\c improvement_hub;

-- Verify connection
\dt
```

### Using pgAdmin (GUI):
1. Open pgAdmin
2. Connect to your PostgreSQL server
3. Right-click "Databases" → "Create" → "Database"
4. Name: `improvement_hub`
5. Click "Save"

## 3. Update Application Configuration

The `application.properties` file has been configured with:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/improvement_hub
spring.datasource.username=postgres
spring.datasource.password=password
```

**Important**: Update the password in `application.properties` to match your PostgreSQL installation.

## 4. Start the Application

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

## 5. Verify Database Tables

The following tables will be automatically created by Hibernate:

- `partners` - Stores partner/company registration data
- `ideas` - Stores idea submissions

## 6. API Endpoints

### Partner Endpoints:
- `POST /api/partners/register` - Register a new partner
- `GET /api/partners` - Get all partners
- `GET /api/partners/{id}` - Get partner by ID
- `GET /api/partners/search?companyName=name` - Search partners

### Idea Endpoints:
- `POST /api/ideas/register` - Register a new idea
- `GET /api/ideas` - Get all ideas
- `GET /api/ideas/{id}` - Get idea by ID
- `GET /api/ideas/search?keyword=term` - Search ideas
- `GET /api/ideas/wants-help` - Get ideas that want help

## 7. Test the Setup

You can test the API using curl or Postman:

```bash
# Test partner registration
curl -X POST http://localhost:8080/api/partners/register \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Test Company",
    "contactPerson": "John Doe",
    "email": "john@testcompany.com"
  }'

# Test idea registration
curl -X POST http://localhost:8080/api/ideas/register \
  -H "Content-Type: application/json" \
  -d '{
    "coreConcept": "AI-powered solution",
    "problemOpportunity": "Solving data analysis problems",
    "email": "inventor@example.com"
  }'
```

## Database Schema

The database will automatically create these tables:

### partners table:
- id (BIGSERIAL PRIMARY KEY)
- company_name (VARCHAR)
- vat_number (VARCHAR)
- contact_person (VARCHAR)
- street_address (VARCHAR)
- city (VARCHAR)
- postal_code (VARCHAR)
- country (VARCHAR)
- email (VARCHAR UNIQUE)
- interests_skills (TEXT)
- created_at (TIMESTAMP)

### ideas table:
- id (BIGSERIAL PRIMARY KEY)
- core_concept (TEXT)
- problem_opportunity (TEXT)
- wants_help (BOOLEAN)
- user_role (TEXT)
- email (VARCHAR)
- created_at (TIMESTAMP)