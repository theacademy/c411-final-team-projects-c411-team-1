# ETrade - Stock Trading Application

A full-stack stock trading simulation built with Spring Boot and React.

## Overview

ETrade is a stock trading platform that allows users to create portfolios, search for stocks, execute buy/sell transactions, and track investment performance. The application integrates with the Alpha Vantage API for real-time stock data.

## Features

- User authentication with JWT
- Portfolio management
- Stock search and real-time price data
- Transaction execution (buy/sell)
- Portfolio performance visualization
- Transaction history tracking

## Technology Stack

### Backend
- Java 11
- Spring Boot, Spring Data JPA, Spring Security
- MySQL
- JWT authentication

### Frontend
- React
- React Router
- Axios
- Recharts for data visualization

## Getting Started

### Prerequisites
- Java 11+
- Node.js and npm
- MySQL

### Backend Setup
```bash
# Clone repository
git clone https://github.com/theacademy/c411-final-team-projects-c411-team-1.git

# Navigate to backend directory
cd etrade

# Build and run
mvn clean install
mvn spring:run
```

### Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

The application will be available at http://localhost:3000

## Database Setup

Run the SQL scripts in the `Database` folder to set up the necessary schema:
- `etrade-schema.sql` - For production database
- `etradetest-schema.sql` - For test database

## API Documentation

The application exposes RESTful APIs for:
- User management
- Portfolio operations
- Stock data retrieval
- Transaction execution

Default API endpoint: http://localhost:8080/api/

## Security

- JWT-based authentication
- Password encryption with BCrypt
- CORS configuration for frontend integration