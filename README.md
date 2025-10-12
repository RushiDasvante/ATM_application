# ATM_application

🏧 ATM Management System (Spring Boot + MySQL)

The ATM Management System is a Spring Boot application that simulates real-world ATM functionalities such as user login, balance inquiry, deposits, withdrawals, money transfers, and transaction history tracking.
It uses Spring Boot, Spring Data JPA, and MySQL for backend logic and persistent data storage.

🔹 Key Features

User Authentication – Secure login using userId and PIN.

Check Balance – Instantly view your current account balance.

Deposit Money – Deposit funds using simple REST endpoints.

Withdraw Money – Withdraw cash (with balance validation).

Transfer Funds – Transfer money between two accounts securely.

Transaction History – View detailed transaction logs for each account, including type, amount, timestamp, and notes.

MySQL Integration – All user, account, and transaction data are stored in a relational MySQL database.

🧱 Tech Stack

Backend: Spring Boot (Java 21), Spring Data JPA

Database: MySQL

Build Tool: Maven

API Testing: Postman

🧩 Entity Overview

User → Contains user credentials and account mapping.

Account → Stores balance and linked user info.

Transaction → Records every deposit, withdrawal, and transfer operation.

🔗 Sample Endpoints
Action	Method	URL	Params
Login	GET	/atm/login?userId=U001&pin=1234	userId, pin
Check Balance	GET	/atm/balance/ACC001	accountNumber
Deposit	POST	/atm/deposit?accountNumber=ACC001&amount=2000	accountNumber, amount
Withdraw	POST	/atm/withdraw?accountNumber=ACC001&amount=1000	accountNumber, amount
Transfer	POST	/atm/transfer?from=ACC001&to=ACC002&amount=500	from, to, amount
Transaction History	GET	/atm/transactions/ACC001	accountNumber
🧠 How It Works

The user logs in using userId and PIN.

Each operation (deposit, withdraw, transfer) automatically creates a new Transaction record.

All actions are stored persistently in the MySQL database.

Users can fetch transaction history at any time using their account number.

🧾 Example Transaction Response
[
  {
    "id": 1,
    "type": "DEPOSIT",
    "amount": 2000.0,
    "note": "Deposit money",
    "timestamp": "2025-10-12T10:30:00"
  },
  {
    "id": 2,
    "type": "WITHDRAW",
    "amount": 500.0,
    "note": "Cash withdrawal",
    "timestamp": "2025-10-12T11:00:00"
  }
]

🚀 Future Enhancements

JWT-based secure login system

Frontend UI using React or Angular

Admin dashboard to manage users and accounts

Email or SMS notifications for transactions
