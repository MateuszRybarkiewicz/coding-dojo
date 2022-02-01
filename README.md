Spring Boot Coding Dojo
---

### How to run the code

- Build application with mvn clean install
- Set environment variable to pass API Key
    - WEATHER_APIKEY=3c15d2bcde4f7a48912bb6a919f1d2d7
- Run jar in application module
    - java -jar dojo.application/target/dojo.application-0.0.1-SNAPSHOT.jar
- Send sample request
  > curl --location --request GET 'localhost:8080/weather?city=moscow'
