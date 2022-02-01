Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the
result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

As the new engineer leading this project, your first task is to make it production-grade, feel free to refactor any
piece necessary to achieve the goal.

### How to run the code

- Build application with mvn clean install
- Set environment variable to pass API Key
    - WEATHER_APIKEY=3c15d2bcde4f7a48912bb6a919f1d2d7
- Run jar in application module
    - java -jar dojo.application/target/dojo.application-0.0.1-SNAPSHOT.jar
- Send sample request
  > curl --location --request GET 'localhost:8080/weather?city=moscow'

> **DO NOT create a Pull Request with your solution**

### Footnote

It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
