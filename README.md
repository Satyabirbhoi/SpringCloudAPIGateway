# Spring Cloud API Gateway
This is a simple Spring Cloud API Gateway implementation built with Spring Boot. The API Gateway acts as a routing and filtering mechanism for microservices.

## Features
- **Dynamic routing:** Forward requests to different services based on the URI.
- **Global Filters:** Apply custom filters for JWT authentication, API keys and IP whitelisting.
- **Circuit breaker:** Protects the microservices architecture.
- **Security:** Can be integrated with OAuth2 for authentication and authorization.
- **Load balancing:** Can be integrated with Spring Cloud LoadBalancer.

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- Spring Boot 3.x
- Spring Cloud Dependencies


## Getting Started
### Clone the repository
```bash

git clone https://github.com/Satyabirbhoi/SpringCloudAPIGateway.git
cd SpringCloudAPIGateway

```
The database configurations for storing the API keys and IPs can be done in the src/main/resources/application.properties  file 

```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3307/apikey_ip_db
spring.datasource.username=root
spring.datasource.password=Password@123
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

``` 
