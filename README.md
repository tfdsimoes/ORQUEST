# ORQUEST project - Register Hours of employees

## Introduction
This is a solution for the challenge of ORQUEST.

## Technologies
These project is build with JAVA8 for backend and Angular for frontend.

### General
The project run with (docker)[https://docs.docker.com/get-docker/] and (docker-compose)[https://docs.docker.com/compose/install/] and need to be installed.

### Backend

#### Frameworks

| Lib | Usage |
| --- | --- |
| spring-boot-starter-web| Starter for building web, including RESTful, applications using Spring MVC |
| spring-boot-starter-test | Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito (no tests implemented) |
| lombok | Automatic Resource Management, automatic generation of getters, setters, equals, hashCode and toString, ... |
| jackson-databind | High-performance JSON processor (parser, generator) |
| springdoc-openapi | Lib responsabel to create yml documentation and swagger ui |
| spring-boot-starter-data-jpa | Lib for access and manipulate databases using hibernate |
| mapstruct | Code generator that greatly simplifies the implementation of mappings between Java bean types based |
| docker-maven-plugin | A Maven plugin for building and pushing Docker images |

#### Variables of environment
These are several variables that can be configurated in the backend:

| Var | Description |
| --- | --- |
| SERVER_PORT | port where the server will run (do not forget to map in docker file) |
| DB_URL | connection to database |
| DB_USERNAME | username of database |
| DB_PASSWORD | password of database |
| DB_DRIVERCLASSNAME | driver used to connect to database |
| DB_DIALECT | dialect used by hibernate to the database |
| PATH_API_DOCS | configure path to access yml endpoints | 
| PATH_SWAGGER | configure path to access swagger |

All have predefined values to run.

#### Documentation 

#### Code
Most of the code have javadoc in the methods and classes.

#### Endpoints
Documentation can be find when lunching the project in [yml](http://localhost:8080/api-docs.yaml) and with [swagger](http://localhost:8080/swagger.html) there is missing information. 

There is also a collection of [Postman](https://www.getpostman.com/) in the root of the project.

### Frontend

#### Variables of environment

## Building and Running
Go to backend project and run:
```bash
$ ./mvnw clean package
```

Now go to the frontend and run:
```bash
$ docker build --rm -t orquest/frontend:0.0.1-SNAPSHOT .
```

These will build the project images and create a docker images.

To run everything just run in the root of the project
```bash
$ docker-compose -f docker-compose-prod.yml up
```

Backend will be served in [localhost:8080](localhost:8080) and frontend [localhost:8081](localhost:8081)

To login in frontend insert the employee number that is already inserted.

## Tests

### Unit
Not implemented

### Integration
Not implemented
