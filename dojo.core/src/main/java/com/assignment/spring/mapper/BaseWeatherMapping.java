package com.assignment.spring.mapper;

import org.mapstruct.Mapping;

@Mapping(target = "city", source = "name")
@Mapping(target = "country", source = "sys.country")
@Mapping(target = "temperature", source = "main.temp")
@Mapping(target = "id", ignore = true)
public @interface BaseWeatherMapping {
}
