package com.assignment.spring.repositories;

import com.assignment.spring.entities.WeatherEntity;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {
}
