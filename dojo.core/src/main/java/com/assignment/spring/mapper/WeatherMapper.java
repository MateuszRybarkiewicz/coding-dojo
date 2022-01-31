package com.assignment.spring.mapper;


import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.entities.WeatherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WeatherMapper {
    WeatherMapper INSTANCE = Mappers.getMapper(WeatherMapper.class);

    @BaseWeatherMapping
    WeatherEntity map(WeatherResponse response);
}
