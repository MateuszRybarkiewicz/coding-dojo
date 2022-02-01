package com.assignment.spring.mapper;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.entities.WeatherEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherMapperTest {

    private WeatherMapper mapper = WeatherMapper.INSTANCE;

    @Test
    public void givenMappedObject_itIsMapped() {
        WeatherResponse weather = parseTextToJson("weather-response.json", WeatherResponse.class);

        WeatherEntity map = mapper.map(weather);

        assertEquals(weather.getSys().getCountry(), map.getCountry());
        assertEquals(weather.getName(), map.getCity());
        assertEquals(weather.getMain().getTemp(), map.getTemperature());
    }

    private <T> T parseTextToJson(String path, Class<T> clazz) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        try (InputStream in = classPathResource.getInputStream()) {
            String text = IOUtils.toString(in, String.valueOf(StandardCharsets.UTF_8));
            return new ObjectMapper().readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test resource: " + path, e);
        }
    }
}
