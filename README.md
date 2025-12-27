Library Management System – Backend API
Project Description

The Library Management System is a backend REST API built using Spring Boot that manages books, users, loans, reservations, and fines.
It demonstrates a complete object-oriented software solution with clean architecture, proper layering, and real-world deployment.

The system supports:

Managing books and book copies
Borrowing and returning books
Reserving unavailable books
Automatically calculating fines for overdue loans
Secure, structured REST APIs
Deployment with PostgreSQL on Render

Main Features

CRUD operations for books and users
Loan management with due dates
Reservation system
Fine calculation using strategy pattern
Pagination and sorting
Swagger/OpenAPI documentation
Separate configurations for development and production
Deployed backend service

Object-Oriented Programming Concepts Used
Classes & Objects

Each domain concept (Book, User, Loan, Reservation, Fine) is modeled as a class.

✅ Encapsulation

All entity fields are private and accessed through public methods.

✅ Inheritance

PerDayFineStrategy implements FineStrategy

✅ Polymorphism

Different fine calculation strategies can be plugged in without changing service logic

✅ Abstraction

Service interfaces

Strategy interface for fine calculation

✅ HAS-A Relationships

Book has BookCopies

User has Loans

Loan has Fine

✅ Clean Architecture

Controller → Service → Repository → Database

🏗 Project Structure
src/main/java/com/example/library
│
├── controller
│   ├── BookController
│   ├── LoanController
│   └── UserController
│
├── service
│   ├── BookService
│   ├── LoanService
│   ├── ReservationService
│   └── FineService
│
├── domain
│   ├── model
│   ├── enums
│   └── strategy
│
├── repository
│   ├── BookRepository
│   ├── LoanRepository
│   └── UserRepository
│
└── LibraryApplication.java


     """The UML diagram is in docs folder in project root"""


Technologies Used

Java 17
Spring Boot 3
Spring Data JPA
Hibernate
PostgreSQL (Production)
H2 Database (Development)
Maven
Swagger / OpenAPI
Docker
Render Cloud

How to Run Locally
Prerequisites

Java 17+
Maven

Run
mvn spring-boot:run


Access:

API: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2

Deployment

The backend is deployed on Render using PostgreSQL.

Live API URL
https://library-backend-bjx6.onrender.com

Swagger UI
https://library-backend-bjx6.onrender.com/swagger-ui.html

Notes

H2 is used for local testing (data resets on restart)

PostgreSQL is used in production (data persists)

Configuration is managed using Spring profiles