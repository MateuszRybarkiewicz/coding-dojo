package com.assignment.spring.properties;


import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather.api")
@Value
public class WeatherConnectionProperties {
    private String apiKey;
}
