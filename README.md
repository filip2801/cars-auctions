# Cars Auction Platform

## Overview

The Cars Auction Platform is designed to facilitate the auction process for cars, allowing multiple dealers log in and place their bids on multiple running auctions. 


## Technology Stack

- **Java 17**: Programming language
- **Spring Boot**: Simplifies the development of new applications.
- **PostgreSQL**: Acts as the primary database, chosen for its reliability and feature set.
- **Flyway**: Manages database migrations, ensuring schema consistency across environments.
- **Spring Data JPA**: Facilitates the implementation of JPA-based repositories.
- **Spring Security**: Provides authentication and authorization functionalities.
- **Quartz Scheduler**: Manages scheduled tasks, such as auction timing controls.
- **Spring AMQP**: Handles event-driven communication with external systems, enabling real-time updates to auction participants.
- **Lombok**: Reduces boilerplate code for model/data objects.
- **Maven**: Manages project dependencies and build process.
- **Spock Framework**: Provides a powerful testing platform, leveraging Groovy for clearer tests.
- **Testcontainers**: Offers lightweight, throwaway instances of common databases, Rabbitmq, or anything else that can run in a Docker container for testing.

## Setup and Installation

Requirements:
- JDK 17
- docker
- docker-compose

### Starting application
#### Start postgres or rabbitmq
You can start them using docker compose. To do that got to application directory run `docker-compose up -d`.
To stop containers and remove them run `docker-compose down`.

#### Starting java application
To build jar run `./mvn package -DskipTests`.
To start application run `java -jar target/cars-application-0.0.1-SNAPSHOT.jar`.

### Running tests
Run ```./mvnw clean test```.
Some tests require postgres and rabbitmq service, so they will be automatically started as docker containers