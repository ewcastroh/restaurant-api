# RESTAURANT API
Restaurant API allows storing new employee information as an Admin Role with some specific data. 
The API has been built with next specifications:  
1. JDK 11
2. 2.3.3.RELEASE
3. H2 Database (Embedded database with some initial data to test API)
4. Swagger 2

## Running application
Application works in port 8080. After run the application it's possible run in next URL:  
http://localhost:8080  

### Checking Database
To check the embedded database you can go to next URL:   
http://localhost:8080/h2-console

Type options below:  
Saved Settings:	Generic H2 (Embedded)  
Setting Name: Generic H2 (Embedded)  
Driver Class: org.h2.Driver  
JDBC URL: jdbc:h2:mem:restaurant_db  
User Name: sa  
Password:  

### Checking API documentation
To check API documentation you can go to next URL:  
http://localhost:8080/swagger-ui