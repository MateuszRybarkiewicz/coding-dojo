package com.assignment.spring.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "weather")
@Data
@Validated
public class WeatherConnectionProperties {

    @NotBlank
    private String apiKey;

}
