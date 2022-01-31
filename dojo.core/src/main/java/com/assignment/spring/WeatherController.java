package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.mapper.WeatherMapper;
import com.assignment.spring.properties.WeatherConnectionProperties;
import com.assignment.spring.repositories.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WeatherController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private WeatherConnectionProperties properties;

    private WeatherMapper mapper = WeatherMapper.INSTANCE;

    @RequestMapping("/weather")
    public WeatherEntity weather(HttpServletRequest request) {
        String city = request.getParameter("city");
        String url = buildUrl(city);
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
        return mapper(response.getBody());
    }

    private String buildUrl(String city) {
        String url = Constants.WEATHER_API_URL
                .replace("{host}", properties.getHost())
                .replace("{city}", city)
                .replace("{appid}", properties.getApiKey());
        return url;
    }

    private WeatherEntity mapper(WeatherResponse response) {
        WeatherEntity entity = mapper.map(response);
        return weatherRepository.save(entity);
    }
}
