## CMEXPRT Ordering Web Service

### Prerequisites
- Java 8
- Maven 3.6.x

Before running, configure `spring.datasource` parameters at `application.properties` according to your database.

### Running the project

Open a terminal, set the cursor at project's root directory and choose one of the following three ways:

#### (1) Maven's Spring Boot plugin run goal
`mvn spring-boot:run`

#### (2) Generating a standalone JAR package
`mvn clean package spring-boot:repackage`

#### (3) Embedding the JAR package in a Docker image
`mvn clean package spring-boot:repackage docker:build`

### API documentation

While the project is running, the web API documentaiton can be found at `http://host:port/api/v1/swagger-ui/`.