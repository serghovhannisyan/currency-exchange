## Project Name

Currency Exchange Rate app

An application uses various currency exchange APIs in order to retrieve data and keep and use it later. 
Built with Spring Boot, MongoDB(embedded), Spring WebFlux, Docker, OpenApi v3, JUnit 5 and Mockito

## Installation and Setup Instructions
Clone down this repository. You will need JDK 8 and Maven installed on your machine.
Running the app very first time could take some time to download embedded mongodb.

## To Run Test Suite:

`mvn clean test`

## Installation:

`mvn clean install`

## To create and run Docker

`docker-compose up -d`

## To Start Server locally without Docker:

remove test scope for embedded mongodb inside pom.xml
and change active profile to local inside application.yml
and then run

`mvn spring-boot:run`

## To use REST Api

`http://localhost:9090/api/v1/**`

##  Docs 
Docs are available by this url:
`http://localhost:9090/docs/swagger-ui`

Application on start-up creates use(admin,password) with ADMIN role
And adds 2 currencies USD and EUR

You can add new currencies but be careful because most of the APIs doesn't support like AMD or others