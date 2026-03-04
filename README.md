## 🚀 Secure ATM Banking Backend System

A secure ATM banking backend system built using Spring Boot and JWT authentication that simulates real-world banking operations such as deposits, withdrawals, transfers, and transaction tracking.

This project demonstrates:

Secure authentication

Transaction management

RESTful API design

Backend system architecture

📌 Project Highlights

✔ JWT Authentication (Access Token + Refresh Token)
✔ Secure REST APIs using Spring Security
✔ Fund Transfer with Transaction Safety
✔ Deposit & Withdraw Operations
✔ Transaction History Tracking
✔ MySQL Database Integration
✔ Clean Layered Architecture
✔ Exception Handling with Global Handler

🏗️ System Architecture

             Client (Postman / Frontend)
                       │
                       ▼
                REST Controller
                       │
                       ▼
                  Service Layer
             (Business Logic)
                       │
                       ▼
                Repository Layer
               (JPA / Hibernate)
                       │
                       ▼
                    MySQL DB
Security Flow

          Login Request
               │
               ▼
          Generate Access Token + Refresh Token
               │
               ▼
          Access Protected APIs
               │
               ▼
          Access Token Expired
               │
               ▼
          Use Refresh Token → Generate New Access Token
⚙️ Tech Stack
# Backend:
Java 21
Spring Boot
Spring Security
JWT Authentication
Hibernate / JPA
Maven
Database
MySQL

# Tools:
Postman
Git & GitHub
IntelliJ / Eclipse

✨ Features
Authentication

User Registration

Secure Login

Access Token & Refresh Token

Token Validation

Banking Operations

Deposit Money

Withdraw Money

Transfer Funds

Check Balance

View Transaction History

Security

Custom JWT Filter

Protected APIs

Password Encryption

Secure Token Validation

📂 Project Structure

      atm-system
      │
      ├── controller
      │   ├── AuthController
      │   └── TransactionController
      │
      ├── service
      │   ├── ATMService
      │
      ├── repository
      │   ├── UserRepository
      │   ├── AccountRepository
      │   └── TransactionRepository
      │
      ├── security
      │   ├── JwtUtil
      │   ├── JwtAuthFilter
      │   └── SecurityConfig
      │
      ├── dto
      │   ├── LoginRequest
      │   ├── LoginResponse
      │   └── RefreshTokenRequest
      │
      └── exception
          └── GlobalExceptionHandler
🔐 Authentication Example
Login API

POST /atm/login

    Response:
    
    {
      "accessToken": "jwt-access-token",
      "refreshToken": "jwt-refresh-token",
      "tokenType": "Bearer",
      "userId": "USR001",
      "name": "Rushi",
      "accountNumber": "ACC12345",
      "balance": 5000
    }
📡 API Endpoints
Authentication APIs
Method	Endpoint	Description
  POST	/atm/register	Register new user
  POST	/atm/login	Login user
  POST	/atm/refresh	Refresh access token
Banking APIs
Method	Endpoint	Description
  GET	/atm/balance	Check balance
  POST	/atm/deposit	Deposit money
  POST	/atm/withdraw	Withdraw money
  POST	/atm/transfer	Transfer money
  GET	/atm/transactions	View transaction history
🗄️ Database Schema
# User Table
    user_id
    name
    pin
    account_id
# Account Table
    account_number
    balance
    Transaction Table
    id
    account_number
    type
    amount
    timestamp

Types of Transactions:

    Deposit
    
    Withdraw
    
    Transfer Sent
    
    Transfer Received

🛠️ How to Run the Project
1. Clone Repository
git clone https://github.com/RushiDasvante/ATM_application.git
2. Open Project in IDE

IntelliJ IDEA / Eclipse

3. Configure Database

Update application.properties:

    spring.datasource.url=jdbc:mysql://localhost:3306/atm_db
    spring.datasource.username=root
    spring.datasource.password=yourpassword
4. Run Application
mvn spring-boot:run
5. Test Using Postman
   
# 📊 Project Workflow

    User → Login → JWT Generated
            │
            ▼
    Access Protected APIs
            │
            ▼
    Perform Banking Transactions
            │
            ▼
    Data Stored in Database
    
🎯 Key Learning Outcomes

Implemented JWT authentication with refresh tokens

Built secure REST APIs using Spring Security

Designed scalable backend architecture

Managed financial transactions safely

Integrated MySQL using Hibernate/JPA

Applied transaction management with @Transactional

🚀 Future Enhancements

Role-Based Access Control (RBAC)

Admin Dashboard

Microservices Architecture

Docker Deployment

API Rate Limiting

Frontend Integration (React)

👨‍💻 Author

Rushi Dasvante
Java Backend Developer

GitHub
https://github.com/RushiDasvante

LinkedIn
https://www.linkedin.com/in/rushidasvante
