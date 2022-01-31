package com.assignment.spring.properties;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "weather")
@Data
@Validated
public class WeatherConnectionProperties {

    @NotBlank
    private String apiKey;

    public String getHost() {
        if (StringUtils.isEmpty(port)) {
            return host;
        } else {
            return host + ":" + port;
        }
    }

    @NotBlank
    @Getter(AccessLevel.NONE)
    private String host;

    @Getter(AccessLevel.NONE)
    private String port;

}
