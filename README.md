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
- **RabbitMQ**: Facilitates asynchronous message queuing and inter-service communication.
- **Lombok**: Reduces boilerplate code for model/data objects.
- **Maven**: Manages project dependencies and build process.
- **Spock Framework**: Provides a powerful testing platform, leveraging Groovy for clearer tests.
- **Testcontainers**: Offers lightweight, throwaway instances of common databases, Rabbitmq, or anything else that can run in a Docker container for testing.

## Design Considerations

- **Concurrency Control**: Implemented optimistic locking to handle concurrent bids, ensuring data consistency.
- **Scalability**: The architecture is designed for scalability, utilizing RabbitMQ asynchronous message processing and inter-service communication. This allows the system to handle a high number of concurrent auctions and bids.
- **Security**: Utilized Spring Security to manage authentication and protect sensitive endpoints. Endpoints are secured by Basic Authentication.

## Setup and Installation

Requirements:
- ***JDK 17***: java environment
- ***docker***: to run tests
- ***docker-compose***:  to start postgres and RabbitMQ containers.

### Starting application
#### Start Postgres and RabbitMQ
You can start them using docker compose. To do that got to application directory run `docker-compose up -d`.
To stop containers and remove them run `docker-compose down`.

#### Starting java application
To build jar run `./mvn package -DskipTests`.
To start application run `java -jar target/cars-application-0.0.1-SNAPSHOT.jar`.

To start application with custom params run it like `java -jar -DRABBITMQ_HOST=8888 target/cars-application-0.0.1-SNAPSHOT.jar`.
List of available parameters can be found in `application.yml`.

On first start application will create first agent user with credentials `admin/admin`.

### Running tests
Run ```./mvnw clean test```.
Some tests require postgres and rabbitmq service, so they will be automatically started as docker containers.

## Future Enhancements

- Enhance security with JWT-based authentication.
- Add database indexes to speed up queries.
- Introduce cars catalog.
