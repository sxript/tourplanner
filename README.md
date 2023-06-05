# Setup
## Dev Environment
**Postgres Setup**
1. Start Postgres & Vault
   - `make up`
2. Connect to vault running on Port :8989
   - login using the token: 00000000-0000-0000-0000-000000000000
   - add the secrets like in the example.vault.json

# Design & Architecture

## 1 Introduction
This document provides an overview of the design and architecture of the project. The project consists of a backend component developed using Spring Boot with Vault for secret management and a frontend component developed using Spring Boot with JavaFX. The backend component follows the reactive paradigm for writing asynchronous code.

## 2 Backend Architecture
The backend architecture is based on the Spring Boot framework, which provides a robust and scalable foundation for building Java applications. The use of Spring Boot allows for easy configuration, dependency injection, and integration with other Spring projects.

### 2.1 Spring Boot

Spring Boot is a framework that simplifies the development of Java applications by providing a convention-over-configuration approach. It allows developers to quickly create standalone, production-ready applications with minimal setup and configuration.

### 2.2 Reactive Paradigm

The backend component of the project follows the reactive programming paradigm. Reactive programming is an asynchronous programming model that enables the efficient handling of concurrent requests and supports high-performance, scalable applications. It is based on the principles of non-blocking I/O, event-driven architecture, and functional programming.

### 2.3 Vault for Secret Management

Secret management is an important aspect of application security. In this project, Vault is used for securely storing and managing secrets, such as API keys, database credentials, and other sensitive information. Vault provides a centralized and encrypted storage solution, ensuring that secrets are protected from unauthorized access.

## 3 Frontend Architecture
The frontend component of the project is developed using Spring Boot with JavaFX, which is a framework for building rich client applications in Java. JavaFX provides a set of libraries and tools for creating user interfaces and interacting with backend services.

### 3.1 Spring Boot with JavaFX

Spring Boot with JavaFX allows for the development of modern, responsive, and cross-platform user interfaces. It integrates with the backend seamlessly, enabling efficient communication and data transfer between the frontend and backend components.

### 3.2 Secrets Storage in .env File

In the frontend component, secrets are stored in an .env file. The .env file is a text file that contains key-value pairs representing environment variables. Storing secrets in a separate file ensures that sensitive information is not exposed in the source code repository.

Integration between Backend and Frontend
The backend and frontend components are integrated using RESTful APIs. The backend exposes a set of RESTful endpoints that the frontend can invoke to retrieve data and perform operations. The frontend component communicates with the backend asynchronously, leveraging the reactive programming model.

## 4 Conclusion
This documentation has provided an overview of the design and architecture of the project. The backend component is built using Spring Boot with Vault for secret management, following the reactive programming paradigm. The frontend component is developed using Spring Boot with JavaFX and stores secrets in an .env file. The integration between the backend and frontend is achieved through RESTful APIs.

# Lessons Learned

During the development of the project, several lessons were learned that can be valuable for future projects. Here are some of the key lessons learned:

1. Proper Secret Management: One important lesson learned was the significance of proper secret management. Using Vault for secret storage and management in the backend component proved to be a robust solution. Storing secrets in an .env file for the frontend component also worked well. This experience highlighted the importance of securely storing sensitive information and implementing effective secret management practices.
2. Reactive Programming Benefits: Adopting the reactive programming paradigm in the backend component proved to be advantageous. Reactive programming allowed for the development of asynchronous and non-blocking code, enabling better performance, scalability, and responsiveness. This experience emphasized the benefits of reactive programming in building high-performance applications.
3. Framework Selection: Choosing the right frameworks for the project played a crucial role in its success. Spring Boot provided a solid foundation for developing both the backend and frontend components. Its ease of configuration, dependency injection, and integration capabilities streamlined the development process. The use of JavaFX for the frontend component allowed for the creation of a modern and interactive user interface.
4. Separation of Concerns: Applying the principle of separation of concerns was essential for maintaining a clean and manageable codebase. Separating the backend and frontend components into separate layers allowed for better organization, modularity, and ease of maintenance. This lesson highlighted the importance of designing software with clear separation of responsibilities.
5. Documentation and Collaboration: Effective documentation and collaboration were key factors in the success of the project. Maintaining up-to-date documentation helped team members understand the design and architecture, facilitating smoother collaboration. This experience emphasized the importance of clear communication, regular updates, and collaborative tools for efficient teamwork.
6. Testing and Quality Assurance: Ensuring proper testing and quality assurance processes were essential for delivering a reliable and robust application. Writing comprehensive unit tests, performing integration tests, and conducting manual testing helped identify and fix issues early in the development cycle. This lesson highlighted the importance of investing time and resources in testing and quality assurance activities.
7. Error Handling and Logging: Implementing robust error handling mechanisms and comprehensive logging proved to be invaluable during debugging and troubleshooting. Properly handling errors and logging relevant information helped in identifying and resolving issues effectively. This lesson emphasized the importance of proactive error handling and logging practices.
