# Balance Service

The Balance Service is a Spring Boot application designed to manage users, balances, transactions, and reservations. It provides a set of RESTful APIs for performing operations related to user management, balance management, transactions (deposit, withdrawal, transfer), and reservation handling.

## Table of Contents

- [Features](#features)
- [Setup and Installation](#setup-and-installation)
- [Future Plans](#future-plans)

## Features

- **User Management**: Create and retrieve user details.
- **Balance Management**: Create and retrieve user balances.
- **Transaction Management**: Perform deposits, withdrawals, and transfers.
- **Reserve Management**: Create, cancel, and complete reserves.

## Getting Started

### Prerequisites
- **Java 17**
- **Maven**
- **Docker (for containerization)**

### Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/ivamly/Balance_service.git
    ```

2. **Build the project:**

    ```bash
    mvn clean install
    ```

3. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

4. **Access the application:**

   The application will be available at [http://localhost:8080](http://localhost:8080).

### Docker

1. **Pull the Docker image:**

    ```bash
    docker pull ivamly/transaction-app
    ```

2. **Run the Docker container:**

    ```bash
    docker run -p 8080:8080 ivamly/transaction-app
    ```

## Future Plans

I have several plans for improving the Balance Service, including:

- **Increased Test Coverage**: Implement additional unit and integration tests to ensure the robustness and reliability of the application.
- **Enhanced API Documentation**: Expand the API documentation to include more detailed descriptions, request/response examples, and authentication details.
- **User Authentication and Authorization**: Integrate user authentication and authorization to secure sensitive operations.
- **Endpoint Testing with cURL and Postman**: Develop a comprehensive set of cURL commands and a Postman collection to thoroughly test and validate all API endpoints.
- **Database Migration to MySQL**: Transition the application from the current H2 database to MySQL to enhance scalability and support for production environments.
- **Additional Features**: Explore and implement additional features such as transaction history, user notifications, and more detailed reporting.
