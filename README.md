# Hotel-Management-App - Spring Boot

## Introduction
This is a **Hotel Room Booking System – A Smart Self-Check-in & Checkout Solution**
The Hotel Room Booking System is a web-based application designed to streamline the hotel check-in and checkout process while ensuring secure authentication and seamless room management. The system integrates JWT-based authentication and role-based access control, This system is designed to facilitate self-service check-in and checkout, enabling guests to register, log in, book, and unbook rooms directly at the hotel reception at a self-service kiosk by eliminating the need for front desk staff intervention. The system also includes an admin dashboard for user management and activity tracking admin can view the list of all users, can book room, can see the report that how many users registered with date in bar chart form.
This is built with **Spring Boot REST API, JavaScript, jQuery, HTML, CSS, Bootstrap, MYSQL database and DataTables**.

## Features
- **User Registration & Login** (JWT-based authentication)
- **Secure User Management API** (GET, POST)
- **Frontend** with **jQuery, Bootstrap, JavaScript, HTML, CSS and DataTables**
- **Postman collection API Documentation**

## Requirements
- **Java 17** or later
- **Maven**
- **Spring Boot 3.x**
- **Postman** (for API testing)

## Installation & Running
### 1. Clone the Repository
```sh
git clone https://github.com/KaminiNegi/Hotel-Management-App.git
cd Hotel-Management-App
```


### 2. Build & Run the Application
```sh
mvn clean install
mvn spring-boot:run
```

### 3. Access API & UI

- **Postman collection API documentation:** (https://documenter.getpostman.com/view/40824632/2sAYkDLziJ)
- **Frontend Dashboard:** Open `index.html` in a browser
- **Admin credentials:**
- Email - admin@gmail.com 
- Password - admin

## API Endpoints
- **Register User:** `POST /api/v1/auth/register`
- **Login:** `POST /api/v1/auth/authenticate`
- **Refresh Token:** `POST /api/v1/auth/refresh-token`
- **Get All Users:** `GET /api/v1/users`
- **Get User by ID:** `GET /api/v1/users/{id}`
- **Get All Rooms details:** `GET /api/v1/rooms
- **Room Book:** `POST /api/v1/rooms/book
- **Room UnBook:** `POST /api/v1/rooms/unbook
- **`

## Database Configuration
The system uses  **MySQL**, update `application.properties`:
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

---
### **Author:** Kamini Negi

