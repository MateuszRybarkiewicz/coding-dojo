package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.entities.WeatherEntity;
import com.assignment.spring.mapper.WeatherMapper;
import com.assignment.spring.repositories.WeatherRepository;
import com.assignment.spring.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private WeatherService service;

    private WeatherMapper mapper = WeatherMapper.INSTANCE;

    @RequestMapping("/weather")
    public WeatherEntity weather(@RequestParam("city") String cityName) {
        WeatherResponse weather = service.getWeather(cityName);
        return mapper(weather);
    }


    private WeatherEntity mapper(WeatherResponse response) {
        WeatherEntity entity = mapper.map(response);
        return weatherRepository.save(entity);
    }
}
