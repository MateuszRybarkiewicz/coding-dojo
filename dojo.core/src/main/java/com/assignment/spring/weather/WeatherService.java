package com.assignment.spring.weather;

import com.assignment.spring.api.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UrlProvider urlProvider;

    public WeatherResponse getWeather(String cityName) {
        String url = urlProvider.buildUrl(cityName);
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
        return response.getBody();
    }
}
