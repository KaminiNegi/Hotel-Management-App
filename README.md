# User Management System - Spring Boot

## Introduction
This is a **User Management System** built with **Spring Boot, JWT Authentication, jQuery, Bootstrap, and DataTables**. The system provides authentication, user registration, and a dashboard to manage users.

## Features
- **User Registration & Login** (JWT-based authentication)
- **Secure User Management API** (Get, Delete Users)
- **Frontend** with **jQuery, Bootstrap, and DataTables**
- **Swagger API Documentation**
- **H2 In-Memory Database Support**

## Requirements
- **Java 17** or later
- **Maven**
- **Spring Boot 3.x**
- **Postman** (for API testing, optional)

## Installation & Running
### 1. Clone the Repository
```sh
git clone <repository-url>
cd user-management-system
```

### 2. Build & Run the Application
```sh
mvn clean install
mvn spring-boot:run
```

### 3. Access API & UI
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)
- **H2 Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Frontend Dashboard:** Open `index.html` in a browser

## API Endpoints
- **Register User:** `POST /api/v1/auth/register`
- **Login:** `POST /api/v1/auth/authenticate`
- **Refresh Token:** `POST /api/v1/auth/refresh-token`
- **Get All Users:** `GET /api/v1/users`
- **Get User by ID:** `GET /api/v1/users/{id}`
- **Delete User:** `DELETE /api/v1/users/{id}`

## Database Configuration
By default, the system uses an **H2 in-memory database**. If using **MySQL/PostgreSQL**, update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## JWT Authentication
After login, the API returns an **access token** and a **refresh token**. Include the access token in API requests:
```
Authorization: Bearer <JWT_ACCESS_TOKEN>
```
If expired, use the refresh token to get a new one.

## Contributing
Feel free to contribute by submitting pull requests or opening issues.

## License
This project is licensed under the **MIT License**.

---
### **Author:** your-name

