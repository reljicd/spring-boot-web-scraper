# Spring Boot Web Scrapping App

## About

This is a demo project. The idea was to build some basic web scrapping app, using Java 8 lambdas and streams.

It was made using Spring Boot, Spring Security, Thymeleaf, Spring Data JPA, Spring Data REST. Database is in memory H2.

There is a login and registration functionality included.

Users can add web links to their profile and tag them. 
Links are also suggested based on data scraping of the website on given link.

Note: It is made in less than one day, so some functionalities require some more brushing up.

## How to run

You can run the application from the command line with Maven. 
Or you can build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that.
Go to the root folder of the application and type:
```
./mvnw spring-boot:run
```
Or you can build the JAR file with 
```
./mvnw clean package
``` 
Then you can run the JAR file:
```
java -jar target/demo-0.0.1-SNAPSHOT.jar
```
The application should be up and running within a few seconds.

Go to the web browser and visit `http://localhost:8090/home`

Admin username: **admin**

Admin password: **admin**

User username: **dusan**

User password: **password**

In `/src/main/resources/application.properties` file it is possible to change admin username/password,
as well as change the port number.

## Helper Tools

### HAL REST Browser

Go to the web browser and visit `http://localhost:8090/`

You will need to be authenticated to be able to see this page.

### H2 Database web interface

Go to the web browser and visit `http://localhost:8090/h2-console`

In field **JDBC URL** put 
```
jdbc:h2:mem:symphony_db
```

In `/src/main/resources/application.properties` file it is possible to change both
web interface url path, as well as the datasource url.
