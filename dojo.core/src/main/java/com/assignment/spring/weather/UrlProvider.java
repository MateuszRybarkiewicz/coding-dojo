package com.assignment.spring.weather;

import com.assignment.spring.properties.WeatherConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UrlProvider {
    private static final String WEATHER_API_URL = "{host}/data/2.5/weather?q={city}&APPID={appid}";
    @Autowired
    private WeatherConnectionProperties properties;

    public String buildUrl(String city) {
        String url = WEATHER_API_URL
                .replace("{host}", properties.getHost())
                .replace("{city}", city)
                .replace("{appid}", properties.getApiKey());
        return url;
    }
}
