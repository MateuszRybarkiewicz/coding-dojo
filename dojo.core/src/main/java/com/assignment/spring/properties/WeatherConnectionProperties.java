package com.assignment.spring.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather")
@Data
public class WeatherConnectionProperties {
    private String apiKey;
}
