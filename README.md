# IMS (Inventory Management System)

IMS is an offline-first web application for tracking devices, assignments, issues, and reports in one place.

## Project Name
- IMS
- Meaning: Inventory Management System

## SysAdmin Credentials
- Username: 24RP07206
- Password: 24RP02943

## Project Structure Highlights
- `src/main/java`: Spring Boot application code (controllers, services, models, repositories)
- `src/main/resources/templates`: Thymeleaf UI pages
- `src/main/resources/application.properties`: app and database configuration
- `ims.sql`: database seed file for SysAdmin account
- `data/`: local H2 file database storage

## Setup Instructions
1. Install Java 17+ and Maven (or use the included Maven wrapper files).
2. From the project root, run:
   - Windows PowerShell: `./mvnw.cmd spring-boot:run`
3. Open browser:
   - `http://localhost:8080/`

If port 8080 is busy, run on another port:
- `./mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"`
- Then open `http://localhost:8081/`

## How To Navigate IMS
1. Open the login page (`/login`) and sign in with the SysAdmin credentials above.
2. Dashboard (`/dashboard`): view overview metrics.
3. Devices (`/devices`): register, view, edit, search, and filter devices.
4. Assignments (`/assignments`): assign devices, track active assignments, and returns.
5. Issues (`/issues`): report, view, filter, and resolve issues.
6. Reports (`/reports`): open device, assignment, issue, inventory status, and audit reports.

## Database Seed Notes
- The application seeds the SysAdmin account at startup through application initialization.
- You can also apply `ims.sql` manually on an H2 database to insert/update the SysAdmin record.
