package com.assignment.spring.mapper;


import com.assignment.spring.WeatherEntity;
import com.assignment.spring.api.WeatherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WeatherMapper {
    public WeatherMapper INSTANCE = Mappers.getMapper(WeatherMapper.class);

    WeatherEntity map(WeatherResponse response);
}
